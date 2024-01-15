// Env.java
package com.company;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import simbad.sim.*;

import java.awt.*;

public class Env extends EnvironmentDescription {
    public Color3f white = new Color3f(Color.white);
    public Color3f ligthgray = new Color3f(Color.lightGray);
    /*public Color3f ambientLightColor = new Color3f(Color);*/
   /*public Color3f gray;
    public Color3f darkgray;
    public Color3f black;
    public Color3f red;
    public Color3f green;
    public Color3f blue;*/

    Env(){
        add(new CherryAgent(new Vector3d(7,0,7),"cherry",0.1f));
        add(new MyRobot(new Vector3d(-7, 0, -7), "robot 1"));
        add(new Wall(new Vector3d(5,0,4), 3, 1, this));
        add(new Arch(new Vector3d(-3,0,-5), this));
        add(new Box(new Vector3d(-3,0,-2), new Vector3f(1,1,1),this));
        light1SetPosition(7, 2, 7);
        light1IsOn = false;
        light2SetPosition(7, 2, 7);
        light2IsOn = true;
        setWorldSize(20);
        showAxis(true);
        ambientLightColor = white;
        backgroundColor = ligthgray;

    }
}