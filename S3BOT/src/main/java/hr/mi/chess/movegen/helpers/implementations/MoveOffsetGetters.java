package hr.mi.chess.movegen.helpers.implementations;

import hr.mi.chess.models.ChessPiece;
import hr.mi.chess.movegen.helpers.interfaces.MoveOffsetGetter;

public class MoveOffsetGetters {
    public static final MoveOffsetGetter PUSH_GETTER = ChessPiece::getPushOffsets;
    public static final MoveOffsetGetter CAPTURE_GETTER = ChessPiece::getCaptureOffsets;
}
