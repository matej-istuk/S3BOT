package hr.mi.chess.movegen.helpers;

public class Bitwise {
    private static final long[] masks = {0x2L, 0xCL, 0xF0L, 0xFF00L, 0xFFFF0000L, 0xFFFFFFFF00000000L};
    private static final int[] bits = {1, 2, 4, 8, 16, 32};

    public static int findIndexOfMS1B(long num){
        int msBit = 0;
        for (int i = 5; i >= 0; i--)
        {
            if ((num & masks[i]) != 0)
            {
                num >>= bits[i];
                msBit |= bits[i];
            }
        }
        return msBit;
    }
}