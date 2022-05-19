public class NBody {
    /*
     * * ReadRadius
     */
    public static double readRadius(String filename) {
        In in = new In(filename);
        int number = in.readInt();
        double radius = in.readDouble();
        return radius;
    }

    /*
     * * ReadPlanets
     */
    public static Planet[] readPlanets(String fileName) {
        In in = new In(fileName);
        int number = in.readInt();
        in.readDouble();
        Planet[] planetList = new Planet[number];
        for (int i = 0; i < number; i++) {
            planetList[i] = new Planet(0, 0, 0, 0, 0, "");
            planetList[i].xxPos = in.readDouble();
            planetList[i].yyPos = in.readDouble();
            planetList[i].xxVel = in.readDouble();
            planetList[i].yyVel = in.readDouble();
            planetList[i].mass = in.readDouble();
            planetList[i].imgFileName = in.readString();
        }
        return planetList;
    }

    /*
     * * main
     */
    public static void main(String[] args) {

        /*
         * * Collecting All Needed Input
         */
        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String filename = args[2];
        Planet[] planetList = NBody.readPlanets(filename);
        double radius = NBody.readRadius(filename);

        /*
         * * Drawing the Background
         */
        StdDraw.setScale(-radius, radius);
        StdDraw.clear();
        StdDraw.picture(0, 0, "./images/starfield.jpg");

        /*
         * * Drawing All of the Planets
         */
        for (int i = 0; i < planetList.length; i++) {
            planetList[i].draw();
        }
        StdDraw.show();
    }

}