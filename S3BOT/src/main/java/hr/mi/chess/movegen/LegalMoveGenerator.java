package hr.mi.chess.movegen;

import hr.mi.chess.models.BoardState;
import hr.mi.chess.models.ChessPiece;
import hr.mi.chess.models.Move;
import hr.mi.chess.movegen.helpers.BitboardUtil;
import hr.mi.chess.movegen.helpers.Bitwise;
import hr.mi.chess.movegen.helpers.MoveUtil;
import hr.mi.chess.util.constants.ChessBoardConstants;
import hr.mi.chess.util.constants.ChessConstants;
import hr.mi.chess.util.constants.ChessPieceConstants;

import java.util.*;

public class LegalMoveGenerator implements MoveGenerator{

    public static List<Move> generateMoves(BoardState boardState) {
        long pushMask = 0xFFFFFFFFFFFFFFFFL;
        long captureMask = 0xFFFFFFFFFFFFFFFFL;
        List<ChessPiece> friendlyPieces = boardState.getActiveColour() ? ChessPieceConstants.WHITE_PIECES : ChessPieceConstants.BLACK_PIECES;
        List<ChessPiece> enemyPieces = boardState.getActiveColour() ? ChessPieceConstants.BLACK_PIECES : ChessPieceConstants.WHITE_PIECES;

        List<Move> moves = new ArrayList<>();

        //generate king moves
        long kingDangerSquares = MoveUtil.getKingDangerSquares(boardState.getBitboards(), boardState.getActiveColour());
        moves.addAll(generateMovesForPiece(boardState, boardState.getBitboards()[friendlyPieces.get(ChessPieceConstants.KING).getKey()],friendlyPieces.get(ChessPieceConstants.KING), pushMask & ~kingDangerSquares, captureMask & ~kingDangerSquares));

        //check for check
        long checkers = 0L;
        long attackLine = 0L;

        //check for check by non sliders
        for (int index: ChessPieceConstants.NON_SLIDER_ATTACKERS){
            long attackMask = MoveUtil.pieceCaptures(boardState.getBitboards(), friendlyPieces.get(index), boardState.getBitboards()[friendlyPieces.get(ChessPieceConstants.KING).getKey()]);
            checkers |= attackMask & boardState.getBitboards()[enemyPieces.get(index).getKey()];
        }

        //check for check by sliders
        for (int offset: ChessPieceConstants.COMPASS_ROSE){
            long attackMask = MoveUtil.getMoveLine(boardState.getBitboards(), boardState.getBitboards()[friendlyPieces.get(ChessPieceConstants.KING).getKey()], offset, boardState.getActiveColour());
            long newCheckers = attackMask & BoardFunctions.calculateOccupiedByIndexes(boardState.getBitboards(), ChessPieceConstants.POSSIBLE_ATTACKERS_BY_OFFSET.get(offset), boardState.getPassiveColour());
            checkers |= newCheckers;

            if (newCheckers > 0L){
                //storing multiple attack lines makes no sense, because in case of a double check, the king must move
                attackLine = attackMask & ~newCheckers;
            }
        }

        int numCheckers = Long.bitCount(checkers);

        //only king moves are valid if there are more than one checkers
        if (numCheckers > 1){
            return moves;
        }

        //if there is one checker we can also evade by capturing it
        if (numCheckers == 1){
            captureMask = checkers;

            ChessPiece checkingPiece = Objects.requireNonNull(BoardFunctions.getPieceByBitboard(boardState.getBitboards(), checkers));
            //if the checking piece is a slider, we can block it by standing in the way of the attack
            if (checkingPiece.isSliding()){
                pushMask = attackLine;
            }
            else {
                pushMask = 0;
            }
        }

        //calculate pinned pieces and generate their moves
        Set<Long> pinnedPieces = new HashSet<>();
        for (int offset: ChessPieceConstants.COMPASS_ROSE) {
            long attackMaskFromKing = MoveUtil.getMoveLine(boardState.getBitboards(), boardState.getBitboards()[friendlyPieces.get(ChessPieceConstants.KING).getKey()], offset, boardState.getPassiveColour());
            long potentialAttackers = 0L;
            for (int potentialAttacker : ChessPieceConstants.POSSIBLE_ATTACKERS_BY_OFFSET.get(-offset)) {
                potentialAttackers |= boardState.getBitboards()[enemyPieces.get(potentialAttacker).getKey()];
            }
            long attackMaskFromAttacker = MoveUtil.getMoveLine(boardState.getBitboards(), potentialAttackers, -offset, boardState.getPassiveColour());
            long pinnedBitboard = attackMaskFromKing & attackMaskFromAttacker;

            if (pinnedBitboard != 0) {
                pinnedPieces.add(pinnedBitboard);
                ChessPiece pinnedPiece = BoardFunctions.getPieceByBitboard(boardState.getBitboards(), pinnedBitboard);
                assert pinnedPiece != null;

                long pinnedLine = MoveUtil.getMoveLine(boardState.getBitboards(), pinnedBitboard, offset, boardState.getActiveColour()) | MoveUtil.getMoveLine(boardState.getBitboards(), pinnedBitboard, -offset, boardState.getActiveColour());

                moves.addAll(generateMovesForPiece(boardState, pinnedBitboard, pinnedPiece, pushMask & pinnedLine, captureMask & pinnedLine));
            }

        }

        //calculate moves for non pinned pieces, except the king
        for (int i = 0; i < ChessPieceConstants.KING; i++){
            ChessPiece piece = friendlyPieces.get(i);
            long localPushMask = pushMask;
            long localCaptureMask = captureMask;
            BitboardUtil.forEachOneBit(boardState.getBitboards()[piece.getKey()], bitIndex -> {
                long pieceBitboard = 1L << bitIndex;
                if (!pinnedPieces.contains(pieceBitboard)) {
                    moves.addAll(generateMovesForPiece(boardState, pieceBitboard, piece, localPushMask, localCaptureMask));
                }
            });
        }

        return moves;
    }

    private static List<Move> generateMovesForPiece(BoardState boardState, long pieceBitboard, ChessPiece piece, long pushMask, long captureMask) {
        List<Move> moves = new ArrayList<>();
        int originOffset = Bitwise.findIndexOfMS1B(pieceBitboard);

        if (piece.isPawn()){
            long legalPushes = MoveUtil.piecePushes(boardState.getBitboards(), piece, pieceBitboard) & pushMask;
            long legalCaptures = MoveUtil.pieceCaptures(boardState.getBitboards(), piece, pieceBitboard) & captureMask & BoardFunctions.calculateOccupiedByColour(boardState.getBitboards(), boardState.getPassiveColour());
            long epCapture = 0L;

            //epCapture only allowed if the new pawn position is in the push mask and the captured pawn is in the capture mask
            if (boardState.getEnPassantTarget() != -1
                && ((1L << (boardState.getEnPassantTarget() + (boardState.getActiveColour() == ChessConstants.WHITE ? ChessBoardConstants.SOUTH : ChessBoardConstants.NORTH))) & captureMask) != 0) {
                epCapture = (1L << boardState.getEnPassantTarget()) & pushMask;
            }

            BitboardUtil.forEachOneBit(legalPushes, bitIndex -> {
                int flag = 0;
                boolean promotion = bitIndex < 8 || bitIndex >= 56;

                if (promotion){
                    flag |= 8;
                    for (int i = 0; i < 4; i++){
                        moves.add(new Move(piece.getKey(), originOffset, bitIndex, flag | i));
                    }
                }
                else {
                    if (Math.abs(originOffset - bitIndex) != ChessBoardConstants.NORTH){
                        flag = 1;
                    }
                    moves.add(new Move(piece.getKey(), originOffset, bitIndex, flag));
                }
            });

            BitboardUtil.forEachOneBit(legalCaptures, bitIndex -> {
                int flag = 4;
                boolean promotion = bitIndex < 8 || bitIndex >= 56;

                if (promotion){
                    flag |= 8;
                    for (int i = 0; i < 4; i++){
                        moves.add(new Move(piece.getKey(), originOffset, bitIndex, flag | i));
                    }
                }
                else {
                    moves.add(new Move(piece.getKey(), originOffset, bitIndex, flag));
                }
            });

            if (epCapture != 0){
                int epCaptureIndex = Bitwise.findIndexOfMS1B(epCapture);
                //check for discovered check
                int colourOffset = boardState.getActiveColour() == ChessConstants.WHITE ? 0: 6;
                int pawnEpOffset = boardState.getActiveColour() == ChessConstants.WHITE ? ChessBoardConstants.SOUTH : ChessBoardConstants.NORTH;

                //first we must remove the captured and capturing pawn
                boardState.getBitboards()[ChessPieceConstants.PAWN + colourOffset] &= ~pieceBitboard;
                boardState.getBitboards()[ChessPieceConstants.PAWN + 6 - colourOffset] &= ~(1L << (epCaptureIndex + pawnEpOffset));

                //then calculate the new king danger squares
                long kingDangerSquares = MoveUtil.getKingDangerSquares(boardState.getBitboards(), boardState.getActiveColour());

                //then return the pieces
                boardState.getBitboards()[ChessPieceConstants.PAWN + colourOffset] |= pieceBitboard;
                boardState.getBitboards()[ChessPieceConstants.PAWN + 6 - colourOffset] |= (1L << (epCaptureIndex + pawnEpOffset));

                //if there is no discovered check, en passant move is legal
                if ((kingDangerSquares & boardState.getBitboards()[ChessPieceConstants.KING + (boardState.getActiveColour() == ChessConstants.WHITE ? 0: 6)]) == 0){
                    moves.add(new Move(piece.getKey(), originOffset, epCaptureIndex, 5));
                }
            }
        }
        else {
            long legalPieceMoves = MoveUtil.pieceMoves(boardState.getBitboards(), piece, pieceBitboard) & (pushMask | captureMask);
            long enemyPieces = BoardFunctions.calculateOccupiedByColour(boardState.getBitboards(), !piece.getColour());

            BitboardUtil.forEachOneBit(legalPieceMoves, bitIndex -> {
                int flag = 0;
                //if move is a capture
                if ((enemyPieces & (1L << bitIndex)) > 0){
                    flag = 4;
                }
                moves.add(new Move(piece.getKey(), originOffset, bitIndex, flag));
            });
        }

        return moves;
    }

}
