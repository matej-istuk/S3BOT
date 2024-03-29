package hr.mi.chess.player.human;

import hr.mi.apps.bridges.UserBridge;
import hr.mi.chess.models.BoardState;
import hr.mi.chess.models.Move;
import hr.mi.chess.movegen.LegalMoveGenerator;
import hr.mi.chess.player.Player;
import hr.mi.support.FromToPair;

import java.util.ArrayList;
import java.util.List;

/**
 * Class describing a human chess player. Takes move input through an implementation of <code>UserBridge</code>.
 * @author Matej Istuk
 */
public class HumanPlayer implements Player {
    private final UserBridge userBridge;
    private volatile boolean isStopped = false;

    /**
     * Constructor. Takes in the way through which the user will communicate through the given <code>UserBridge</code>
     * instance.
     * @param userBridge implementation of the <code>UserBridge</code> interface
     */
    public HumanPlayer(UserBridge userBridge) {
        this.userBridge = userBridge;
    }

    @Override
    public Move requestMove(BoardState boardState) {
        List<Move> moves = LegalMoveGenerator.generateMoves(boardState);

        Move move;

        do {
            FromToPair toFrom = userBridge.requestMoveInput();
            if (isStopped){
                return null;
            }
            move = findCorrespondingMove(moves, toFrom.from(), toFrom.to());
        } while (move == null);

        return move;
    }

    @Override
    public void stop() {
        isStopped = true;
        userBridge.stop();
    }

    /**
     * Finds the move corresponding to the received move from all legal moves.
     * @param moves legal moves
     * @param from from where the user wants to move
     * @param to to where the user wants to move
     * @return Move corresponding to the received coordinates, null if none found.
     */
    private Move findCorrespondingMove (List<Move> moves, int from, int to){
        //a list because pawn promotions will have multiple options
        List<Move> possibleMoves = new ArrayList<>();
        Move correspondingMove = null;

        for (Move move: moves){
            if (move.getFrom() == from && move.getTo() == to){
                possibleMoves.add(move);
            }
        }

        if (possibleMoves.size() == 1){
            correspondingMove = possibleMoves.get(0);
        }
        //4 possible promotion moves
        else if (possibleMoves.size() == 4){
            int promotedPieceFlags = userBridge.requestPromotedPiece();
            assert promotedPieceFlags >= 0 && promotedPieceFlags <= 3;
            for (Move move: possibleMoves){
                if ((move.getFlags() & 3) == promotedPieceFlags) {
                    correspondingMove = move;
                }
            }
        }

        return correspondingMove;
    }
}
