import java.lang.Math;

public class Planet {
    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;

    private static final double G = 6.67e-11;

    public Planet(double xP, double yP, double xV, double yV, double m, String img) {
        xxPos = xP;
        yyPos = yP;
        xxVel = xV;
        yyVel = yV;
        ;
        mass = m;
        imgFileName = img;
    }

    public Planet(Planet p) {
        xxPos = p.xxPos;
        yyPos = p.yyPos;
        xxVel = p.xxVel;
        yyVel = p.yyVel;
        ;
        mass = p.mass;
        imgFileName = p.imgFileName;
    }

    /**
     * * calcDistance
     */
    public double calcDistance(Planet p) {
        double dx = this.xxPos - p.xxPos;
        double dy = this.yyPos - p.yyPos;
        double r = Math.sqrt(dx * dx + dy * dy);
        return r;
    }

    /**
     * * calcForceExertedBy
     */
    public double calcForceExertedBy(Planet p) {
        double F = (G * this.mass * p.mass) / (this.calcDistance(p) * this.calcDistance(p));
        return F;
    }

    /**
     * * calcForceExertedByX and calcForceExertedByY
     */
    public double calcForceExertedByX(Planet p) {
        double Fx = (this.calcForceExertedBy(p)) * (p.xxPos - this.xxPos)
                / (this.calcDistance(p));
        return Fx;
    }

    public double calcForceExertedByY(Planet p) {
        double Fy = (this.calcForceExertedBy(p)) * (p.yyPos - this.yyPos)
                / (this.calcDistance(p));
        return Fy;
    }

    /**
     * * calcNetForceExertedByX and calcNetForceExertedByY
     */
    public double calcNetForceExertedByX(Planet[] array_p) {
        double Fx = 0;
        double F;
        for (int i = 0; i < array_p.length; i++) {
            if (array_p[i].equals(this)) {
                continue;
            }
            F = (this.calcForceExertedBy(array_p[i])) * (array_p[i].xxPos - this.xxPos)
                    / (this.calcDistance(array_p[i]));
            Fx += F;
        }
        return Fx;
    }

    public double calcNetForceExertedByY(Planet[] array_p) {
        double Fy = 0;
        double F;
        for (int i = 0; i < array_p.length; i++) {
            if (array_p[i].equals(this)) {
                continue;
            }
            F = (this.calcForceExertedBy(array_p[i])) * (array_p[i].yyPos - this.yyPos)
                    / (this.calcDistance(array_p[i]));
            F += F;
        }
        return Fy;
    }

    /**
     * * update
     */
    public void update(double dt, double Fx, double Fy) {
        double ax = Fx / this.mass;
        double ay = Fy / this.mass;
        this.xxVel = this.xxVel + ax * dt;
        this.yyVel = this.yyVel + ay * dt;
        this.xxPos = this.xxPos + this.xxVel * dt;
        this.yyPos = this.yyPos + this.yyVel * dt;
    }

    /**
     * * Drawing One Planet
     */
    public void draw() {
        StdDraw.picture(this.xxPos, this.yyPos, "./images/"+imgFileName);
    }
}