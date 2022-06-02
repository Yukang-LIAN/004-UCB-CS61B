package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Player {
    public static int playerX;
    public static int playerY;


    public TETile[][] walkLeft(TETile[][] world){
        if(world[playerX-1][playerY]==Tileset.FLOOR){
            world[playerX][playerY]=Tileset.FLOOR;
            playerX--;
            world[playerX][playerY]=Tileset.PLAYER;
        }
        return world;
    }

    public TETile[][] walkRight(TETile[][] world){
        if(world[playerX+1][playerY]==Tileset.FLOOR){
            world[playerX][playerY]=Tileset.FLOOR;
            playerX++;
            world[playerX][playerY]=Tileset.PLAYER;
        }
        return world;
    }

    public TETile[][] walkBottom(TETile[][] world){
        if(world[playerX][playerY-1]==Tileset.FLOOR){
            world[playerX][playerY]=Tileset.FLOOR;
            playerY--;
            world[playerX][playerY]=Tileset.PLAYER;
        }
        return world;
    }

    public TETile[][] walkTop(TETile[][] world){
        if(world[playerX][playerY+1]==Tileset.FLOOR){
            world[playerX][playerY]=Tileset.FLOOR;
            playerX++;
            world[playerX][playerY]=Tileset.PLAYER;
        }
        return world;
    }
}
