package hr.mi.chess.movegen.helpers;

import hr.mi.chess.models.BoardState;
import hr.mi.chess.models.ChessPiece;
import hr.mi.chess.util.ChessConstants;

import java.util.function.LongUnaryOperator;

public class AttackedSquareGenerator {
    private static long calculateAttackedByPieceType(BoardState boardState, int piece){
        return 0;
    }

    public static long generateAttackBySlidingPiece(BoardState boardState, ChessPiece piece){
        long result = 0L;

        long attackingPieces = boardState.getBitboards()[piece.getKey()];
        long hardBlockers;
        long softBlockers;

        if (piece.getKey() <= ChessConstants.WHITE_KING) {
            hardBlockers = calculateWhiteOccupied(boardState);
            softBlockers = calculateBlackOccupied(boardState);
        } else {
            hardBlockers = calculateBlackOccupied(boardState);
            softBlockers = calculateWhiteOccupied(boardState);
        }

        for (int attackOffset: piece.getAttackOffsets()){
            if (piece.equals(ChessPiece.WHITE_KNIGHT) || piece.equals(ChessPiece.BLACK_KNIGHT))
                result |= generateJumpingAttack(attackOffset, attackingPieces, hardBlockers, softBlockers, piece.isSliding());
            else
                result |= generateDirectionalAttack(attackOffset, attackingPieces, hardBlockers, softBlockers, piece.isSliding());
        }

        return result;
    }

    private static long generateDirectionalAttack(int attackOffset, long attackingPieces, long hardBlockers, long softBlockers, boolean isSliding){
        //correction for any move in the east direction
        if (attackOffset == ChessConstants.NORTH_EAST || attackOffset == ChessConstants.EAST || attackOffset == ChessConstants.SOUTH_EAST){
            softBlockers |= ChessConstants.FILE_H;
            softBlockers &= ~hardBlockers;
            attackingPieces &= ~ChessConstants.FILE_H;
        }
        //correction for any move in the west direction
        else if (attackOffset == ChessConstants.NORTH_WEST || attackOffset == ChessConstants.WEST || attackOffset == ChessConstants.SOUTH_WEST){
            softBlockers |= ChessConstants.FILE_A;
            softBlockers &= ~hardBlockers;
            attackingPieces &= ~ChessConstants.FILE_A;
        }

        return generateAttack(attackOffset, attackingPieces, hardBlockers, softBlockers, isSliding);
    }

    private static long generateJumpingAttack(int attackOffset, long attackingPieces, long hardBlockers, long softBlockers, boolean isSliding){
        //To be replaced with a map
        switch (attackOffset){
            case ChessConstants.KNIGHT_NORTH_WEST -> attackingPieces = attackingPieces & ~ChessConstants.NO_MOVE_KNIGHT_NORTH_WEST;
            case ChessConstants.KNIGHT_NORTH_EAST -> attackingPieces = attackingPieces & ~ChessConstants.NO_MOVE_KNIGHT_NORTH_EAST;
            case ChessConstants.KNIGHT_EAST_NORTH -> attackingPieces = attackingPieces & ~ChessConstants.NO_MOVE_KNIGHT_EAST_NORTH;
            case ChessConstants.KNIGHT_EAST_SOUTH -> attackingPieces = attackingPieces & ~ChessConstants.NO_MOVE_KNIGHT_EAST_SOUTH;
            case ChessConstants.KNIGHT_SOUTH_EAST -> attackingPieces = attackingPieces & ~ChessConstants.NO_MOVE_KNIGHT_SOUTH_EAST;
            case ChessConstants.KNIGHT_SOUTH_WEST -> attackingPieces = attackingPieces & ~ChessConstants.NO_MOVE_KNIGHT_SOUTH_WEST;
            case ChessConstants.KNIGHT_WEST_SOUTH -> attackingPieces = attackingPieces & ~ChessConstants.NO_MOVE_KNIGHT_WEST_SOUTH;
            case ChessConstants.KNIGHT_WEST_NORTH -> attackingPieces = attackingPieces & ~ChessConstants.NO_MOVE_KNIGHT_WEST_NORTH;
        }

        return generateAttack(attackOffset, attackingPieces, hardBlockers, softBlockers, isSliding);
    }

    private static long generateAttack(int attackOffset, long attackingPieces, long hardBlockers, long softBlockers, boolean isSliding){
        //maybe optimise as two separate functions
        LongUnaryOperator shift = attackOffset > 0 ? o -> o << attackOffset : o -> o >>> -attackOffset;

        long hitSoftBlockers = 0L;
        long attackedSquares = attackingPieces;
        long attackedSquaresOld = 0L;

        do {
            //the part after the & might save time
            attackedSquaresOld = attackedSquares & ~(hardBlockers);
            //move attacked squares in sliding direction
            attackedSquares |= shift.applyAsLong(attackedSquares);
            //if soft blockers were hit generate new hard blockers and mark hit soft blockers
            hitSoftBlockers |= attackedSquares & softBlockers;
            hardBlockers |= hitSoftBlockers;
            //remove attack on hard blockers
            attackedSquares &= ~(hardBlockers);
        } while (isSliding && attackedSquaresOld != attackedSquares);

        return attackedSquares | hitSoftBlockers;
    }

    private static long calculateWhiteOccupied(BoardState boardState){
        return calculateOccupiedByIndexRange(boardState, 0, 6);
    }

    private static long calculateBlackOccupied(BoardState boardState){
        return calculateOccupiedByIndexRange(boardState, 6, 12);
    }

    private static long calculateOccupiedByIndexRange(BoardState boardState, int from, int to){
        long occupied = 0L;
        for (int i = from; i < to; i++){
            occupied |= boardState.getBitboards()[i];
        }
        return occupied;
    }

}
