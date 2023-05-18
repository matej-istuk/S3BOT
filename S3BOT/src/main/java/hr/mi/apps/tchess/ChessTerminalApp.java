package hr.mi.apps.tchess;

import hr.mi.chess.algorithm.support.OpeningBook;
import hr.mi.chess.constants.ChessConstants;
import hr.mi.chess.constants.ChessPieceConstants;
import hr.mi.chess.game.ChessGame;
import hr.mi.chess.game.GameStateEnum;
import hr.mi.chess.models.BoardState;
import hr.mi.chess.player.Player;
import hr.mi.chess.player.ai.PlayerAlan;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Chess app in the terminal. Obsolete by the gui app.
 * @author Matej Istuk
 */
public class ChessTerminalApp {

    private static final String ROW_SEPARATOR = "+---+---+---+---+---+---+---+---+";
    private static final String FINAL_ROW = "  a   b   c   d   e   f   g   h";

    /**
     * The constructor, starts the game.
     */
    public ChessTerminalApp() {
        playGame();
    }

    /**
     * Establishes everything necessary for the chess game.
     */
    private static void playGame(){
        Player whitePlayer = new PlayerAlan("baron30.bin", OpeningBook.MOST_COMMON_MOVE, 0);;
        Player blackPlayer = new PlayerAlan("baron30.bin", OpeningBook.MOST_COMMON_MOVE);


        ChessGame game = new ChessGame(whitePlayer, blackPlayer);
        GameStateEnum gameState = GameStateEnum.IN_PROGRESS;

        drawGame(game.getBoardState());

        while (gameState == GameStateEnum.IN_PROGRESS){
            gameState = game.advance();
            try {
                clearScreen();
            } catch (Exception ignored){

            }
            drawGame(game.getBoardState());
        }

        switch (gameState){
            case WHITE_VICTORY -> System.out.printf("%nWhite wins!%n");
            case BLACK_VICTORY -> System.out.printf("%nBlack wins!%n");
            case DRAW -> System.out.printf("%nDraw%n");
            case FORCED_STOP -> System.out.printf("%nForced Stop%n");
            case IN_PROGRESS -> throw new IllegalStateException();
        }
    }

    /**
     * Draws the board and other info in a text format from the received board-state.
     * @param boardState the board-state to be drawn
     */
    public static void drawGame(BoardState boardState){
        System.out.printf("Currently active player: %s%n", boardState.getActiveColour() == ChessConstants.WHITE ? "white" : "black");
        drawBoard(boardState.getBitboards()).forEach(System.out::println);
        System.out.println();
    }

    /**
     * Draws the board in a text format from the received board-state.
     * @param bitboards long[] representing pieces
     * @return list of strings
     */
    private static List<String> drawBoard(long[] bitboards){
        char[][] board = getBoard(bitboards);
        List<String> boardVisual = new ArrayList<>();
        boardVisual.add(ROW_SEPARATOR);
        for (int i = 0; i < board.length; i++) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("|");

            for (char square: board[i]) {
                stringBuilder.append(String.format(" %c |", square));
            }

            stringBuilder.append(String.format(" %d", 8 - i));

            boardVisual.add(stringBuilder.toString());
            boardVisual.add(ROW_SEPARATOR);
        }

        boardVisual.add(FINAL_ROW);

        return boardVisual;
    }

    /**
     * Returns a matrix representation of the board where the pieces are characters as defined by the <code>BoardState</code> class.
     * @return a matrix representation of the board
     */
    private static char[][] getBoard(long[] bitboards){
        char[][] charBoard = new char[8][8];
        Arrays.stream(charBoard).forEach(o -> Arrays.fill(o, ' '));

        for (int i = 0; i < 64; i++){
            for (char piece: ChessPieceConstants.PIECE_INT_MAPPING.keySet()){
                if ((bitboards[ChessPieceConstants.PIECE_INT_MAPPING.get(piece)] & (1L << i)) != 0L){
                    //Reverses the rows
                    charBoard[(63 - i)/8][i%8] = piece;
                    break;
                }
            }
        }

        return charBoard;
    }

    /**
     * Clears the screen
     */
    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Main method, starts the program.
     * @param args args
     */
    public static void main(String[] args) {
        new ChessTerminalApp();
    }
}
