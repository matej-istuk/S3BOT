package hr.mi.chess.movegen.helpers;

import hr.mi.chess.models.BoardState;
import hr.mi.chess.models.ChessPiece;
import hr.mi.chess.movegen.BoardFunctions;
import hr.mi.chess.movegen.helpers.implementations.MoveOffsetGetters;
import hr.mi.chess.movegen.helpers.interfaces.BlockerCalcFunc;
import hr.mi.chess.movegen.helpers.interfaces.MoveOffsetGetter;
import hr.mi.chess.util.constants.ChessBoardConstants;
import hr.mi.chess.util.constants.ChessConstants;
import hr.mi.chess.util.constants.ChessPieceConstants;

import java.util.List;
import java.util.function.LongUnaryOperator;

public class MoveUtil {

    public static long getMoveLine(long[] bitboards, long moveOrigin, int offset, boolean hardBlockerColour){
        return MoveCalc.generateMoveByOffset(offset, moveOrigin, BoardFunctions.calculateOccupiedByColour(bitboards, hardBlockerColour), BoardFunctions.calculateOccupiedByColour(bitboards, !hardBlockerColour), true);
    }

    public static long getKingDangerSquares(long[] bitboards, boolean activeColour){
        if (activeColour == ChessConstants.WHITE){
            return getKingDangerSquare(bitboards, ChessPieceConstants.BLACK_PIECES, ChessPiece.WHITE_KING);
        }
        else {
            return getKingDangerSquare(bitboards, ChessPieceConstants.WHITE_PIECES, ChessPiece.BLACK_KING);
        }
    }

    private static long getKingDangerSquare(long[] bitboards, List<ChessPiece> attackers, ChessPiece king){
        //we have to remove the king from the board to calculate his danger squares
        long kingBitboard = bitboards[king.getKey()];
        bitboards[king.getKey()] = 0L;

        long kingDangerSquares = 0L;
        BlockerCalcFunc dangerSquareHardBlockers = o -> 0L;
        BlockerCalcFunc dangerSquareSoftBlockers = o -> BoardFunctions.calculateOccupiedByColour(o, ChessConstants.WHITE) | BoardFunctions.calculateOccupiedByColour(o, ChessConstants.BLACK);
        for (ChessPiece attacker: attackers){
            if (bitboards[attacker.getKey()] != 0L)
                kingDangerSquares |= typeCaptures(bitboards, attacker, dangerSquareHardBlockers, dangerSquareSoftBlockers);
        }

        //returning the king to the bitboards
        bitboards[king.getKey()] = kingBitboard;

        return kingDangerSquares;
    }

    public static long piecePushes(long[] bitboards, ChessPiece piece, long pieceBitboard){
        boolean friendlyColour = piece.getColour();
        return getMoves(bitboards, piece, pieceBitboard, BoardFunctions::calculateOccupiedAll, o -> 0, MoveOffsetGetters.PUSH_GETTER);
    }

    public static long pieceCaptures(long[] bitboards, ChessPiece piece, long pieceBitboard){
        boolean friendlyColour = piece.getColour();
        return getMoves(bitboards, piece, pieceBitboard, o -> BoardFunctions.calculateOccupiedByColour(o, friendlyColour), o -> BoardFunctions.calculateOccupiedByColour(o, !friendlyColour), MoveOffsetGetters.CAPTURE_GETTER);
    }

    public static long pieceMoves(long[] bitboards, ChessPiece piece, long pieceBitboard){
        if (piece.isPawn()){
            return piecePushes(bitboards, piece, pieceBitboard) | pieceCaptures(bitboards, piece, pieceBitboard);
        }
        return pieceCaptures(bitboards, piece, pieceBitboard);
    }

    public static long typePushes(long[] bitboards, ChessPiece piece){
        boolean friendlyColour = piece.getColour();
        return typePushes(bitboards, piece, BoardFunctions::calculateOccupiedAll, o -> 0);
    }

    public static long typeCaptures(long[] bitboards, ChessPiece piece){
        boolean friendlyColour = piece.getColour();
        return typeCaptures(bitboards, piece, o -> BoardFunctions.calculateOccupiedByColour(o, friendlyColour), o -> BoardFunctions.calculateOccupiedByColour(o, !friendlyColour));
    }

    public static long typeMoves(long[] bitboards, ChessPiece piece){
        if (piece.isPawn()){
            return typePushes(bitboards, piece) | typeCaptures(bitboards, piece);
        }
        return typePushes(bitboards, piece);
    }

    private static long typePushes(long[] bitboards, ChessPiece piece, BlockerCalcFunc hardBlockerCalcFunc, BlockerCalcFunc softBlockerCalcFunc){
        return getMoves(bitboards, piece, bitboards[piece.getKey()], hardBlockerCalcFunc, softBlockerCalcFunc, MoveOffsetGetters.PUSH_GETTER);
    }

    private static long typeCaptures(long[] bitboards, ChessPiece piece, BlockerCalcFunc hardBlockerCalcFunc, BlockerCalcFunc softBlockerCalcFunc){
        return getMoves(bitboards, piece, bitboards[piece.getKey()], hardBlockerCalcFunc, softBlockerCalcFunc, MoveOffsetGetters.CAPTURE_GETTER);
    }

    private static long getMoves(long[] bitboards, ChessPiece piece, long attackingPieces, BlockerCalcFunc hardBlockerCalcFunc, BlockerCalcFunc softBlockerCalcFunc, MoveOffsetGetter moveOffsetGetter) {
        long result = 0L;

        long hardBlockers = hardBlockerCalcFunc.calculateBlockers(bitboards);
        long softBlockers = softBlockerCalcFunc.calculateBlockers(bitboards);

        for (int offset : moveOffsetGetter.getMoveOffset(piece)) {
            result |= MoveCalc.generateMoveByOffset(offset, attackingPieces, hardBlockers, softBlockers, piece.isSliding());
        }

        //generate double push for pawns
        if (piece.isPawn()){
            long pawnStartingRowMask = piece.getColour() == ChessConstants.WHITE ? ChessBoardConstants.RANK_2 : ChessBoardConstants.RANK_7;
            LongUnaryOperator shift = piece.getColour() == ChessConstants.WHITE ? o -> o << ChessBoardConstants.NORTH : o -> o >>> ChessBoardConstants.NORTH;
            while (attackingPieces != 0){
                long singleAttackingPiece = Long.lowestOneBit(attackingPieces);
                attackingPieces &= ~singleAttackingPiece;

                if ((singleAttackingPiece & pawnStartingRowMask) != 0 && (shift.applyAsLong(singleAttackingPiece) & result) != 0){
                    result |= shift.applyAsLong(shift.applyAsLong(singleAttackingPiece));
                    result &= ~hardBlockers;
                }
            }
        }

        return result;
    }

}