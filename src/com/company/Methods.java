package com.company;

import simbad.sim.Agent;
import simbad.sim.RangeSensorBelt;

import javax.vecmath.Point3d;

public class Methods {

    /**
     *
     * @param sonar: the sonar which found the obstacle
     * @param sonars: the whole sonar belt
     * @param myRobot: the robot performing the task
     * @return: The coordinates of the point where the sonar detected an obstacle
     */
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

    /**
     *
     * @param angle: the angle to be converted
     * @return: the angle represented in radians to the interval [-π, π)
     */
    public static double wrapToPi(double angle) {
        if (angle > Math.PI)
            return angle - Math.PI * 2;
        if (angle <= -Math.PI)
            return angle + Math.PI * 2;
        return angle;
    }
}
