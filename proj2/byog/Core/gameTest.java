package byog.Core;

import byog.TileEngine.TETile;
import static org.junit.Assert.*;
import org.junit.Test;

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
}
