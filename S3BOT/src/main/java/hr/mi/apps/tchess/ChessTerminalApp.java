package hr.mi.apps.tchess;

import hr.mi.apps.ChessModelAdapter;

public class ChessTerminalApp {
    public static void main(String[] args) {
        ChessModelAdapter chessModelAdapter = new ChessModelAdapter();
        printBoard(chessModelAdapter.getBoard());
    }

    private static void printBoard(char[][] board){
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                System.out.print(board[i][j]);
            }
            System.out.print("\n");
        }
    }
}
