package hr.mi.apps;

import hr.mi.chess.models.BoardState;
import hr.mi.chess.util.constants.ChessBoardConstants;
import hr.mi.chess.util.constants.ChessPieceConstants;

import java.util.Arrays;

public class ChessModelAdapter {
    private BoardState boardState;

    public ChessModelAdapter() {
        this.boardState = new BoardState();
    }

    public ChessModelAdapter(String fen) {
        this.boardState = new BoardState(fen);
    }

    /**
     * Returns a matrix representation of the board where the pieces are characters as defined by the <code>BoardState</code> class.
     * @return a matrix representation of the board
     */
    public char[][] getBoard(){
        long[] bitboards = boardState.getBitboards();
        char[][] charBoard = new char[8][8];
        Arrays.stream(charBoard).forEach(o -> Arrays.fill(o, '.'));

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
}