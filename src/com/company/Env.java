package com.company;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import simbad.sim.*;

import java.awt.*;

public class Env extends EnvironmentDescription {
    public Color3f white = new Color3f(Color.white);
    public Color3f lightGray = new Color3f(Color.lightGray);
    public Color3f yellow = new Color3f(Color.yellow);
    public Color3f magenta = new Color3f(Color.MAGENTA);

    public Wall wall1, wall2, wall3, wall4;

    Env(){
        wall1 = new Wall(new Vector3d(5,0,4), 4,1, this);
        wall2 = new Wall(new Vector3d(3,0,2.1), 4,1, this);
        wall2.rotate90(1);
        wall3 = new Wall(new Vector3d(0,0,-5), 5, 4, this);
        wall3.rotate90(1);
        wall3.setColor(yellow);
        wall4 = new Wall(new Vector3d(0,0,7), 4,1, this);
        wall4.setColor(magenta);
        wall4.rotateY(-0.5);
        wall4.rotate90(1);

        add(new CherryAgent(new Vector3d(7,0,7),"cherry",0.1f));
        add(new MyRobot(new Vector3d(-7, 0, -7), "robot 1"));
        add(new Arch(new Vector3d(-5,0,-5), this));
        add(new Box(new Vector3d(-3,0,-2), new Vector3f(1,1,1),this));
        add(new Wall(new Vector3d(-5, 0, 5), 4,2, this));
        add(wall1);
        add(wall2);
        add(wall3);
        add(wall4);

        light1SetPosition(7, 2, 7);
        light1IsOn = false;
        light2SetPosition(7, 2, 7);
        light2IsOn = true;
        setWorldSize(20);
        showAxis(true);
        ambientLightColor = white;
        backgroundColor = lightGray;

    }
}