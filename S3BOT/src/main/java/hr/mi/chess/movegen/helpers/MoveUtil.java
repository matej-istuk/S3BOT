package hr.mi.chess.movegen.helpers;

import hr.mi.chess.models.ChessPiece;
import hr.mi.chess.util.BoardFunctions;
import hr.mi.chess.movegen.helpers.implementations.MoveOffsetGetters;
import hr.mi.chess.constants.ChessBoardConstants;
import hr.mi.chess.constants.ChessConstants;
import hr.mi.chess.constants.ChessPieceConstants;

import java.util.List;
import java.util.function.LongUnaryOperator;

/**
 * Utility class containing various methods for extracting bitboards of possible moves for a given chess boardstate.
 * <p>
 *     Necessary terminology for understanding this "library"
 *     <ul>
 *         <li>
 *             hard blocker - a piece is a hard blocker if the moved piece cannot move on it's square or past it
 *         </li>
 *         <li>
 *             soft blocker - a piece is a soft blocker if the moved piece can move on it's square but cannot move past
 *             it
 *         </li>
 *         <li>
 *             offset - consult LERF compass rose
 *             <a href="https://www.chessprogramming.org/Square_Mapping_Considerations#Little-Endian_File-Rank_Mapping">here</a>
 *         </li>
 *     </ul>
 * </p>
 * @author Matej Istuk
 */
public class MoveUtil {

    /**
     * Gets a bitmask of the possible moves in a line from an origin square.
     * @param bitboards bitboard representation of a chessboard
     * @param moveOrigin index of the tile from where the move is starting
     * @param offset offset of the move
     * @param hardBlockerColour which colour of pieces represents hard blockers (for example white are hard blockers for
     *                          white piecese)
     * @return a bitmask of possible squares where a sliding piece could move
     */
    public static long getMoveLine(long[] bitboards, long moveOrigin, int offset, boolean hardBlockerColour){
        return generateMoveByOffset(offset, moveOrigin, BoardFunctions.calculateOccupiedByColour(bitboards, hardBlockerColour), BoardFunctions.calculateOccupiedByColour(bitboards, !hardBlockerColour), true);
    }

    /**
     * Returns squares that the king cannot move to for the received bitboards.
     * @param bitboards bitboard representation of a chessboard
     * @param activeColour determines the colour of the king
     * @return danger square bitboard
     */
    public static long getKingDangerSquares(long[] bitboards, boolean activeColour){
        if (activeColour == ChessConstants.WHITE){
            return getKingDangerSquare(bitboards, ChessPieceConstants.BLACK_PIECES, ChessPiece.WHITE_KING);
        }
        else {
            return getKingDangerSquare(bitboards, ChessPieceConstants.WHITE_PIECES, ChessPiece.BLACK_KING);
        }
    }

    /**
     * Returns king danger squares by specific attackers on the specific king
     * @param bitboards bitboard representation of a chessboard
     * @param attackers for which to generate danger squares
     * @param king king for which the squares are calculated
     * @return danger square bitboard
     */
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

    /**
     * Creates a bitboard of legal pushes for a piece (not type) whose location is described by the pieceBitboard.
     * @param bitboards bitboard representation of a chessboard
     * @param piece which type of piece is being moved
     * @param pieceBitboard the bitboard of the specific piece
     * @return bitboard representation of legal piece pushes
     */
    public static long piecePushes(long[] bitboards, ChessPiece piece, long pieceBitboard){
        return getMoves(bitboards, piece, pieceBitboard, BoardFunctions::calculateOccupiedAll, o -> 0, MoveOffsetGetters.PUSH_GETTER);
    }

    /**
     * Creates a bitboard of legal captures for a piece (not type) whose location is described by the pieceBitboard.
     * @param bitboards bitboard representation of a chessboard
     * @param piece which type of piece is being moved
     * @param pieceBitboard the bitboard of the specific piece
     * @return bitboard representation of legal piece captures
     */
    public static long pieceCaptures(long[] bitboards, ChessPiece piece, long pieceBitboard){
        boolean friendlyColour = piece.getColour();
        return getMoves(bitboards, piece, pieceBitboard, o -> BoardFunctions.calculateOccupiedByColour(o, friendlyColour), o -> BoardFunctions.calculateOccupiedByColour(o, !friendlyColour), MoveOffsetGetters.CAPTURE_GETTER);
    }

    /**
     * Creates a bitboard of legal pushes and captures for a piece (not type) whose location is described by the
     * pieceBitboard. (includes all squares where a capture could theoretically be)
     * @param bitboards bitboard representation of a chessboard
     * @param piece which type of piece is being moved
     * @param pieceBitboard the bitboard of the specific piece
     * @return bitboard representation of legal piece captures and pushes
     */
    public static long pieceMoves(long[] bitboards, ChessPiece piece, long pieceBitboard){
        if (piece.isPawn()){
            return piecePushes(bitboards, piece, pieceBitboard) | pieceCaptures(bitboards, piece, pieceBitboard);
        }
        return pieceCaptures(bitboards, piece, pieceBitboard);
    }

    /**
     * Creates a bitboard of legal pushes for a piece type.
     * @param bitboards bitboard representation of a chessboard
     * @param piece which type of piece is being moved
     * @return bitboard representation of legal piece type pushes
     */
    public static long typePushes(long[] bitboards, ChessPiece piece){
        boolean friendlyColour = piece.getColour();
        return typePushes(bitboards, piece, BoardFunctions::calculateOccupiedAll, o -> 0);
    }

    /**
     * Creates a bitboard of legal captures for a piece type (includes all squares where a capture could theoretically
     * be).
     * @param bitboards bitboard representation of a chessboard
     * @param piece which type of piece is being moved
     * @return bitboard representation of legal piece type captures
     */
    public static long typeCaptures(long[] bitboards, ChessPiece piece){
        boolean friendlyColour = piece.getColour();
        return typeCaptures(bitboards, piece, o -> BoardFunctions.calculateOccupiedByColour(o, friendlyColour), o -> BoardFunctions.calculateOccupiedByColour(o, !friendlyColour));
    }

    /**
     * Creates a bitboard of legal pushes and captures for a piece type.
     * @param bitboards bitboard representation of a chessboard
     * @param piece which type of piece is being moved
     * @return bitboard representation of legal piece type captures and pushes
     */
    public static long typeMoves(long[] bitboards, ChessPiece piece){
        if (piece.isPawn()){
            return typePushes(bitboards, piece) | typeCaptures(bitboards, piece);
        }
        return typePushes(bitboards, piece);
    }


    /**
     * Creates a bitboard of legal pushes for a piece type.
     * @param bitboards bitboard representation of a chessboard
     * @param piece which type of piece is being moved
     * @param hardBlockerCalcFunc function which calculates hard blockers
     * @param softBlockerCalcFunc function which calculates soft blockers
     * @return bitboard representation of legal piece type pushes
     */
    private static long typePushes(long[] bitboards, ChessPiece piece, BlockerCalcFunc hardBlockerCalcFunc, BlockerCalcFunc softBlockerCalcFunc){
        return getMoves(bitboards, piece, bitboards[piece.getKey()], hardBlockerCalcFunc, softBlockerCalcFunc, MoveOffsetGetters.PUSH_GETTER);
    }

    /**
     * Creates a bitboard of legal captures for a piece type.
     * @param bitboards bitboard representation of a chessboard
     * @param piece which type of piece is being moved
     * @param hardBlockerCalcFunc function which calculates hard blockers
     * @param softBlockerCalcFunc function which calculates soft blockers
     * @return bitboard representation of legal piece type captures
     */
    private static long typeCaptures(long[] bitboards, ChessPiece piece, BlockerCalcFunc hardBlockerCalcFunc, BlockerCalcFunc softBlockerCalcFunc){
        return getMoves(bitboards, piece, bitboards[piece.getKey()], hardBlockerCalcFunc, softBlockerCalcFunc, MoveOffsetGetters.CAPTURE_GETTER);
    }

    /**
     * Generates moves as requested by the method parameters.
     * @param bitboards board bitboards
     * @param piece for which piece are moves being generated
     * @param piecesToMove bitboard of all pieces for which possible moves are being calculated
     * @param hardBlockerCalcFunc function which calculates hard blockers
     * @param softBlockerCalcFunc function which calculates soft blockers
     * @param moveOffsetGetter function which gets movement offsets for the specific piece
     * @return bitboard of all legal move squares
     */
    private static long getMoves(long[] bitboards, ChessPiece piece, long piecesToMove, BlockerCalcFunc hardBlockerCalcFunc, BlockerCalcFunc softBlockerCalcFunc, MoveOffsetGetter moveOffsetGetter) {
        long result = 0L;

        long hardBlockers = hardBlockerCalcFunc.calculateBlockers(bitboards);
        long softBlockers = softBlockerCalcFunc.calculateBlockers(bitboards);

        for (int offset : moveOffsetGetter.getMoveOffset(piece)) {
            result |= generateMoveByOffset(offset, piecesToMove, hardBlockers, softBlockers, piece.isSliding());
        }

        //generate double push for pawns
        if (piece.isPawn() && moveOffsetGetter.getMoveOffset(piece).length == 1){
            long pawnStartingRowMask = piece.getColour() == ChessConstants.WHITE ? ChessBoardConstants.RANK_2 : ChessBoardConstants.RANK_7;
            LongUnaryOperator shift = piece.getColour() == ChessConstants.WHITE ? o -> o << ChessBoardConstants.NORTH : o -> o >>> ChessBoardConstants.NORTH;
            while (piecesToMove != 0){
                long singleAttackingPiece = Long.lowestOneBit(piecesToMove);
                piecesToMove &= ~singleAttackingPiece;

                if ((singleAttackingPiece & pawnStartingRowMask) != 0 && (shift.applyAsLong(singleAttackingPiece) & result) != 0){
                    result |= shift.applyAsLong(shift.applyAsLong(singleAttackingPiece));
                    result &= ~hardBlockers;
                }
            }
        }

        return result;
    }

    /**
     * Function which directly calculates legal movements with various bitmasks
     * @param moveOffset offset of the movement, in which direction to scan forward
     * @param piecesToMove bitboard of pieces to be moved
     * @param hardBlockers bitboard of hard blockers
     * @param softBlockers bitboard of soft blockers
     * @param isSliding is the move sliding (can it move any amount of squares in the direction of the offset)
     * @return bitboard of possible moves
     */
    private static long generateMoveByOffset(int moveOffset, long piecesToMove, long hardBlockers, long softBlockers, boolean isSliding){
        //applying necessary mask so that nothing overflows, can be replaced by a map, but performance may suffer
        switch (moveOffset){
            case ChessBoardConstants.NORTH_EAST, ChessBoardConstants.EAST, ChessBoardConstants.SOUTH_EAST -> {
                softBlockers |= ChessBoardConstants.FILE_H;
                softBlockers &= ~hardBlockers;
                piecesToMove &= ~ChessBoardConstants.FILE_H;
            }
            case ChessBoardConstants.NORTH_WEST, ChessBoardConstants.WEST, ChessBoardConstants.SOUTH_WEST -> {
                softBlockers |= ChessBoardConstants.FILE_A;
                softBlockers &= ~hardBlockers;
                piecesToMove &= ~ChessBoardConstants.FILE_A;
            }
            case ChessBoardConstants.KNIGHT_NORTH_WEST -> piecesToMove = piecesToMove & ~ChessBoardConstants.NO_MOVE_KNIGHT_NORTH_WEST;
            case ChessBoardConstants.KNIGHT_NORTH_EAST -> piecesToMove = piecesToMove & ~ChessBoardConstants.NO_MOVE_KNIGHT_NORTH_EAST;
            case ChessBoardConstants.KNIGHT_EAST_NORTH -> piecesToMove = piecesToMove & ~ChessBoardConstants.NO_MOVE_KNIGHT_EAST_NORTH;
            case ChessBoardConstants.KNIGHT_EAST_SOUTH -> piecesToMove = piecesToMove & ~ChessBoardConstants.NO_MOVE_KNIGHT_EAST_SOUTH;
            case ChessBoardConstants.KNIGHT_SOUTH_EAST -> piecesToMove = piecesToMove & ~ChessBoardConstants.NO_MOVE_KNIGHT_SOUTH_EAST;
            case ChessBoardConstants.KNIGHT_SOUTH_WEST -> piecesToMove = piecesToMove & ~ChessBoardConstants.NO_MOVE_KNIGHT_SOUTH_WEST;
            case ChessBoardConstants.KNIGHT_WEST_SOUTH -> piecesToMove = piecesToMove & ~ChessBoardConstants.NO_MOVE_KNIGHT_WEST_SOUTH;
            case ChessBoardConstants.KNIGHT_WEST_NORTH -> piecesToMove = piecesToMove & ~ChessBoardConstants.NO_MOVE_KNIGHT_WEST_NORTH;
        }

        //maybe optimise as two separate functions
        LongUnaryOperator shift = moveOffset > 0 ? o -> o << moveOffset : o -> o >>> -moveOffset;

        long hitSoftBlockers = 0L;
        long attackedSquares = shift.applyAsLong(piecesToMove);
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