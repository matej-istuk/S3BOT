package hr.mi.chess.movegen.helpers;

import hr.mi.chess.constants.ChessBoardConstants;

import java.util.function.LongUnaryOperator;

public class MoveCalc {
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
