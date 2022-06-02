package byog.Core;

import java.awt.Color;
import java.awt.Font;
import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

import static java.lang.System.exit;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 100;
    public static final int HEIGHT = 50;

    public static long SEED;

    public static Random RANDOM;

    public ArrayList<Rectangle> existRects;

    Player player = new Player();

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
    public void playWithKeyboard() {
        /** !! TODO: playWithKeyboard */
        drawStartUI();
        char firstChar = getFirstChar();
        if (firstChar == 'n') {
            newGame();
        } else if (firstChar == 'l') {
            loadGame();
        } else {
            exit(0);
        }
    }

    private char getFirstChar() {
        char c;
        while (true) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            c = Character.toLowerCase(StdDraw.nextKeyTyped());
            if (c == 'l' || c == 'n' || c == 'q') {
                break;
            }
        }
        return c;
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
            exit(0);
        }
        return finalWorldFrame;
    }

    public void newGame() {
        getSeed();
        TETile[][] finalWorldFrame = generateWorld();
        showWorld(finalWorldFrame);
        play(finalWorldFrame);
    }

    /** * @param input is an argument * @return the new random world */
    public TETile[][] newGame(String input) {
        getSeed(input);
        int indexS = input.indexOf('s');
        TETile[][] finalWorldFrame = generateWorld();
        showWorld(finalWorldFrame);
        finalWorldFrame = play(finalWorldFrame, input.substring(indexS + 1));
        showWorld(finalWorldFrame);
        return finalWorldFrame;
    }

    public void loadGame() {
        TETile[][] finalWorldFrame = getSavedGame();
        showWorld(finalWorldFrame);
        play(finalWorldFrame);
    }

    /** * @param SEED is a an argument * @return the loaded world */
    public TETile[][] loadGame(String input) {
        TETile[][] finalWorldFrame = getSavedGame();
        finalWorldFrame = play(finalWorldFrame, input.substring(1));
        showWorld(finalWorldFrame);
        return finalWorldFrame;
    }

    public void getSeed() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setFont(new Font("Monaco", Font.PLAIN, 50));
        StdDraw.text(WIDTH / 2, 3 * HEIGHT / 4, "Please enter a random seed:");
        StdDraw.show();
        String seedString = "";
        while (true) {
            StdDraw.clear(Color.BLACK);
            StdDraw.setFont(new Font("Monaco", Font.PLAIN, 50));
            StdDraw.text(WIDTH / 2, 3 * HEIGHT / 4, "Please enter a random seed:");

            char digit;
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            digit = Character.toLowerCase(StdDraw.nextKeyTyped());
            if (digit != 's') {
                if (!Character.isDigit(digit)) {
                    continue;
                }
                seedString += digit;
                StdDraw.setFont(new Font("Monaco", Font.PLAIN, 30));
                StdDraw.text(WIDTH / 2, HEIGHT / 2, seedString);
                StdDraw.show();
            } else {
                break;
            }
        }
        SEED = Long.parseLong(seedString);
        RANDOM = new Random(SEED);
    }

    /** * @param input is a String * @return String to int */
    public long getSeed(String input) {
        int indexS = input.indexOf('s');
        String stringSEED = input.substring(1, indexS);
        SEED = Long.parseLong(stringSEED);
        RANDOM = new Random(SEED);
        return Long.parseLong(stringSEED);
    }

    /** * @param SEED is a random number * @return the whole world */
    public TETile[][] generateWorld() {
        TETile[][] randomRoom = generateRandomRoom(50);
        TETile[][] linkedRoom = linkedRandomRoom(randomRoom);
        TETile[][] worldWithRoom = generateDoor(linkedRoom);
        TETile[][] finalWorldFrame = generatePlayer(worldWithRoom);
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
    public TETile[][] linkedRandomRoom(TETile[][] randomRoom) {
        TETile[][] linkedRandomRoom = new TETile[WIDTH][HEIGHT];
        Rectangle mainRoom = existRects.remove(existRects.size() - 1);
        int mainX = mainRoom.getRoomX(), mainY = mainRoom.getRoomY();
        existRects.sort(Comparator.comparing(o -> (o.left * o.left + o.bottom * o.bottom)));
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
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(finalWorldFrame);
    }

    public TETile[][] generateDoor(TETile[][] linkedRandomRoom) {
        while (true) {
            int x = RANDOM.nextInt(WIDTH - 2) + 1;
            int y = RANDOM.nextInt(HEIGHT - 2) + 1;
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

    public TETile[][] generatePlayer(TETile[][] worldWithRoom) {
        while (true) {
            int x = RANDOM.nextInt(WIDTH - 2) + 1;
            int y = RANDOM.nextInt(HEIGHT - 2) + 1;
            if (worldWithRoom[x][y] == Tileset.FLOOR) {
                worldWithRoom[x][y] = Tileset.PLAYER;
                player.playerX = x;
                player.playerY = y;

                break;
            }
        }
        return worldWithRoom;
    }

    public void play(TETile[][] finalWorldFrame) {
        while (true) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char c = Character.toLowerCase(StdDraw.nextKeyTyped());
            switch (c) {
                case 'a':
                    finalWorldFrame = player.walkLeft(finalWorldFrame);
                    ter.renderFrame(finalWorldFrame);
                    break;
                case 'd':
                    finalWorldFrame = player.walkRight(finalWorldFrame);
                    ter.renderFrame(finalWorldFrame);
                    break;
                case 's':
                    finalWorldFrame = player.walkBottom(finalWorldFrame);
                    ter.renderFrame(finalWorldFrame);
                    break;
                case 'w':
                    finalWorldFrame = player.walkTop(finalWorldFrame);
                    ter.renderFrame(finalWorldFrame);
                    break;
                case ':':
                    while (true) {
                        if (!StdDraw.hasNextKeyTyped()) {
                            continue;
                        }
                        if (Character.toLowerCase(StdDraw.nextKeyTyped()) == 'q') {
                            saveGame(finalWorldFrame);
                            System.exit(0);
                        } else {
                            break;
                        }
                    }
                    break;
                default:
            }
        }
    }

    public TETile[][] play(TETile[][] finalWorldFrame, String order) {
        for (int i = 0; i < order.length(); i++) {
            char c = order.charAt(i);
            switch (c) {
                case 'a':
                    finalWorldFrame = player.walkLeft(finalWorldFrame);
                    break;
                case 'd':
                    finalWorldFrame = player.walkRight(finalWorldFrame);
                    break;
                case 's':
                    finalWorldFrame = player.walkBottom(finalWorldFrame);
                    break;
                case 'w':
                    finalWorldFrame = player.walkTop(finalWorldFrame);
                    break;
                case ':':
                    if (i + 1 < order.length() && order.charAt(i + 1) == 'q') {
                        saveGame(finalWorldFrame);
                    }
                    break;
                default:
            }
        }
        return finalWorldFrame;
    }

    private void saveGame(TETile[][] finalWorldFrame) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("savefile.txt"));
            out.writeObject(finalWorldFrame);
            out.writeObject(Player.playerX);
            out.writeObject(Player.playerY);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private TETile[][] getSavedGame() {
        TETile[][] finalWorldFrame = null;
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("savefile.txt"));
            finalWorldFrame = (TETile[][]) in.readObject();
            Player.playerX = ((int) in.readObject());
            Player.playerY = ((int) in.readObject());
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return finalWorldFrame;
    }

    private void drawStartUI() {
        initializeCanvas();

        Font font = new Font("Monaco", Font.PLAIN, 60);
        StdDraw.setFont(font);
        StdDraw.text(WIDTH / 2, 3 * HEIGHT / 4, "CS61B: THE GAME");

        Font smallFont = new Font("Monaco", Font.PLAIN, 30);
        StdDraw.setFont(smallFont);
        StdDraw.text(WIDTH / 2, HEIGHT / 4 + 2, "New Game (N)");
        StdDraw.text(WIDTH / 2, HEIGHT / 4, "Load Game (L)");
        StdDraw.text(WIDTH / 2, HEIGHT / 4 - 2, "Quit (Q)");

        StdDraw.show();
    }

    private void initializeCanvas() {
        StdDraw.setCanvasSize(WIDTH * 16, (HEIGHT + 1) * 16);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT + 1);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenColor(Color.WHITE);
    }
}
