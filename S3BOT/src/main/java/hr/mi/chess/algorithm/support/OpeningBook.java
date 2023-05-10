package hr.mi.chess.algorithm.support;

import hr.mi.chess.models.BoardState;
import hr.mi.chess.models.Move;
import hr.mi.chess.util.ChessTranslator;
import hr.mi.support.FromToPair;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;

public class OpeningBook {

    public static final int MOST_COMMON_MOVE = 1;
    public static final int WEIGHTED_RANDOM_MOVE = 2;
    public static final int TRUE_RANDOM_MOVE = 3;
    public static final int LEAST_COMMON_MOVE = 4;

    private final Map<Long, List<Entry>> moveMap;

    private final Function<List<Entry>, Short> moveChooser;

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

    private Map<Long, List<Entry>> loadOpeningBook(String openingBookName) {
        Map<Long, List<Entry>> moveMap = new HashMap<>();
        try (DataInputStream dataInputStream = new DataInputStream(Files.newInputStream(Path.of(this.getClass().getClassLoader().getResource("OpeningBooks/" + openingBookName).getPath())))){
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

    private static String coordinatesToAlgebraicPos(int file, int row) {
        return ChessTranslator.LERFToAlgebraicPos(8*row+file);
    }

    private static final Random random = new Random();

    private static record Entry (short move, short weight, int learn) {}

    private static final Function<List<Entry>, Short> MOST_COMMON_MOVE_STRATEGY = entries -> entries.stream().max(Comparator.comparingInt(Entry::weight)).orElseThrow().move();
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
    private static final Function<List<Entry>, Short> TRUE_RANDOM_MOVE_STRATEGY = entries -> entries.get(random.nextInt(entries.size())).move();
    private static final Function<List<Entry>, Short> LEAST_COMMON_MOVE_STRATEGY = entries -> entries.stream().min(Comparator.comparingInt(Entry::weight)).orElseThrow().move();


}
