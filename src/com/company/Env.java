package com.company;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import simbad.sim.*;

import java.awt.*;
import java.util.Vector;

/**
 * @author Christos Christidis
 */
public class Env extends EnvironmentDescription {
    Color3f white = new Color3f(Color.white);
    Color3f lightGray = new Color3f(Color.lightGray);
    Wall wall1, wall2, wall3, wall4;

    Env(){

    }

    /**
     *
     * @param scenario: different wall/obstacle configurations are created based on this
     * @param lightX: light position X
     * @param lightY: light position Y
     * @param lightZ: light position Z
     * @param robotPosition: vector based on which the robot will be placed on the map
     */
    Env(int scenario, double lightX, double lightY, double lightZ, Vector3d robotPosition) {
        switch (scenario) {
            case 1:
                wallCreator();
                break;

            case 2:
                wallCreator();
                wall3 = new Wall(new Vector3d(3,0,5), 4,1, this);
                wall3.rotate90(1);
                add(wall3);
                break;

            case 3:
                wallCreator();
                wall3 = new Wall(new Vector3d(3,0,5), 4,1, this);
                wall3.rotate90(1);
                wall4 = new Wall(new Vector3d(1, 0, 7), 4,1, this);
                add(wall3);
                add(wall4);
                break;

        }
        add(new CherryAgent(new Vector3d(7,0,7),"cherry",0.1f));
        add(new MyRobot(robotPosition, "robot 1"));
        light1SetPosition(-7, -6, -7);
        light1Color = new Color3f(Color.BLACK);
        light1IsOn = false;
        light2SetPosition(lightX, lightY, lightZ);
        light2IsOn = true;
        setWorldSize(25);
        showAxis(true);
        ambientLightColor = white;
        backgroundColor = lightGray;
    }

    /**
     * Simple wall creator, makes the baseline environment on top of which the rest of the scenarios will be based on
     */
    private void wallCreator(){
        wall1 = new Wall(new Vector3d(-1,0,-3), 12, 1, this);
        wall1.rotate90(1);
        wall2 = new Wall(new Vector3d(1,0,3), 4,1, this);

        add(wall1);
        add(wall2);
    }

}