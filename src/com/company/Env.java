package com.company;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import simbad.sim.*;

import java.awt.*;

public class Env extends EnvironmentDescription {
    Color3f white = new Color3f(Color.white);
    Color3f lightGray = new Color3f(Color.lightGray);
    Wall wall1, wall2, wall3, wall4;

    Env(){
        setWorldSize(100);

        wall1 = new Wall(new Vector3d(1,0,4), 4,1, this);
        wall2 = new Wall(new Vector3d(3,0,5), 2,1, this);
        wall2.rotate90(1);
        wall3 = new Wall(new Vector3d(-1,0,-2.5), 13, 1, this);
        wall3.rotate90(1);
        wall4 = new Wall(new Vector3d(1, 0, 7), 4,1, this);

        //add(new Box(new Vector3d(-5,0,-5), new Vector3f(1,1,1), this));
        add(new Box(new Vector3d(-4,0,1), new Vector3f(1,1,1),this));
        add(new Box(new Vector3d(-5,0,4), new Vector3f(1,1,1),this));
        add(new Box(new Vector3d(-4,0,7), new Vector3f(1,1,1),this));
        add(wall1);
        add(wall2);
        add(wall3);
        //add(wall4);

        add(new CherryAgent(new Vector3d(7,0,7),"cherry",0.1f));
        add(new MyRobot(new Vector3d(-7, 0, -7), "robot 1"));

        light1SetPosition(-7, -6, -7);
        light1Color = new Color3f(Color.BLACK);
        light1IsOn = false;
        light2SetPosition(7, 2, 7);
        light2IsOn = true;
        setWorldSize(20);
        showAxis(true);
        ambientLightColor = white;
        backgroundColor = lightGray;

    }
}