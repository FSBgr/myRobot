package com.company;

import simbad.sim.Agent;
import simbad.sim.RangeSensorBelt;

import javax.vecmath.Point3d;

public class Methods {

    /**
     * Calculates and returns a 3D point representing the sensed position of an object using sonar measurements.
     * The method considers the distance measured by a specific sonar sensor on a robot and computes the corresponding
     * x, y, and z coordinates in a 3D space.
     *
     * @param sonar: the sonar which found the obstacle
     * @param sonars: the whole sonar belt
     * @param myRobot: the robot performing the task
     * @return: The coordinates of the point where the sonar detected an obstacle
     */
    public static Point3d getCollisionPoint(int sonar, RangeSensorBelt sonars, Agent myRobot) {
        // Calculate the distance to the sensed point based on sonar measurements
        double v, x, z;
        if (sonars.hasHit(sonar))
            v = myRobot.getRadius() + sonars.getMeasurement(sonar);
        else
            v = myRobot.getRadius() + sonars.getMaxRange();
        // Calculate x and z coordinates in a 3D space based on the sensor angle
        x = v * Math.cos(sonars.getSensorAngle(sonar));
        z = v * Math.sin(sonars.getSensorAngle(sonar));
        // Return a Point3d object representing the calculated 3D point
        return new Point3d(x, 0, z);
    }

    /**
     * Wraps an angle to the interval [-π, π] to ensure it falls within the standard periodicity of angles.
     * If the input angle is greater than π, it subtracts 2π until it falls within the interval.
     * If the input angle is less than or equal to -π, it adds 2π until it falls within the interval.
     *
     * @param angle: the angle to be converted
     * @return: the angle represented in radians to the interval [-π, π)
     */
    public static double wrapToPi(double angle) {
        // If angle is greater than π, subtract 2π until it falls within the interval
        if (angle > Math.PI)
            return angle - Math.PI * 2;
        // If angle is less than or equal to -π, add 2π until it falls within the interval
        if (angle <= -Math.PI)
            return angle + Math.PI * 2;
        // If angle is already within the interval [-π, π], return it unchanged
        return angle;
    }
}
