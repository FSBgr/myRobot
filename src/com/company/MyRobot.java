package com.company;

import simbad.sim.Agent;
import simbad.sim.LightSensor;
import simbad.sim.RangeSensorBelt;
import simbad.sim.RobotFactory;

import javax.vecmath.Vector3d;

public class MyRobot extends Agent {
    RangeSensorBelt bumpers, sonars;
    LightSensor l, r, m;
    IBug iBug;

    public MyRobot (Vector3d position, String name) {
        super(position,name);
        bumpers = RobotFactory.addBumperBeltSensor(this, 1);
        sonars = RobotFactory.addSonarBeltSensor(this, 4);
        m = RobotFactory.addLightSensor(this);
        l = RobotFactory.addLightSensor(this, new Vector3d(0.18, 0.25, 0.18), 1, "left");
        r = RobotFactory.addLightSensor(this, new Vector3d(0.18, 0.25, -0.18), 1, "right");
        iBug = new IBug(this, sonars, bumpers, l,r,m);
    }

    public void initBehavior() {
    }

    public void performBehavior()
    {
        iBug.step();
    }
}
