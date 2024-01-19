package com.company;

import simbad.sim.Agent;
import simbad.sim.LightSensor;
import simbad.sim.RangeSensorBelt;

import java.lang.Math;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class IBugMain {
    RangeSensorBelt bumpers, sonars;
    LightSensor l, r, m;
    double intensityL, intensityH, intensityLeft, intensityRight, intensityMiddle, K1 = 5, K2 = 0.8, K3 = 1, SAFETY = 1;
    Agent myRobot;
    boolean CLOCKWISE;
    public enum robotState {
        MoveToGoal, CircumNavigate, GoalReached, Start
    }
    private robotState state;


    public IBugMain(Agent myRobot, RangeSensorBelt sonars, RangeSensorBelt bumpers, LightSensor l, LightSensor r, LightSensor m) {
        this.myRobot = myRobot;
        this.bumpers = bumpers;
        this.sonars = sonars;
        this.l = l;
        this.r = r;
        this.m = m;
        state = robotState.Start;
        this.CLOCKWISE = false;
    }

    private void moveToGoal() {
        myRobot.setRotationalVelocity(Math.signum(intensityRight - intensityLeft)); // v_orientation
        myRobot.setTranslationalVelocity(2);
    }

    private void circumNavigate() {
        int min;
        min = 0;
        for (int i = 1; i < sonars.getNumSensors(); i++)
            if (sonars.getMeasurement(i) < sonars.getMeasurement(min))
                min = i;
        Point3d p = Methods.getSensedPoint(min, sonars, myRobot);
        double d = p.distance(new Point3d(0, 0, 0));
        Vector3d v;
        v = CLOCKWISE ? new Vector3d(-p.z, 0, p.x) : new Vector3d(p.z, 0, -p.x);
        double phLin = Math.atan2(v.z, v.x);
        double phRot = Math.atan(K3 * (d - SAFETY));
        if (CLOCKWISE)
            phRot = -phRot;
        double phRef = Methods.wrapToPi(phLin + phRot);

        myRobot.setRotationalVelocity(K1 * phRef);
        myRobot.setTranslationalVelocity(K2 * Math.cos(phRef));
    }

    //TODO: handle case where light source directly behind (works fine for the other cases)

    public void step() {
        double minDist = 2 * SAFETY;
        intensityMiddle = m.getLux();
        intensityLeft = l.getLux();
        intensityRight = r.getLux();  // right-left -> orientation to target left and right have same value, greater than middle
        intensityL = intensityLeft;

        if (intensityLeft >= 0.06) {
            state = robotState.GoalReached;
        }

        if(state==robotState.Start){
            if(intensityMiddle>intensityLeft && intensityMiddle>intensityRight) {
                if (!CLOCKWISE)
                    myRobot.rotateY(1);
                else
                    myRobot.rotateY(-1);
            }
            state = robotState.MoveToGoal;
        }

        if (state == robotState.MoveToGoal) {
            for (int i = 0; i < sonars.getNumSensors(); i++) {
                double ph = Methods.wrapToPi(sonars.getSensorAngle(i));
                if (Math.abs(ph) <= Math.PI / 2)
                    minDist = Math.min(minDist, sonars.getMeasurement(i));
            }
            if (minDist > SAFETY) {
                moveToGoal();
            } else {
                state = robotState.CircumNavigate;
                intensityH = intensityRight;
            }
        }
        if (state == robotState.GoalReached) {
            myRobot.setRotationalVelocity(0);
            myRobot.setTranslationalVelocity(0);
        }

        if (intensityL != intensityRight) {
            intensityH = intensityRight;
        }

        if (state == robotState.CircumNavigate) {
            circumNavigate();
            if (intensityRight > intensityH)
                state = robotState.MoveToGoal;

            // we are moving counter clockwise, left lightsensor should have higher value, if circumnavigating but right sensor has higher value than left, means
            // we are going away from the goal so no need to keep circumnavigating and therefore proceed to goal
            if (intensityRight > intensityLeft)
                state = robotState.MoveToGoal;

        }
    }
}