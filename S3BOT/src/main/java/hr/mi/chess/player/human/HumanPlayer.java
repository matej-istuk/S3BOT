package hr.mi.chess.player.human;

import hr.mi.chess.models.BoardState;
import hr.mi.chess.models.Move;
import hr.mi.chess.movegen.LegalMoveGenerator;
import hr.mi.chess.player.Player;

import java.util.ArrayList;
import java.util.List;

public class HumanPlayer implements Player {

    private final UserBridge userBridge;

    public HumanPlayer(UserBridge userBridge) {
        this.userBridge = userBridge;
    }

    @Override
    public Move requestMove(BoardState boardState) {
        List<Move> moves = LegalMoveGenerator.generateMoves(boardState);

        Move move;

        do {
            FromToPair toFrom = userBridge.requestMoveInput();
            move = findCorrespondingMove(moves, toFrom.from(), toFrom.to());
        } while (move == null);

        return move;
    }

    private Move findCorrespondingMove (List<Move> moves, int from, int to){
        //a list because pawn promotions will have multiple options
        List<Move> possibleMoves = new ArrayList<>();

        for (Move move: moves){
            if (move.getFrom() == from && move.getTo() == to){
                possibleMoves.add(move);
            }
        }

        //4 possible promotion moves
        if (possibleMoves.size() == 4){
            int promotedPieceFlags = userBridge.requestPromotedPiece() - 1;
            assert promotedPieceFlags >= 0 && promotedPieceFlags <= 3;
            for (Move move: possibleMoves){
                if ((move.getFlags() & 3) != promotedPieceFlags) {
                    moves.remove(move);
                }
            }
        }

        if (possibleMoves.size() == 1){
            return possibleMoves.get(0);
        }

        return null;
    }
}
