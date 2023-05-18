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

/**
 * AI chess player utilising the <code>GameStateSearch</code> class with the <code>SimplePlusEvaluationFunction</code>
 * eval. func.
 * <br>
 * Can read moves from the opening book for better early games.
 * @author Matej Istuk
 */
public class PlayerAlan implements Player {

    private final GameStateSearch gameStateSearch;
    private final OpeningBook openingBook;
    private boolean isInBook;
    private boolean isStopped;
    private final SearchEndCondition searchEndCondition;

    /**
     * Constructor, doesn't define an opening book.
     */
    public PlayerAlan() {
        this(null, OpeningBook.WEIGHTED_RANDOM_MOVE);
    }

    /**
     * Constructor, tries to load the requested opening book with the specified decision style. See class
     * <code>OpeningBook</code> for more. Sets transposition table size at 33554432.
     * @param openingBookName name of the opening book
     * @param decisionStyle move choosing style
     */
    public PlayerAlan(String openingBookName, int decisionStyle){
        this(openingBookName, decisionStyle, 33554432);
    }

    /**
     * Constructor, tries to load the requested opening book with the specified decision style. See class
     * <code>OpeningBook</code> for more.
     * @param openingBookName name of the opening book
     * @param decisionStyle move choosing style
     * @param ttSize size of the transposition table
     */
    public PlayerAlan(String openingBookName, int decisionStyle, int ttSize){
        if (openingBookName != null){
            openingBook = new OpeningBook(openingBookName, decisionStyle);
            isInBook = true;
        } else {
            openingBook = null;
            isInBook = false;
        }
        this.searchEndCondition = new SearchEndCondition();
        searchEndCondition.setMaxTime(10000);
        this.gameStateSearch = new GameStateSearch(new SimplePlusEvaluationFunction(), ttSize);
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
