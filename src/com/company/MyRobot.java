package com.company;
import javax.vecmath.Vector3d;
import simbad.sim.Agent;
import simbad.sim.LightSensor;
import simbad.sim.RobotFactory;

public class MyRobot extends Agent {
    LightSensor left, right, middle ;
    public MyRobot (Vector3d position, String name) {
        super(position,name);
        middle = RobotFactory.addLightSensor(this);
        left = RobotFactory.addLightSensor(this, new Vector3d(0.18, 0.25, 0.18), 1, "left");
        right = RobotFactory.addLightSensor(this, new Vector3d(0.18, 0.25, -0.18), 1, "right");
        //left = RobotFactory.addLightSensorLeft(this);
        //right = RobotFactory.addLightSensorRight(this);

    }
    public void initBehavior() {
        setTranslationalVelocity(0.5);
        setRotationalVelocity(0.3);
    }
    public void performBehavior()
    {
    }
}
