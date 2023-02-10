package hr.mi.chess.movegen.helpers;

import hr.mi.chess.models.BoardState;
import hr.mi.chess.models.ChessPiece;
import hr.mi.chess.movegen.BoardFunctions;
import hr.mi.chess.movegen.helpers.implementations.MoveOffsetGetters;
import hr.mi.chess.movegen.helpers.interfaces.BlockerCalcFunc;
import hr.mi.chess.movegen.helpers.interfaces.MoveOffsetGetter;
import hr.mi.chess.util.constants.ChessBoardConstants;
import hr.mi.chess.util.constants.ChessPieceConstants;

import java.util.List;
import java.util.function.LongUnaryOperator;

public class PieceMoveGenerator {

    public static long generateActivePlayerKingDangerSquares(BoardState boardState){
        if (boardState.isWhiteActive()){
            return generateKingDangerSquare(boardState, ChessPieceConstants.BLACK_PIECES, ChessPiece.WHITE_KING);
        }else {
            return generateKingDangerSquare(boardState, ChessPieceConstants.WHITE_PIECES, ChessPiece.BLACK_KING);
        }
    }

    private static long generateKingDangerSquare(BoardState boardState, List<ChessPiece> attackers, ChessPiece king){
        //we have to remove the king from the board to calculate his danger squares
        long kingBitboard = boardState.getBitboards()[king.getKey()];
        boardState.getBitboards()[king.getKey()] = 0L;

        long kingDangerSquares = 0L;
        BlockerCalcFunc dangerSquareHardBlockers = o -> 0L;
        BlockerCalcFunc dangerSquareSoftBlockers = o -> BoardFunctions.calculateWhiteOccupied(o) | BoardFunctions.calculateBlackOccupied(o);
        for (ChessPiece attacker: attackers){
            if (boardState.getBitboards()[attacker.getKey()] != 0L)
                kingDangerSquares |= generateCapturesForPieceType(boardState, attacker, dangerSquareHardBlockers, dangerSquareSoftBlockers);
        }

        //returning the king to the bitboards
        boardState.getBitboards()[king.getKey()] = kingBitboard;

        return kingDangerSquares;
    }

    public static long generatePushesForPieceType(BoardState boardState, ChessPiece piece){
        if (piece.getKey() <= ChessPiece.WHITE_KING.getKey()){
            return generatePushesForPieceType(boardState, piece, BoardFunctions::calculateWhiteOccupied, BoardFunctions::calculateBlackOccupied);
        }
        else {
            return generatePushesForPieceType(boardState, piece, BoardFunctions::calculateBlackOccupied, BoardFunctions::calculateWhiteOccupied);
        }
    }

    public static long generateCapturesForPieceType(BoardState boardState, ChessPiece piece){
        if (piece.getKey() <= ChessPiece.WHITE_KING.getKey()){
            return generateCapturesForPieceType(boardState, piece, BoardFunctions::calculateWhiteOccupied, BoardFunctions::calculateBlackOccupied);
        }
        else {
            return generateCapturesForPieceType(boardState, piece, BoardFunctions::calculateBlackOccupied, BoardFunctions::calculateWhiteOccupied);
        }
    }

    public static long generatePushesForSinglePiece(BoardState boardState, ChessPiece piece, long pieceBitboard){
        if (piece.getKey() <= ChessPiece.WHITE_KING.getKey()){
            return generateMoves(boardState, piece, pieceBitboard, BoardFunctions::calculateWhiteOccupied, BoardFunctions::calculateBlackOccupied, MoveOffsetGetters.PUSH_GETTER);
        }
        else {
            return generateMoves(boardState, piece, pieceBitboard, BoardFunctions::calculateBlackOccupied, BoardFunctions::calculateWhiteOccupied, MoveOffsetGetters.PUSH_GETTER);
        }
    }

    public static long generateCapturesSingleForPiece(BoardState boardState, ChessPiece piece, long pieceBitboard){
        if (piece.getKey() <= ChessPiece.WHITE_KING.getKey()){
            return generateMoves(boardState, piece, pieceBitboard, BoardFunctions::calculateWhiteOccupied, BoardFunctions::calculateBlackOccupied, MoveOffsetGetters.CAPTURE_GETTER);
        }
        else {
            return generateMoves(boardState, piece, pieceBitboard, BoardFunctions::calculateBlackOccupied, BoardFunctions::calculateWhiteOccupied, MoveOffsetGetters.CAPTURE_GETTER);
        }
    }

    private static long generatePushesForPieceType(BoardState boardState, ChessPiece piece, BlockerCalcFunc hardBlockerCalcFunc, BlockerCalcFunc softBlockerCalcFunc){
        return generateMoves(boardState, piece, boardState.getBitboards()[piece.getKey()], hardBlockerCalcFunc, softBlockerCalcFunc, MoveOffsetGetters.PUSH_GETTER);
    }

    private static long generateCapturesForPieceType(BoardState boardState, ChessPiece piece, BlockerCalcFunc hardBlockerCalcFunc, BlockerCalcFunc softBlockerCalcFunc){
        return generateMoves(boardState, piece, boardState.getBitboards()[piece.getKey()], hardBlockerCalcFunc, softBlockerCalcFunc, MoveOffsetGetters.CAPTURE_GETTER);
    }

    private static long generateMoves(BoardState boardState, ChessPiece piece, long attackingPieces, BlockerCalcFunc hardBlockerCalcFunc, BlockerCalcFunc softBlockerCalcFunc, MoveOffsetGetter moveOffsetGetter) {
        long result = 0L;

        long hardBlockers = hardBlockerCalcFunc.calculateBlockers(boardState);
        long softBlockers = softBlockerCalcFunc.calculateBlockers(boardState);

        for (int offset: moveOffsetGetter.getMoveOffset(piece)){
            result |= generateMoveByOffset(offset, attackingPieces, hardBlockers, softBlockers, piece.isSliding());
        }

        return result;
    }

    private static long generateMoveByOffset(int attackOffset, long attackingPieces, long hardBlockers, long softBlockers, boolean isSliding){
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