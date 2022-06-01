package byog.Core;

import byog.TileEngine.TETile;
import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;

import java.util.concurrent.TimeUnit;

public class gameTest {
    public Game game = new Game();

    @Test
    public void testGame() {
        String input = "n1234s";
        TETile[][] a = game.inputProcess(input);
        assertNull(a);
    }
    @Test
    public void testGetSeed(){
        String input = "n1234s";
        long output=game.getSeed(input);
        assertEquals(1234,output);
    }
    @Test
    public void testIsOverlap(){

        Game.Rectangle rec1=new Game.Rectangle(0,0,2,2);
        Game.Rectangle rec2=new Game.Rectangle(2,2,3,3);
        boolean output=game.isOverlap(rec1,rec2);
        assertFalse(output);
    }
    @Test
    public void testCanRoomPlaced(){

        Game.Rectangle rec0=new Game.Rectangle(0,0,3,3);
        Game.Rectangle rec1=new Game.Rectangle(0,0,2,2);
        Game.Rectangle rec2=new Game.Rectangle(2,2,3,3);
        Game.Rectangle rec3=new Game.Rectangle(3,3,4,4);
        ArrayList<Game.Rectangle> exist= new ArrayList<>();
        exist.add(rec2);
        exist.add(rec3);
        boolean output0=game.canRoomPlaced(rec0,exist);
        assertFalse(output0);
        boolean output1=game.canRoomPlaced(rec1,exist);
        assertTrue(output1);
    }
    @Test
    public void testWholeGame(){
        game.playWithInputString("n5197880843569031643s");
        game.playWithInputString("n5197880843569031643s");
        int i=0;
    }


    public static void main(String[] args) throws InterruptedException {
        Game game=new Game();
        game.playWithInputString("n5197880843569031643s");

        TimeUnit.SECONDS.sleep(5);
        game.playWithInputString("n5197880843569031643s");
    }
}


