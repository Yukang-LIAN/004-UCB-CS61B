package byog.lab5;

import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/** Draws a world consisting of hexagonal regions. */
public class HexWorld {
    private static final int WIDTH = 60;
    private static final int HEIGHT = 30;

    private static void drawHexagon(TETile[][] m, int size, int pos_x, int pos_y) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < 2 * i + size; j++) {
                m[pos_x + j - i][pos_y - i] = Tileset.WALL;
            }
        }
        for (int l = size - 1; l >= 0; l--) {
            for (int j = 0; j < 2 * l + size; j++) {
                m[pos_x + j - l][pos_y - 2 * size + 1 + l] = Tileset.WALL;
            }
        }
    }

    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT

        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        // fills in a block 14 tiles wide by 4 tiles tall
        int x = 30;
        int y = 15;
        int min = 1;
        int max = 2;

        drawHexagon(world, 5, 30, 15);

        // draws the world to the screen
        ter.renderFrame(world);
    }
}
