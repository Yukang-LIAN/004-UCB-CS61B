package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Random;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 100;
    public static final int HEIGHT = 50;

    /** Method used for playing a fresh game. The game should start from the main menu. */
    public void playWithKeyboard() {}

    public static class Rectangle {
        int left;
        int right;
        int top;
        int bottom;

        public Rectangle(int left, int bottom, int right, int top) {
            this.left = left;
            this.right = right;
            this.top = top;
            this.bottom = bottom;
        }
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series of
     * characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should behave
     * exactly as if the user typed these characters into the game after playing playWithKeyboard.
     * If the string ends in ":q", the same world should be returned as if the string did not end
     * with q. For example "n123sss" and "n123sss:q" should return the same world. However, the
     * behavior is slightly different. After playing with "n123sss:q", the game should save, and
     * thus if we then called playWithInputString with the string "l", we'd expect to get the exact
     * same world back again, since this corresponds to loading the saved game.
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // TODO: Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().

        TETile[][] finalWorldFrame = inputProcess(input);
        return finalWorldFrame;
    }

    /** * @param input is an argument * @return the finalWorldFrame */
    public TETile[][] inputProcess(String input) {
        input = input.toLowerCase();
        char firstChar = input.charAt(0);
        TETile[][] finalWorldFrame = null;
        if (firstChar == 'n') {
            finalWorldFrame = newGame(input);
        } else if (firstChar == 'l') {
            finalWorldFrame = loadGame(input);
        } else if (firstChar == 'q') {
            System.exit(0);
        }
        return finalWorldFrame;
    }

    /** * @param input is an argument * @return the new random world */
    public TETile[][] newGame(String input) {
        long SEED = getSeed(input);
        TETile[][] finalWorldFrame = generateWorld(SEED);
        showWorld(finalWorldFrame);
        play(finalWorldFrame);
        return finalWorldFrame;
    }

    /** * @param SEED is a an argument * @return the loaded world */
    /** ! !!!!!! TODO: loadGame */
    public TETile[][] loadGame(String input) {
        return null;
    }

    /** * @param input is a String * @return String to int */
    /** !!TODO: getseed */
    public long getSeed(String input) {
        input = input.substring(0, input.length() - 1);
        input = input.substring(1);
        return Long.parseLong(input);
    }

    /** * @param SEED is a random number * @return the whole world */
    public TETile[][] generateWorld(long SEED) {
        TETile[][] randomRoom = generateRandomRoom(SEED, 1000);
        TETile[][] linkedRoom = linkedRandomRoom(randomRoom);
        /** !! TODO: return linkedRoom */
        return randomRoom;
    }

    /** * @param SEED is a random number * @return the random room */
    public TETile[][] generateRandomRoom(long SEED, int roomNum) {
        ArrayList<Rectangle> existRects = new ArrayList<>();
        int th = 0;
        for (int i = 0; i < roomNum; ) {
            Rectangle rectangle = generateRectangle(new Random(SEED + i + th), th);
            if (canRoomPlaced(rectangle, existRects)) {
                i++;
                existRects.add(rectangle);
            } else {
                th++;
                if (th > 1000) break;
            }
        }
        return roomSet(existRects);
    }

    /** * @param randomRoom is a random room * @return the linked random room */
    public TETile[][] linkedRandomRoom(TETile[][] randomRoom) {
        return null;
    }

    public Rectangle generateRectangle(Random RANDOM, int tryNum) {
        int randomWidth, randomHeight;
        if (tryNum < 50) {
            randomWidth = RANDOM.nextInt(WIDTH / 10) + 2;
            randomHeight = RANDOM.nextInt(HEIGHT / 5) + 2;
        } else {
            randomWidth = RANDOM.nextInt(WIDTH / 20) + 2;
            randomHeight = RANDOM.nextInt(HEIGHT / 10) + 2;
        }
        int left = RANDOM.nextInt(WIDTH - randomWidth);
        int bottom = RANDOM.nextInt(HEIGHT - randomHeight);
        return new Rectangle(left, bottom, left + randomWidth, bottom + randomHeight);
    }

    public boolean canRoomPlaced(Rectangle rectangle, ArrayList<Rectangle> existRects) {
        for (var room : existRects) {
            if (isOverlap(room, rectangle)) {
                return false;
            }
        }
        return true;
    }

    public boolean isOverlap(Rectangle rec1, Rectangle rec2) {
        if (rec1.left == rec1.right
                || rec1.bottom == rec1.top
                || rec2.left == rec2.right
                || rec2.bottom == rec2.top) {
            return false;
        }
        return !(rec1.right < rec2.left - 1
                || // left
                rec1.top < rec2.bottom - 1
                || // bottom
                rec1.left > rec2.right + 1
                || // right
                rec1.bottom > rec2.top + 1); // top
    }

    public TETile[][] roomSet(ArrayList<Rectangle> existRects) {
        TETile[][] randomRoom = new TETile[WIDTH][HEIGHT];
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                randomRoom[i][j] = Tileset.NOTHING;
            }
        }
        /** !! TODO:draw floor */
        for (var room : existRects) {
            int x = room.left;
            int y = room.bottom;
            while (x < room.right) {
                randomRoom[x][y] = Tileset.WALL;
                x++;
            }
            while (y < room.top) {
                randomRoom[x][y] = Tileset.WALL;
                y++;
            }
            while (x > room.left) {
                randomRoom[x][y] = Tileset.WALL;
                x--;
            }
            while (y > room.bottom) {
                randomRoom[x][y] = Tileset.WALL;
                y--;
            }
            for (x = room.left + 1; x < room.right; x++) {
                for (y = room.bottom + 1; y < room.top; y++) {
                    randomRoom[x][y] = Tileset.FLOOR;
                }
            }
        }
        return randomRoom;
    }

    public void showWorld(TETile[][] finalWorldFrame) {
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(finalWorldFrame);
    }

    public void play(TETile[][] finalWorldFrame) {}
}
