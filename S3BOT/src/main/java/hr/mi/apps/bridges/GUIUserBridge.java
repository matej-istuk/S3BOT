package hr.mi.apps.bridges;

import hr.mi.chess.player.human.FromToPair;
import hr.mi.chess.player.human.UserBridge;

public class GUIUserBridge implements UserBridge {
    @Override
    public FromToPair requestMoveInput() {
        return null;
    }

    @Override
    public int requestPromotedPiece() {
        return 0;
    }
}
