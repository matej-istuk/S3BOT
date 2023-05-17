package hr.mi.apps.jchess.components;

/**
 * Listener
 */
@FunctionalInterface
public interface TileClickListener {
    /**
     * Activates whenever a tile is clicked, receives the index of the tile clicked.
     * @param tileIndex index of the tile clicked
     */
    void tileClicked(int tileIndex);
}
