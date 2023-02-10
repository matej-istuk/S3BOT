package hr.mi.chess.movegen.helpers.interfaces;

import hr.mi.chess.models.ChessPiece;

public interface MoveOffsetGetter {
    int[] getMoveOffset (ChessPiece piece);
}
