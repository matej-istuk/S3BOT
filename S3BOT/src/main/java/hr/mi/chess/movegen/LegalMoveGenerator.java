package hr.mi.chess.movegen;

import hr.mi.chess.models.BoardState;
import hr.mi.chess.models.ChessPiece;
import hr.mi.chess.models.Move;
import hr.mi.chess.movegen.helpers.Bitwise;
import hr.mi.chess.movegen.helpers.MoveUtil;
import hr.mi.chess.util.constants.ChessPieceConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.IntToLongFunction;

public class LegalMoveGenerator implements MoveGenerator{

    public static List<Move> generateMoves(BoardState boardState) {
        long pushMask = 0xFFFFFFFFFFFFFFFFL;
        long captureMask = 0xFFFFFFFFFFFFFFFFL;
        List<ChessPiece> friendlyPieces = boardState.getActiveColour() ? ChessPieceConstants.WHITE_PIECES : ChessPieceConstants.BLACK_PIECES;
        List<ChessPiece> enemyPieces = boardState.getActiveColour() ? ChessPieceConstants.BLACK_PIECES : ChessPieceConstants.WHITE_PIECES;

        List<Move> moves = new ArrayList<>();

        //generate king moves
        long kingDangerSquares = MoveUtil.getKingDangerSquares(boardState.getBitboards(), boardState.getActiveColour());
        moves.addAll(generateMovesForPieceType(boardState, friendlyPieces.get(ChessPieceConstants.KING), pushMask & ~kingDangerSquares, captureMask & ~kingDangerSquares));

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
            long newCheckers = attackMask & BoardFunctions.calculateOccupiedByIndexes(boardState.getBitboards(), ChessPieceConstants.POSSIBLE_ATTACKERS_BY_OFFSET.get(offset), !boardState.getActiveColour());
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

        //calculate pinned pieces



        return moves;
    }

    private static List<Move> generateMovesForPieceType(BoardState boardState, ChessPiece piece, long pushMask, long captureMask){
        List<Move> moves = new ArrayList<>();
        long pieceBitboard = boardState.getBitboards()[piece.getKey()];

        IntToLongFunction pushGenFunc = o -> MoveUtil.piecePushes(boardState.getBitboards(), piece, 1L << o) & ~BoardFunctions.calculateOccupiedByColour(boardState.getBitboards(), !boardState.getActiveColour()) & pushMask;
        IntToLongFunction captureGenFunc = o -> MoveUtil.pieceCaptures(boardState.getBitboards(), piece, 1L << o ) & BoardFunctions.calculateOccupiedByColour(boardState.getBitboards(), !boardState.getActiveColour()) & captureMask;

        while (pieceBitboard != 0){
            int singlePieceIndex = Bitwise.findIndexOfMS1B(pieceBitboard);
            pieceBitboard &= ~(1L << singlePieceIndex);
            long possiblePushes = pushGenFunc.applyAsLong(singlePieceIndex);
            long possibleCaptures = captureGenFunc.applyAsLong(singlePieceIndex);

            while (possiblePushes != 0){
                int singlePushIndex = Bitwise.findIndexOfMS1B(possiblePushes);
                possiblePushes &= ~(1L << singlePushIndex);

                moves.add(new Move(piece.getKey(), singlePieceIndex, singlePushIndex, 0));
            }

            while (possibleCaptures != 0){
                int singleCaptureIndex = Bitwise.findIndexOfMS1B(possibleCaptures);
                possibleCaptures &= ~(1L << singleCaptureIndex);

                moves.add(new Move(piece.getKey(), singlePieceIndex, singleCaptureIndex, 4));
            }
        }

        return moves;
    }

}
