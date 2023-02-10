package hr.mi.chess.movegen.helpers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BitwiseTest {
    @Test
    void test1(){
        assertEquals(0, Bitwise.findMS1B(1));
    }
    @Test
    void test2(){
        assertEquals(1, Bitwise.findMS1B(2));
    }

    @Test
    void test3(){
        assertEquals(1, Bitwise.findMS1B(3));
    }
    @Test
    void test4(){
        assertEquals(3, Bitwise.findMS1B(0xF));
    }
    @Test
    void test5(){
        assertEquals(63, Bitwise.findMS1B(-1));
    }
}