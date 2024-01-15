package com.company;
import javax.vecmath.Vector3d;
import simbad.sim.Agent;
import simbad.sim.LightSensor;
import simbad.sim.RobotFactory;

public class MyRobot extends Agent {
    LightSensor light;
    public MyRobot (Vector3d position, String name) {
        super(position,name);
        light = RobotFactory.addLightSensor(this);
    }
    public void initBehavior() {
        setTranslationalVelocity(0.5);
        setRotationalVelocity(0.3);
    }
    public void performBehavior()
    {
    }
}
