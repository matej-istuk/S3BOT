package hr.mi.apps.uci.support;

import hr.mi.chess.algorithm.GameStateSearch;
import hr.mi.chess.evaluation.SimplePlusEvaluationFunction;

public class SearchManager {
    private GameStateSearch gameStateSearch;

    public SearchManager() {
        this.gameStateSearch = new GameStateSearch(new SimplePlusEvaluationFunction());

    }
}
