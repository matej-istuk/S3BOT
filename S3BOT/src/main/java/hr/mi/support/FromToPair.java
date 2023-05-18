package hr.mi.support;

/**
 * Record of a from to pair, both parameters are in
 * <a href="https://www.chessprogramming.org/Square_Mapping_Considerations#Little-Endian_File-Rank_Mapping">LERF notation</a>..
 * @param from
 * @param to
 * @author Matej Istuk
 */
public record FromToPair(int from, int to) {
}
