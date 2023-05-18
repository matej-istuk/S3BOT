package hr.mi.chess.movegen.helpers;

import hr.mi.chess.models.ChessPiece;

/**
 * Interface modelling a strategy which returns offsets by which a piece can move.
 * @author Matej Istuk
 */
public interface MoveOffsetGetter {
    /**
     * Returns offsets by which the requested piece can move.
     * @param piece the requested piece
     * @return array of offsets
     */
    int[] getMoveOffset (ChessPiece piece);
}
