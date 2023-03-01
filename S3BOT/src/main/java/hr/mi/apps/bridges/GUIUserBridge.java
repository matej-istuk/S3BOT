package hr.mi.apps.bridges;

import hr.mi.chess.player.human.FromToPair;
import hr.mi.chess.player.human.UserBridge;

import javax.swing.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class GUIUserBridge implements UserBridge {

    private final BlockingQueue<FromToPair> blockingQueue;

    public GUIUserBridge(BlockingQueue<FromToPair> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    @Override
    public FromToPair requestMoveInput() {
        FromToPair fromToPair = null;
        blockingQueue.clear();

        do {
            try {
                fromToPair = blockingQueue.take();
            } catch (InterruptedException ignored){}
        } while (fromToPair == null);

        return fromToPair;
    }

    @Override
    public int requestPromotedPiece() {
        Object[] options = {"Rook",
                "Knight",
                "Bishop",
                "Queen"};
        return JOptionPane.showOptionDialog(null, "Chose a piece to promote to", "Promotion",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[3]);
    }
}
