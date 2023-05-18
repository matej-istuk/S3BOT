package hr.mi.apps.uci.commands.support;

/**
 * Enum containing all commands for the UCI protocol. Its only parameter is the command text.
 * @author Matej Istuk
 */
public enum Commands {
    UCI("uci"),
    DEBUG("debug"),
    IS_READY("isready"),
    SET_OPTION("setoption"),
    REGISTER("register"),
    UCI_NEW_GAME("ucinewgame"),
    POSITION("position"),
    GO("go"),
    STOP("stop"),
    PONDER_HIT("ponderhit"),
    QUIT("quit");

    private final String text;

    Commands(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
