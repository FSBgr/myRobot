package com.company;

import simbad.sim.Agent;
import simbad.sim.LightSensor;
import simbad.sim.RobotFactory;
import java.lang.Math;

import javax.vecmath.Vector3d;

public class MyRobot extends Agent {
    LightSensor l, r, m;

    double left, right, middle;
    public MyRobot (Vector3d position, String name) {
        super(position,name);
        m = RobotFactory.addLightSensor(this);
        l = RobotFactory.addLightSensor(this, new Vector3d(0.18, 0.25, 0.18), 1, "left");
        r = RobotFactory.addLightSensor(this, new Vector3d(0.18, 0.25, -0.18), 1, "right");

    }
    public void initBehavior() {
        setTranslationalVelocity(0.5);
        setRotationalVelocity(0.3);
    }
    public void performBehavior()
    {
        //TODO: add bumpers or sonars and implement collision avoidance based on i-Bug

        middle = m.getLux();
        left = l.getLux();
        right = r.getLux();
        setTranslationalVelocity(1000*middle);
        setRotationalVelocity(Math.signum(right-left));
    }
}
