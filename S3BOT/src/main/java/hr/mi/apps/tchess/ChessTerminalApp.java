package hr.mi.apps.tchess;

import hr.mi.chess.constants.ChessConstants;
import hr.mi.chess.constants.ChessPieceConstants;
import hr.mi.chess.game.ChessGame;
import hr.mi.chess.game.support.GameStateEnum;
import hr.mi.chess.models.BoardState;
import hr.mi.chess.player.Player;
import hr.mi.chess.player.ai.PlayerRandy;
import hr.mi.chess.player.human.HumanPlayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChessTerminalApp {

    private static final String ROW_SEPARATOR = "+---+---+---+---+---+---+---+---+";
    private static final String FINAL_ROW = "  a   b   c   d   e   f   g   h";
    public ChessTerminalApp() {
        playGame();
    }

    private void playGame(){
        Player whitePlayer = new HumanPlayer(new TerminalUserBridge());
        Player blackPlayer = new PlayerRandy(true);

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
            case IN_PROGRESS -> throw new IllegalStateException();
        }
    }

    private void drawGame(BoardState boardState){
        System.out.printf("Currently active player: %s%n", boardState.getActiveColour() == ChessConstants.WHITE ? "white" : "black");
        drawBoard(boardState.getBitboards()).forEach(System.out::println);
        System.out.println();
    }

    private List<String> drawBoard(long[] bitboards){
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
    private char[][] getBoard(long[] bitboards){
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

    private static void clearScreen() throws IOException, InterruptedException {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void main(String[] args) {
        new ChessTerminalApp();
    }
}
