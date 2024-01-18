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
        setWorldSize(10);

        wall1 = new Wall(new Vector3d(-1,0,-3), 12, 1, this);
        wall1.rotate90(1);
        wall2 = new Wall(new Vector3d(1,0,3), 4,1, this);
        wall3 = new Wall(new Vector3d(3,0,5), 4,1, this);
        wall3.rotate90(1);

        wall4 = new Wall(new Vector3d(1, 0, 7), 4,1, this);

        //TODO: Try a layout with a Π shape
        add(wall1);
        add(wall2);
        //add(wall3);
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