package hr.mi.chess.algorithm.support;

import hr.mi.chess.models.BoardState;
import hr.mi.chess.models.Move;
import hr.mi.chess.util.ChessTranslator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;

/**
 * Class acting as an opening book for chess. It can load any opening book in the <a href="https://www.chessprogramming.org/PolyGlot">Polyglot</a>
 * format, and can be adjusted with various ways of choosing moves from the book.
 * @author Matej Istuk
 */
public class OpeningBook {

    public static final int MOST_COMMON_MOVE = 1;
    public static final int WEIGHTED_RANDOM_MOVE = 2;
    public static final int TRUE_RANDOM_MOVE = 3;
    public static final int LEAST_COMMON_MOVE = 4;

    private final Map<Long, List<Entry>> moveMap;

    private final Function<List<Entry>, Short> moveChooser;

    /**
     * The constructor, loads the file with the received openingBookName name from the OpeningBooks folder and sets the
     * chosen move decision style.
     * @param openingBookName name of the opening book file
     * @param decisionStyle decision style, defined by the following public class variables: MOST_COMMON_MOVE,
     *                      WEIGHTED_RANDOM_MOVE, TRUE_RANDOM_MOVE, LEAST_COMMON_MOVE
     */
    public OpeningBook(String openingBookName, int decisionStyle) {
        moveMap = loadOpeningBook(openingBookName);
        moveChooser = switch (decisionStyle) {
            case MOST_COMMON_MOVE -> MOST_COMMON_MOVE_STRATEGY;
            case WEIGHTED_RANDOM_MOVE -> WEIGHTED_RANDOM_MOVE_STRATEGY;
            case TRUE_RANDOM_MOVE -> TRUE_RANDOM_MOVE_STRATEGY;
            case LEAST_COMMON_MOVE -> LEAST_COMMON_MOVE_STRATEGY;
            default -> throw new IllegalArgumentException();
        };
    }

    /**
     * Loads the opening book from the file located in the OpeningBooks folder.
     * @param openingBookName name of the opening book file
     * @return opening book as a <code>Map<Long, List<Entry>></code>
     */
    private Map<Long, List<Entry>> loadOpeningBook(String openingBookName) {
        Map<Long, List<Entry>> moveMap = new HashMap<>();
        try (DataInputStream dataInputStream = new DataInputStream(Files.newInputStream(Path.of(this.getClass().getClassLoader().getResource(Paths.get("OpeningBooks", openingBookName).toString()).getPath())))){
            long key;
            while (dataInputStream.available() > 0){
                key = dataInputStream.readLong();
                if (!moveMap.containsKey(key)){
                    moveMap.put(key, new ArrayList<>());
                }
                moveMap.get(key).add(new Entry(dataInputStream.readShort(), dataInputStream.readShort(), dataInputStream.readInt()));
            }
        } catch (IOException e) {
            System.out.printf("Opening book reading fail!%n%s", e.getMessage());
        }

        return moveMap;
    }

    /**
     * Returns an opening book move for the received position (chosen by the predefined strategy)
     * @param boardState boardstate for which the move is requested
     * @return chosen move
     */
    public Move getOpeningBookMove(BoardState boardState){
        if (!moveMap.containsKey(boardState.getZobristHash())) {
            return null;
        }
        try {
            return ChessTranslator.algebraicToMove(codeToAlgebraic(moveChooser.apply(moveMap.get(boardState.getZobristHash()))), boardState);
        } catch (IllegalStateException e){
            return null;
        }
    }

    /**
     * Turns the polyglot move encoding into the algebraic notation (for example e4e5 a7a8q...).
     * @param code polyglot move code
     * @return algebraic notation
     */
    private static String codeToAlgebraic(short code){
        String from = coordinatesToAlgebraicPos((code >>> 6) & 0b111, (code >>> 9) & 0b111);
        String to = coordinatesToAlgebraicPos(code & 0b111, (code >>> 3) & 0b111);
        String promotion = switch ((code >>> 12) & 0b111) {
            case 0 -> "";
            case 1 -> "k";
            case 2 -> "b";
            case 3 -> "r";
            case 4 -> "q";
            default -> throw new RuntimeException();
        };

        return from + to + promotion;
    }

    /**
     * Chess board coordinates to algebraic position ((0,0) is a1)
     * @param file int between 0 and 7 (inclusive)
     * @param row int between 0 and 7 (inclusive)
     * @return Algebraic position string
     */
    private static String coordinatesToAlgebraicPos(int file, int row) {
        return ChessTranslator.LERFToAlgebraicPos(8*row+file);
    }

    private static final Random random = new Random();

    /**
     * Record for the opening book map.
     * @param move Polyglot encoding (as a short) for the move
     * @param weight measure of the move quality (bigger is better)
     * @param learn unused, see <a href="http://hgm.nubati.net/book_format.html">more</a>
     */
    private record Entry (short move, short weight, int learn) {}

    /**
     * Strategy for selecting the most common opening move from the received ones
     */
    private static final Function<List<Entry>, Short> MOST_COMMON_MOVE_STRATEGY = entries -> entries.stream().max(Comparator.comparingInt(Entry::weight)).orElseThrow().move();

    /**
     * Strategy for selecting a random (weighted by quality) move from the received ones
     */
    private static final Function<List<Entry>, Short> WEIGHTED_RANDOM_MOVE_STRATEGY = entries -> {
        int chosenWeight = random.nextInt(entries.stream().mapToInt(Entry::weight).sum());
        int weightSum = 0;
        for (Entry entry : entries) {
            weightSum += entry.weight();
            if (chosenWeight < weightSum) {
                return entry.move();
            }
        }
        throw new RuntimeException();
    };

    /**
     * Strategy for selecting a random (unweighted) move from the received ones
     */
    private static final Function<List<Entry>, Short> TRUE_RANDOM_MOVE_STRATEGY = entries -> entries.get(random.nextInt(entries.size())).move();

    /**
     * Strategy for selecting the least common opening move from the received ones
     */
    private static final Function<List<Entry>, Short> LEAST_COMMON_MOVE_STRATEGY = entries -> entries.stream().min(Comparator.comparingInt(Entry::weight)).orElseThrow().move();
}
