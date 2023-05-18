package hr.mi.apps.bridges;

import hr.mi.support.FromToPair;

import javax.swing.*;
import java.util.concurrent.BlockingQueue;

/**
 * Implementation of the UserBridge for the <code>ChessGuiApp</code>, allows for communication between the
 * <code>Swing</code> user interface and the chess game.
 * @author Matej Istuk
 */
public class GUIUserBridge implements UserBridge {

    private final BlockingQueue<FromToPair> blockingQueue;

    /**
     * The constructor, receives a blocking queue through which it will receive moves from the GUI.
     * @param blockingQueue input blocking queue
     */
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

    @Override
    public void stop() {
        do {
            try {
                blockingQueue.put(new FromToPair(-1, -1));
                return;
            } catch (InterruptedException ignored){}
        } while (true);

    }
}
