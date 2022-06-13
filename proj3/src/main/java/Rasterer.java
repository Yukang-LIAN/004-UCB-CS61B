import java.util.HashMap;
import java.util.Map;

import static java.awt.image.ImageObserver.HEIGHT;
import static java.awt.image.ImageObserver.WIDTH;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    private static final double ULLAT = MapServer.ROOT_ULLAT;
    private static final double ULLON = MapServer.ROOT_ULLON;
    private static final double LRLAT = MapServer.ROOT_LRLAT;
    private static final double LRLON = MapServer.ROOT_LRLON;
    private static final double TILE_SIZE = MapServer.TILE_SIZE;
    private static final double LON_WIDTH = Math.abs(MapServer.ROOT_ULLON - MapServer.ROOT_LRLON);
    private static final double LAT_HEIGHT = Math.abs(MapServer.ROOT_ULLAT - MapServer.ROOT_LRLAT);

    public Rasterer() {
        // YOUR CODE HERE
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     * <p>
     * The grid of images must obey the following properties, where image in the
     * is referred to as a "tile".
     * <ul>
     *     <li>The tiles collected must cover the most longitudinal distance per pixel
     *     (LonDPP) possible, while still covering less than or equal to the amount of
     *     longitudinal distance per pixel in the query box for the user viewport size. </li>
     *     <li>Contains all tiles that intersect the query bounding box that fulfill the
     *     above condition.</li>
     *     <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     * </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     * forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        // System.out.println(params);
        Map<String, Object> results = new HashMap<>();
        double lrlon = params.get("lrlon");
        double lrlat = params.get("lrlat");
        double ullon = params.get("ullon");
        double ullat = params.get("ullat");
        double width = params.get("w");
        double height = params.get("h");

        if (lrlon < ullon || ullat < lrlat || lrlon <= ULLON || lrlat >= ULLAT || ullon >= LRLON || ullat <= LRLAT) {
            results.put("query_success", false);
            results.put("render_grid", null);
            results.put("raster_ul_lon", 0);
            results.put("raster_ul_lat", 0);
            results.put("raster_lr_lon", 0);
            results.put("raster_lr_lat", 0);
            results.put("raster_depth", 0);
            return results;
        }

        double requiredLonDpp = (lrlon - ullon) / width;
        int k = getDepth(requiredLonDpp);
        results.put("depth", k);

        double pictureWidth = LON_WIDTH / Math.pow(2, k);
        double pictureHeight = LAT_HEIGHT / Math.pow(2, k);

        int widthMin = (int) (Math.floor((ullon - ULLON) / pictureWidth));
        int widthMax = (int) (Math.floor((lrlon - ULLON) / pictureWidth));
        int heightMin = (int) (Math.floor((ULLAT - ullat) / pictureHeight));
        int heightMax = (int) (Math.floor((ULLAT - lrlat) / pictureHeight));

        double left = ULLON + widthMin * pictureWidth;
        double right = ULLON + (widthMax + 1) * pictureWidth;
        double top = ULLAT - heightMin * pictureHeight;
        double bottom = ULLAT - (heightMax + 1) * pictureHeight;

        if (ullon < ULLON) {
            widthMin = 0;
            left = ULLON;
        }
        if (lrlon > LRLON) {
            widthMax = (int) Math.pow(2, k) - 1;
            right = LRLON;
        }
        if (ullat > ULLAT) {
            heightMin = 0;
            top = ULLAT;
        }
        if (lrlat < LRLAT) {
            heightMax = (int) Math.pow(2, k) - 1;
            bottom = LRLAT;
        }

        String[][] files = new String[heightMax - heightMin + 1][widthMax - widthMin + 1];
        for (int i = heightMin; i <= heightMax; i++) {
            for (int j = widthMin; j <= widthMax; j++) {
                files[i - heightMin][j - widthMin] = "d" + k + "_x" + j + "_y" + i + ".png";
            }
        }

        results.put("query_success", true);
        results.put("render_grid", files);
        results.put("raster_ul_lon", left);
        results.put("raster_ul_lat", top);
        results.put("raster_lr_lon", right);
        results.put("raster_lr_lat", bottom);
        results.put("depth", k);

        return results;
    }

    private int getDepth(double requiredLonDpp) {
        int k = 0;
        double LonDpp = (LRLON - ULLON) / TILE_SIZE;

        while (requiredLonDpp < LonDpp) {
            LonDpp = LonDpp / 2;
            k++;
        }

        k = Math.min(k, 7);
        return k;
    }

}
