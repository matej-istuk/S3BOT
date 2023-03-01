package hr.mi.chess.game;

import hr.mi.chess.constants.ChessBoardConstants;
import hr.mi.chess.models.BoardState;
import hr.mi.chess.models.Move;
import hr.mi.chess.player.Player;
import hr.mi.chess.util.BoardFunctions;
import hr.mi.chess.movegen.LegalMoveGenerator;
import hr.mi.chess.constants.ChessConstants;

import java.util.ArrayList;
import java.util.List;

public class ChessGame {
    private final BoardState boardState;
    private final Player whitePlayer;
    private final Player blackPlayer;
    private final List<GameListener> listeners;

    public ChessGame(Player whitePlayer, Player blackPlayer) {
        this(ChessBoardConstants.STARTING_POSITION_FEN, whitePlayer, blackPlayer);
    }

    public ChessGame(String fen, Player whitePlayer, Player blackPlayer) {
        this.boardState = new BoardState(fen);
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.listeners = new ArrayList<>();
    }

    public GameStateEnum advance(){
        Player activePlayer = boardState.getActiveColour() == ChessConstants.WHITE ? whitePlayer : blackPlayer;
        List<Move> legalMoves = LegalMoveGenerator.generateMoves(boardState);

        //check if the game is finished
        if (legalMoves.isEmpty()){
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
        } while (!legalMoves.contains(move));

        boardState.makeMove(move);
        updateListeners();

        return GameStateEnum.IN_PROGRESS;
    }

    public BoardState getBoardState() {
        return boardState;
    }

    public void addGameListener(GameListener listener){
        listeners.add(listener);
    }

    private void updateListeners(){
        listeners.forEach(GameListener::gameStateUpdated);
    }
}
