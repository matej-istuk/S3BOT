package hr.mi.apps.jchess.components;

@FunctionalInterface
public interface TileClickListener {
    void tileClicked(int tileIndex);
}
