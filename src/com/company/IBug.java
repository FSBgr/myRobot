package com.company;

import simbad.sim.Agent;
import simbad.sim.LightSensor;
import simbad.sim.RangeSensorBelt;
import simbad.sim.RobotFactory;
import java.lang.Math;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class IBug {
    RangeSensorBelt bumpers, sonars;
    LightSensor l, r, m;
    double intensityL, intensityH, left, right, middle, trans=2, rot=2, SAFETY=0.8;
    Agent myRobot;
    boolean CLOCKWISE;

    public IBug(Agent myRobot, RangeSensorBelt sonars, RangeSensorBelt bumpers, LightSensor l, LightSensor r, LightSensor m){
        this.myRobot = myRobot;
        this.bumpers = bumpers;
        this.sonars = sonars;
        this.l=l;
        this.r=r;
        this.m=m;
    }

    private Point3d getSensedPoint(int sonar){

        double v;
        if (sonars.hasHit(sonar))
            v=myRobot.getRadius()+sonars.getMeasurement(sonar);
        else
            v=myRobot.getRadius()+sonars.getMaxRange();
        double x = v*Math.cos(sonars.getSensorAngle(sonar));
        double z = v*Math.sin(sonars.getSensorAngle(sonar));
        return new Point3d(x,0,z);
    }

    public static double wrapToPi(double a){
        if (a>Math.PI)
            return a-Math.PI*2;
        if (a<=-Math.PI)
            return a+Math.PI*2;
        return a;
    }

    private void moveToGoal(){
        myRobot.setRotationalVelocity(Math.signum(right-left)); // v_orientation
        myRobot.setTranslationalVelocity(2);
    }

    private void circumNavigate(){
        int min;
        min=0;
        for (int i=1;i<sonars.getNumSensors();i++)
            if (sonars.getMeasurement(i)<sonars.getMeasurement(min))
                min=i;
        Point3d p = getSensedPoint(min);
        double d = p.distance(new Point3d(0,0,0));
        Vector3d v;
        v = CLOCKWISE? new Vector3d(-p.z,0,p.x): new Vector3d(p.z,0,-p.x);
        double phLin = Math.atan2(v.z,v.x);
        double phRot =Math.atan(rot*(d-SAFETY));
        if (CLOCKWISE)
            phRot=-phRot;
        double phRef = wrapToPi(phLin+phRot);

        myRobot.setRotationalVelocity(rot*phRef);
        myRobot.setTranslationalVelocity(trans*Math.cos(phRef));
    }

    public void step(){
        middle = m.getLux();
        left = l.getLux();
        right = r.getLux();  // right-left -> orientation to target left and right have same value, greater than middle

        intensityL = left;
        moveToGoal();
        /*if(intensityL>=0.329){
            myRobot.setRotationalVelocity(0);
            myRobot.setTranslationalVelocity(0);
        }

        myRobot.setRotationalVelocity(Math.signum(right-left)); // v_orientation
        myRobot.setTranslationalVelocity(2);
*/


    }

}
