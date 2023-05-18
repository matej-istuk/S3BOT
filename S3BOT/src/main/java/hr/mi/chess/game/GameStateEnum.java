package hr.mi.chess.game;

/**
 * State of the <code>ChessGame</code>
 * @author Matej Istuk
 */
public enum GameStateEnum {
    IN_PROGRESS,
    WHITE_VICTORY,
    BLACK_VICTORY,
    DRAW,
    FORCED_STOP
}
