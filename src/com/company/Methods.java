package com.company;

import simbad.sim.Agent;
import simbad.sim.RangeSensorBelt;

import javax.vecmath.Point3d;

public class Methods {

    public static Point3d getSensedPoint(int sonar, RangeSensorBelt sonars, Agent myRobot) {

        double v;
        if (sonars.hasHit(sonar))
            v = myRobot.getRadius() + sonars.getMeasurement(sonar);
        else
            v = myRobot.getRadius() + sonars.getMaxRange();
        double x = v * Math.cos(sonars.getSensorAngle(sonar));
        double z = v * Math.sin(sonars.getSensorAngle(sonar));
        return new Point3d(x, 0, z);
    }

    public static double wrapToPi(double a) {
        if (a > Math.PI)
            return a - Math.PI * 2;
        if (a <= -Math.PI)
            return a + Math.PI * 2;
        return a;
    }
}
