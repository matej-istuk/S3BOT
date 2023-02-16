package hr.mi.chess.player.human;

public interface UserBridge {
    FromToPair requestMoveInput();
    int requestPromotedPiece();
}
