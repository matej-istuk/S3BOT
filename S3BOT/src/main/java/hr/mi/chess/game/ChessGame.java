package hr.mi.chess.game;

import hr.mi.chess.constants.ChessBoardConstants;
import hr.mi.chess.models.BoardState;
import hr.mi.chess.models.Move;
import hr.mi.chess.player.Player;
import hr.mi.chess.util.BoardFunctions;
import hr.mi.chess.movegen.LegalMoveGenerator;
import hr.mi.chess.constants.ChessConstants;

import java.io.BufferedWriter;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ChessGame {
    private final BoardState boardState;
    private final Player whitePlayer;
    private final Player blackPlayer;
    private final List<GameListener> listeners;
    private final List<GameListener> gameSavedlisteners;
    private final String startTime;
    private volatile boolean forceStop = false;
    private volatile boolean isSaved = false;

    public ChessGame(Player whitePlayer, Player blackPlayer) {
        this(ChessBoardConstants.STARTING_POSITION_FEN, whitePlayer, blackPlayer);
    }

    public ChessGame(String fen, Player whitePlayer, Player blackPlayer) {
        this.boardState = new BoardState(fen);
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.listeners = new ArrayList<>();
        this.gameSavedlisteners = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        this.startTime = String.format("%d-%d-%d_%d:%d:%d", now.getYear(), now.getMonth().getValue(), now.getDayOfMonth(), now.getHour(), now.getMinute(), now.getSecond());

    }

    public GameStateEnum advance(){
        Player activePlayer = boardState.getActiveColour() == ChessConstants.WHITE ? whitePlayer : blackPlayer;
        List<Move> legalMoves = LegalMoveGenerator.generateMoves(boardState);

        //check if the game is finished
        if (legalMoves.isEmpty()){
            saveToDisc();
            //if the king is in check, it's a victory for the passive colour
            if (BoardFunctions.determineCheckByColour(boardState.getBitboards(), boardState.getActiveColour())){
                return boardState.getPassiveColour() == ChessConstants.WHITE ? GameStateEnum.WHITE_VICTORY : GameStateEnum.BLACK_VICTORY;
            }
            //otherwise it's a draw
            else{
                return GameStateEnum.DRAW;
            }
        }

        Move move;
        //wait until a legal move is played
        do {
            move = activePlayer.requestMove(boardState);
        } while (!(legalMoves.contains(move) || move == null));

        if (forceStop || move == null){
            saveToDisc();
            return GameStateEnum.FORCED_STOP;
        }

        boardState.makeMove(move);
        updateListeners();

        return GameStateEnum.IN_PROGRESS;
    }

    public void saveToDisc(){
        String error = "";
        Path gamePath = Path.of("games" + File.separator + "chess_game_" + startTime);
        gamePath.getParent().toFile().mkdirs();
        try (BufferedWriter writer = Files.newBufferedWriter(gamePath, StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {
            for (Move move : this.getBoardState().getPreviousMoves()) {
                writer.write(move.toString() + " ");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        isSaved = true;
        updateGameSavedListeners();
    }
    public BoardState getBoardState() {
        return boardState;
    }

    public void stop() {
        forceStop = true;
        whitePlayer.stop();
        blackPlayer.stop();
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void addGameListener(GameListener listener){
        listeners.add(listener);
    }

    private void updateListeners(){
        listeners.forEach(GameListener::gameStateUpdated);
    }

    public void addGameSavedListener(GameListener listener){
        gameSavedlisteners.add(listener);
    }

    private void updateGameSavedListeners(){
        gameSavedlisteners.forEach(GameListener::gameStateUpdated);
    }
}
