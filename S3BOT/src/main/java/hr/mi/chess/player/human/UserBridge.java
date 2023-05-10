package hr.mi.chess.player.human;

import hr.mi.support.FromToPair;

public interface UserBridge {
    FromToPair requestMoveInput();
    int requestPromotedPiece();
}
