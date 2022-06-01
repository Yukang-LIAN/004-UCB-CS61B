package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 100;
    public static final int HEIGHT = 50;

    public static long SEED;

    Random RANDOM = new Random(SEED);

    public ArrayList<Rectangle> existRects;

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

        private int getRoomX() {
            return left + (right - left) / 2;
        }

        private int getRoomY() {
            return bottom + (top - bottom) / 2;
        }

        private int getWidth() {
            return right - left;
        }

        private int getHeight() {
            return top - bottom;
        }
    }

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

        return inputProcess(input);
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
        TETile[][] finalWorldFrame = generateWorld();
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
    public long getSeed(String input) {
        int indexS = input.indexOf('s');
        String stringSEED = input.substring(1, indexS);
        SEED = Long.parseLong(stringSEED);
        return Long.parseLong(stringSEED);
    }

    /** * @param SEED is a random number * @return the whole world */
    public TETile[][] generateWorld() {
        TETile[][] randomRoom = generateRandomRoom(50);
        TETile[][] linkedRoom = linkedRandomRoom(randomRoom);
        TETile[][] finalWorldFrame = generateDoor(linkedRoom);
        return finalWorldFrame;
    }

    /** * @param SEED is a random number * @return the random room */
    public TETile[][] generateRandomRoom(int roomNum) {
        ArrayList<Rectangle> existRects = new ArrayList<>();
        int th = 0;
        for (int i = 0; i < roomNum; ) {
            Rectangle rectangle = generateRectangle(th);
            if (canRoomPlaced(rectangle, existRects)) {
                i++;
                existRects.add(rectangle);
            } else {
                th++;
                if (th > 1000) break;
            }
        }
        this.existRects = existRects;
        return roomSet(existRects);
    }

    /** * @param randomRoom is a random room * @return the linked random room */
    /** !! TODO: linkedRoom */
    public TETile[][] linkedRandomRoom(TETile[][] randomRoom) {
        TETile[][] linkedRandomRoom = new TETile[WIDTH][HEIGHT];
        Rectangle mainRoom = existRects.remove(existRects.size() - 1);
        int mainX = mainRoom.getRoomX(), mainY = mainRoom.getRoomY();
        existRects.sort(Comparator.comparing(o -> (o.left * o.left + o.bottom * o.bottom)));
        /** !!!!! */
        for (var room : existRects) {
            int roomX = RANDOM.nextInt(room.getWidth()) + room.left;
            int roomY = RANDOM.nextInt(room.getHeight()) + room.bottom;
            linkedRandomRoom = linked2Room(randomRoom, mainX, mainY, roomX, roomY);
            mainX = roomX;
            mainY = roomY;
        }
        linkedRandomRoom = fixWall(linkedRandomRoom);
        return linkedRandomRoom;
    }

    public TETile[][] linked2Room(
            TETile[][] randomRoom, int mainX, int mainY, int roomX, int roomY) {
        TETile[][] linkedRandomRoom = randomRoom;
        if (mainX < roomX) {
            while (mainX < roomX) {
                linkedRandomRoom[mainX][mainY] = Tileset.FLOOR;
                mainX++;
            }
        } else {
            while (mainX > roomX) {
                linkedRandomRoom[mainX][mainY] = Tileset.FLOOR;
                mainX--;
            }
        }
        if (mainY < roomY) {
            while (mainY < roomY) {
                linkedRandomRoom[mainX][mainY] = Tileset.FLOOR;
                mainY++;
            }
        } else {
            while (mainY > roomY) {
                linkedRandomRoom[mainX][mainY] = Tileset.FLOOR;
                mainY--;
            }
        }
        return linkedRandomRoom;
    }

    public TETile[][] fixWall(TETile[][] linkedRandomRoom) {
        for (int x = 1; x < WIDTH - 1; x++) {
            for (int y = 1; y < HEIGHT - 1; y++) {
                if (linkedRandomRoom[x][y] == Tileset.FLOOR) {
                    for (int i = -1; i < 2; i++) {
                        for (int j = -1; j < 2; j++) {
                            if (i == 0 && j == 0) {
                                continue;
                            }
                            if (linkedRandomRoom[x + i][y + j] == Tileset.NOTHING) {
                                linkedRandomRoom[x + i][y + j] = Tileset.WALL;
                            }
                        }
                    }
                }
            }
        }
        return linkedRandomRoom;
    }

    public Rectangle generateRectangle(int tryNum) {
        int randomWidth, randomHeight;
        if (tryNum < 50) {
            randomWidth = RANDOM.nextInt(WIDTH / 10) + 2;
            randomHeight = RANDOM.nextInt(HEIGHT / 5) + 2;
        } else {
            randomWidth = RANDOM.nextInt(WIDTH / 20) + 2;
            randomHeight = RANDOM.nextInt(HEIGHT / 10) + 2;
        }
        int left = RANDOM.nextInt(WIDTH - randomWidth - 1) + 1;
        int bottom = RANDOM.nextInt(HEIGHT - randomHeight - 1) + 1;
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
        //ter.initialize(WIDTH, HEIGHT);
        //ter.renderFrame(finalWorldFrame);
    }

    public TETile[][] generateDoor(TETile[][] linkedRandomRoom) {
        while (true) {
            int x = RANDOM.nextInt(WIDTH);
            int y = RANDOM.nextInt(HEIGHT);
            if (linkedRandomRoom[x][y] == Tileset.WALL) {
                if ((linkedRandomRoom[x - 1][y] == Tileset.WALL)
                        && (linkedRandomRoom[x + 1][y] == Tileset.WALL)) {
                    linkedRandomRoom[x][y] = Tileset.LOCKED_DOOR;
                    break;
                }
                if ((linkedRandomRoom[x][y - 1] == Tileset.WALL)
                        && (linkedRandomRoom[x][y + 1] == Tileset.WALL)) {
                    linkedRandomRoom[x][y] = Tileset.LOCKED_DOOR;
                    break;
                }
            }
        }
        return linkedRandomRoom;
    }

    /** !! TODO: play */
    public void play(TETile[][] finalWorldFrame) {}
}
