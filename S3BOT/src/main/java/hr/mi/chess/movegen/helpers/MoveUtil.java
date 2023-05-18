package hr.mi.chess.movegen.helpers;

import hr.mi.chess.models.ChessPiece;
import hr.mi.chess.util.BoardFunctions;
import hr.mi.chess.movegen.helpers.implementations.MoveOffsetGetters;
import hr.mi.chess.constants.ChessBoardConstants;
import hr.mi.chess.constants.ChessConstants;
import hr.mi.chess.constants.ChessPieceConstants;

import java.util.List;
import java.util.function.LongUnaryOperator;

public class MoveUtil {

    public static long getMoveLine(long[] bitboards, long moveOrigin, int offset, boolean hardBlockerColour){
        return generateMoveByOffset(offset, moveOrigin, BoardFunctions.calculateOccupiedByColour(bitboards, hardBlockerColour), BoardFunctions.calculateOccupiedByColour(bitboards, !hardBlockerColour), true);
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
            result |= generateMoveByOffset(offset, attackingPieces, hardBlockers, softBlockers, piece.isSliding());
        }

        //generate double push for pawns
        if (piece.isPawn() && moveOffsetGetter.getMoveOffset(piece).length == 1){
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

    public static long generateMoveByOffset(int attackOffset, long attackingPieces, long hardBlockers, long softBlockers, boolean isSliding){
        //applying necessary mask so that nothing overflows, can be replaced by a map, but performance may suffer
        switch (attackOffset){
            case ChessBoardConstants.NORTH_EAST, ChessBoardConstants.EAST, ChessBoardConstants.SOUTH_EAST -> {
                softBlockers |= ChessBoardConstants.FILE_H;
                softBlockers &= ~hardBlockers;
                attackingPieces &= ~ChessBoardConstants.FILE_H;
            }
            case ChessBoardConstants.NORTH_WEST, ChessBoardConstants.WEST, ChessBoardConstants.SOUTH_WEST -> {
                softBlockers |= ChessBoardConstants.FILE_A;
                softBlockers &= ~hardBlockers;
                attackingPieces &= ~ChessBoardConstants.FILE_A;
            }
            case ChessBoardConstants.KNIGHT_NORTH_WEST -> attackingPieces = attackingPieces & ~ChessBoardConstants.NO_MOVE_KNIGHT_NORTH_WEST;
            case ChessBoardConstants.KNIGHT_NORTH_EAST -> attackingPieces = attackingPieces & ~ChessBoardConstants.NO_MOVE_KNIGHT_NORTH_EAST;
            case ChessBoardConstants.KNIGHT_EAST_NORTH -> attackingPieces = attackingPieces & ~ChessBoardConstants.NO_MOVE_KNIGHT_EAST_NORTH;
            case ChessBoardConstants.KNIGHT_EAST_SOUTH -> attackingPieces = attackingPieces & ~ChessBoardConstants.NO_MOVE_KNIGHT_EAST_SOUTH;
            case ChessBoardConstants.KNIGHT_SOUTH_EAST -> attackingPieces = attackingPieces & ~ChessBoardConstants.NO_MOVE_KNIGHT_SOUTH_EAST;
            case ChessBoardConstants.KNIGHT_SOUTH_WEST -> attackingPieces = attackingPieces & ~ChessBoardConstants.NO_MOVE_KNIGHT_SOUTH_WEST;
            case ChessBoardConstants.KNIGHT_WEST_SOUTH -> attackingPieces = attackingPieces & ~ChessBoardConstants.NO_MOVE_KNIGHT_WEST_SOUTH;
            case ChessBoardConstants.KNIGHT_WEST_NORTH -> attackingPieces = attackingPieces & ~ChessBoardConstants.NO_MOVE_KNIGHT_WEST_NORTH;
        }

        //maybe optimise as two separate functions
        LongUnaryOperator shift = attackOffset > 0 ? o -> o << attackOffset : o -> o >>> -attackOffset;

        long hitSoftBlockers = 0L;
        long attackedSquares = shift.applyAsLong(attackingPieces);
        long attackedSquaresOld = 0L;

        //if soft blockers were hit generate new hard blockers and mark hit soft blockers
        hitSoftBlockers |= attackedSquares & softBlockers;
        hardBlockers |= hitSoftBlockers;
        //remove attack on hard blockers
        attackedSquares &= ~(hardBlockers);

        //continue for sliding pieces
        while (isSliding && attackedSquaresOld != attackedSquares){
            attackedSquaresOld = attackedSquares;
            //move attacked squares in sliding direction
            attackedSquares |= shift.applyAsLong(attackedSquares);
            //if soft blockers were hit generate new hard blockers and mark hit soft blockers
            hitSoftBlockers |= attackedSquares & softBlockers;
            hardBlockers |= hitSoftBlockers;
            //remove attack on hard blockers
            attackedSquares &= ~(hardBlockers);
        }

        return attackedSquares | hitSoftBlockers;
    }
}