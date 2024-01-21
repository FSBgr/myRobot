package com.company;

import simbad.sim.Agent;
import simbad.sim.LightSensor;
import simbad.sim.RangeSensorBelt;

import java.lang.Math;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * @author Christidis Christos
 */


public class IBugMain {
    RangeSensorBelt sonars;
    LightSensor l, r, m;
    double intensityL, intensityH, intensityLeft, intensityRight, intensityMiddle;
    double goalSafetyDist=0.08,  K1 = 4, K2 = 0.8, K3 = 1, SAFETY = 1;
    Agent myRobot;
    boolean CLOCKWISE;
    public enum robotState {
        MoveToGoal, CircumNavigate, GoalReached, Start
    }
    private robotState state;


    /**
     *
     * @param myRobot: The robot performing the task
     * @param sonars: The whole sonar belt
     * @param l: left light sensor
     * @param r: right light sensor
     * @param m: middle/back light sensor
     */
    public IBugMain(Agent myRobot, RangeSensorBelt sonars, LightSensor l, LightSensor r, LightSensor m) {
        this.myRobot = myRobot;
        this.sonars = sonars;
        this.l = l;
        this.r = r;
        this.m = m;
        state = robotState.Start;
        this.CLOCKWISE = false;
    }

    /**
     * Set rotational velocity based on the difference of the two frontal light sensors and a fixed translational velocity
     * This ensures that the robot's movement will always be towards the light source
     */
    private void moveToGoal() {
        myRobot.setRotationalVelocity(Math.signum(intensityRight - intensityLeft)); // v_orientation
        myRobot.setTranslationalVelocity(2);
    }

    /**
     * Phew. Ok so. Implements obstacle avoidance behavior for a 3D robot using sonar sensor data.
     * The robot adjusts its velocities to navigate around obstacles while avoiding collisions.
     */
    private void circumNavigate() {
        int min;
        min = 0;

        // Find the sonar sensor with the minimum measurement
        for (int i = 1; i < sonars.getNumSensors(); i++)
            if (sonars.getMeasurement(i) < sonars.getMeasurement(min))
                min = i;

        // Retrieve the 3D position of the minimum sonar measurement
        Point3d p = Methods.getSensedPoint(min, sonars, myRobot);

        // Calculate the distance from the robot to the obstacle
        double d = p.distance(new Point3d(0, 0, 0));

        // Calculate a 3D vector perpendicular to the line connecting the robot to the obstacle
        Vector3d v;
        v = CLOCKWISE ? new Vector3d(-p.z, 0, p.x) : new Vector3d(p.z, 0, -p.x);

        // Calculate orientation angles
        double phLin = Math.atan2(v.z, v.x);
        double phRot = Math.atan(K3 * (d - SAFETY));

        // Adjust the sign of phRot based on the CLOCKWISE flag
        if (CLOCKWISE)
            phRot = -phRot;

        // Calculate the wrapped orientation angle
        double phRef = Methods.wrapToPi(phLin + phRot);

        // Set the robot's rotational velocity based on the wrapped orientation angle
        myRobot.setRotationalVelocity(K1 * phRef);
        // Set the robot's translational velocity based on the cosine of the wrapped orientation angle
        myRobot.setTranslationalVelocity(K2 * Math.cos(phRef));
    }

    /**
     * Performs a step in the robot's behavior, incorporating obstacle avoidance and navigation logic.
     * The method evaluates sensor data, adjusts the robot's orientation and velocity, and transitions between
     * different states based on the robot's proximity to the goal.
     */
    public void step() {
        double minDist = 2 * SAFETY;

        // Intensity values from light sensors
        intensityMiddle = m.getLux();
        intensityLeft = l.getLux();
        intensityRight = r.getLux();  // right-left -> orientation to target left and right have same value, greater than middle
        intensityL = intensityLeft;

        //Check if goal is reached
        if (intensityLeft >= goalSafetyDist) {
            state = robotState.GoalReached;
        }

        // State transitions and robot orientation adjustments based on initial state
        if(state==robotState.Start){
            if(intensityMiddle>intensityLeft && intensityMiddle>intensityRight) {
                if (!CLOCKWISE)
                    myRobot.rotateY(1);
                else
                    myRobot.rotateY(-1);
            }
            state = robotState.MoveToGoal;
        }

        // Move towards the goal or circumnavigate obstacles based on the robot's state
        if (state == robotState.MoveToGoal) {
            for (int i = 0; i < sonars.getNumSensors(); i++) {
                double ph = Methods.wrapToPi(sonars.getSensorAngle(i));
                if (Math.abs(ph) <= Math.PI / 2)
                    minDist = Math.min(minDist, sonars.getMeasurement(i));
            }

            // Decide whether to move towards the goal or circumnavigate obstacles
            if (minDist > SAFETY) {
                moveToGoal();
            } else {
                state = robotState.CircumNavigate;
                intensityH = intensityRight;
            }
        }
        // Stop the robot if the goal is reached
        if (state == robotState.GoalReached) {
            myRobot.setRotationalVelocity(0);
            myRobot.setTranslationalVelocity(0);
        }

        // Adjust intensity values
        if (intensityL != intensityRight) {
            intensityH = intensityRight;
        }

        if (state == robotState.CircumNavigate) {
            circumNavigate();
            if (intensityRight > intensityH)
                state = robotState.MoveToGoal;

            // Checking conditions for transitioning back to MoveToGoal state
            // We are moving counter clockwise, left light sensor should have higher value, if circumnavigating but
            // right sensor has higher value than left, means we are going away from the goal so no need to keep
            // circumnavigating and therefore proceed to goal. Works for counterclockwise rotation.
            if (intensityRight > intensityLeft)
                state = robotState.MoveToGoal;
        }
    }
}