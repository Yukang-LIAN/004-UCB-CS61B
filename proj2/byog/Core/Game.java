package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    /** Method used for playing a fresh game. The game should start from the main menu. */
    public void playWithKeyboard() {}

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
        return generateWorld(getSeed(input));
    }

    /** * @param SEED is a an argument * @return the loaded world */
    /** ! !!!!!! TODO: loadGame */
    public TETile[][] loadGame(String input) {
        return null;
    }

    /** * @param input is a String * @return String to int */
    public int getSeed(String input) {
        input = input.substring(0, input.length() - 1);
        input = input.substring(1);
        return Integer.parseInt(input);
    }

    /** * @param SEED is a random number * @return the whole world */
    public TETile[][] generateWorld(int SEED) {
        TETile[][] randomRoom = generateRandomRoom(SEED);
        TETile[][] linkedRoom = linkedRandomRoom(randomRoom);
        return null;
    }

    /** * @param SEED is a random number * @return the random room */
    public TETile[][] generateRandomRoom(int SEED) {
        return null;
    }

    /** * @param randomRoom is a random room * @return the linked random room */
    public TETile[][] linkedRandomRoom(TETile[][] randomRoom) {
        return null;
    }
}
