package com.company;

import simbad.sim.Agent;
import simbad.sim.LightSensor;
import simbad.sim.RangeSensorBelt;
import simbad.sim.RobotFactory;
import java.lang.Math;

import javax.vecmath.Vector3d;

public class MyRobot extends Agent {
    RangeSensorBelt bumpers, sonars;
    LightSensor l, r, m;
    double intensityL, intensityH, left, right, middle;

    public MyRobot (Vector3d position, String name) {
        super(position,name);
        bumpers = RobotFactory.addBumperBeltSensor(this, 1);
        sonars = RobotFactory.addSonarBeltSensor(this, 4);
        m = RobotFactory.addLightSensor(this);
        l = RobotFactory.addLightSensor(this, new Vector3d(0.18, 0.25, 0.18), 1, "left");
        r = RobotFactory.addLightSensor(this, new Vector3d(0.18, 0.25, -0.18), 1, "right");

    }
    private void rotate90Degrees(String direction) {
        // Rotate the robot by 90 degrees
        if(direction.equals("right")) {
            setRotationalVelocity(-Math.PI / 2.0);
        }
        else if(direction.equals("left")){
            setRotationalVelocity(Math.PI/2.0);
        }
        setRotationalVelocity(0);
    }

    public void initBehavior() {
        setTranslationalVelocity(0.5);
        setRotationalVelocity(0.3);
    }

    public void performBehavior()
    {
        //TODO: add bumpers or sonars and implement collision avoidance based on i-Bug

        intensityL = left;
        middle = m.getLux();
        left = l.getLux();
        right = r.getLux();  // right-left -> orientation to target left and right have same value, greater than middle
        setRotationalVelocity(Math.signum(right-left)); // v_orientation
        setTranslationalVelocity(0);
        setTranslationalVelocity(2); // v_forward

        if(bumpers.hasHit(0)){
            intensityH = left;
            setTranslationalVelocity(-1);
            rotate90Degrees("right");

            if(sonars.hasHit(3)){
                setTranslationalVelocity(2);
            }
            else{
                if(left>intensityH) {
                    setRotationalVelocity(0);
                    setTranslationalVelocity(0);
                }
                else{
                        rotate90Degrees("left");
                        setTranslationalVelocity(2);
                    }
                }
            }






        /*//i-Bug

        intensityL = left;

        if(right >= 0.0329) { // 0.0329 was decided via trial and error
            setTranslationalVelocity(0);
            setRotationalVelocity(0);
        }

        setRotationalVelocity(Math.signum(right - left)); // v_orientation
        setTranslationalVelocity(2);


        if (sonars.hasHit(0)) {
            setTranslationalVelocity(-1);
            intensityH = left;
            setTranslationalVelocity(0);
            rotate90Degrees();
            setTranslationalVelocity(2);
        }*/






    }
}
