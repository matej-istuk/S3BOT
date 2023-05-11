package hr.mi.chess.player.ai;

import hr.mi.chess.algorithm.GameStateSearch;
import hr.mi.chess.algorithm.SearchEndCondition;
import hr.mi.chess.algorithm.support.OpeningBook;
import hr.mi.chess.evaluation.SimplePlusEvaluationFunction;
import hr.mi.chess.models.BoardState;
import hr.mi.chess.models.Move;
import hr.mi.chess.movegen.LegalMoveGenerator;
import hr.mi.chess.player.Player;

import java.util.List;
import java.util.Objects;

public class PlayerAlan implements Player {

    private  GameStateSearch gameStateSearch;
    private final OpeningBook openingBook;
    private boolean isInBook;
    private boolean isStopped;
    private final SearchEndCondition searchEndCondition;

    public PlayerAlan() {
        this(null, 0);
    }

    public PlayerAlan(String openingBookName, int decisionStyle){
        if (openingBookName != null){
            openingBook = new OpeningBook(openingBookName, decisionStyle);
            isInBook = true;
        } else {
            openingBook = null;
            isInBook = false;
        }
        this.searchEndCondition = new SearchEndCondition();
        searchEndCondition.setMaxTime(10000);
        this.gameStateSearch = new GameStateSearch(new SimplePlusEvaluationFunction());
    }

    @Override
    public Move requestMove(BoardState boardState) {
        if (isStopped) {
            throw new IllegalStateException();
        }
        //check book
        if (isInBook){
            assert openingBook != null;
            Move bookMove = openingBook.getOpeningBookMove(boardState);
            if (bookMove != null && LegalMoveGenerator.generateMoves(boardState).contains(bookMove)) {
                return bookMove;
            } else {
                isInBook = false;
            }
        }
        return gameStateSearch.getBestMove(boardState, searchEndCondition);
    }

    @Override
    public void stop() {
        searchEndCondition.setManualStop(true);
        isStopped = true;
    }
}
