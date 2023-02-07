package hr.mi.apps.tchess;

import hr.mi.apps.ChessModelAdapter;

import java.util.Objects;
import java.util.Scanner;

public class ChessTerminalApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String fen = scanner.nextLine();
        ChessModelAdapter chessModelAdapter;
        if (Objects.equals(fen, "")){
            chessModelAdapter = new ChessModelAdapter();
        }else {
            chessModelAdapter = new ChessModelAdapter(fen);
        }
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
