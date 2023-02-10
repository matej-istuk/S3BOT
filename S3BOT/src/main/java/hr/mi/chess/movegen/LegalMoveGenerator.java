package hr.mi.chess.movegen;

import hr.mi.chess.models.BoardState;
import hr.mi.chess.models.ChessPiece;
import hr.mi.chess.models.Move;
import hr.mi.chess.movegen.helpers.Bitwise;
import hr.mi.chess.movegen.helpers.PieceMoveGenerator;
import hr.mi.chess.util.constants.ChessPieceConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntToLongFunction;

public class LegalMoveGenerator implements MoveGenerator{

    public static List<Move> generateMoves(BoardState boardState) {
        long pushMask = 0xFFFFFFFFFFFFFFFFL;
        long captureMask = 0xFFFFFFFFFFFFFFFFL;
        List<ChessPiece> pieces = boardState.isWhiteActive() ? ChessPieceConstants.WHITE_PIECES : ChessPieceConstants.BLACK_PIECES;

        List<Move> moves = new ArrayList<>();

        //generate king moves
        long kingDangerSquares = PieceMoveGenerator.generateActivePlayerKingDangerSquares(boardState);
        moves.addAll(generateMovesForPieceType(boardState, pieces.get(ChessPieceConstants.KING), pushMask & ~kingDangerSquares, captureMask & ~kingDangerSquares));

        //check for check
        return moves;
    }

    private static List<Move> generateMovesForPieceType(BoardState boardState, ChessPiece piece, long pushMask, long captureMask){
        List<Move> moves = new ArrayList<>();
        long pieceBitboard = boardState.getBitboards()[piece.getKey()];

        IntToLongFunction pushGenFunc = o -> PieceMoveGenerator.generatePushesForSinglePiece(boardState, piece, 1L << o) & ~BoardFunctions.calculateNotActiveColourOccupied(boardState) & pushMask;
        IntToLongFunction captureGenFunc = o -> PieceMoveGenerator.generateCapturesSingleForPiece(boardState, piece, 1L << o ) & BoardFunctions.calculateNotActiveColourOccupied(boardState) & captureMask;

        while (pieceBitboard != 0){
            int singlePieceIndex = Bitwise.findMS1B(pieceBitboard);
            pieceBitboard &= ~(1L << singlePieceIndex);
            long possiblePushes = pushGenFunc.applyAsLong(singlePieceIndex);
            long possibleCaptures = captureGenFunc.applyAsLong(singlePieceIndex);

            while (possiblePushes != 0){
                int singlePushIndex = Bitwise.findMS1B(possiblePushes);
                possiblePushes &= ~(1L << singlePushIndex);

                moves.add(new Move(piece.getKey(), singlePieceIndex, singlePushIndex, 0));
            }

            while (possibleCaptures != 0){
                int singleCaptureIndex = Bitwise.findMS1B(possibleCaptures);
                possibleCaptures &= ~(1L << singleCaptureIndex);

                moves.add(new Move(piece.getKey(), singlePieceIndex, singleCaptureIndex, 4));
            }
        }

        return moves;
    }

}
