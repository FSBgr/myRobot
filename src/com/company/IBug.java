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
    double intensityL, intensityH, left, right, middle, K1=5, K2=0.8, K3=1, SAFETY=1;
    Agent myRobot;
    boolean CLOCKWISE;
    public enum robotState {
        MoveToGoal, CircumNavigate, GoalReached
    }
    private robotState state;
    int temp=0;

    public IBug(Agent myRobot, RangeSensorBelt sonars, RangeSensorBelt bumpers, LightSensor l, LightSensor r, LightSensor m){
        this.myRobot = myRobot;
        this.bumpers = bumpers;
        this.sonars = sonars;
        this.l=l;
        this.r=r;
        this.m=m;
        state=robotState.MoveToGoal;
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
        double phRot =Math.atan(K3*(d-SAFETY));
        if (CLOCKWISE)
            phRot=-phRot;
        double phRef = wrapToPi(phLin+phRot);

        myRobot.setRotationalVelocity(K1*phRef);
        myRobot.setTranslationalVelocity(K2*Math.cos(phRef));
    }

    public void step(){
        double minDist=2*SAFETY;
        middle = m.getLux();
        left = l.getLux();
        right = r.getLux();  // right-left -> orientation to target left and right have same value, greater than middle

        System.out.println(sonars.getMeasurement(0) +" "+ sonars.getMeasurement(1) +" "+ sonars.getMeasurement(2) +" "+ sonars.getMeasurement(3));

        intensityL = left;

        if(left>=0.0329){
            state= robotState.GoalReached;
        }

        if (state==robotState.GoalReached){
            myRobot.setRotationalVelocity(0);
            myRobot.setTranslationalVelocity(0);
        }
        if (state==robotState.MoveToGoal)        {
            for (int i=0;i<sonars.getNumSensors();i++)       {
                double ph = wrapToPi(sonars.getSensorAngle(i));
                if (Math.abs(ph/*-f2g*/)<=Math.PI/2)
                    minDist=Math.min(minDist,sonars.getMeasurement(i));
            }
            if (minDist>SAFETY) {
                moveToGoal();
                //System.out.println("I got in here");
            }
            else {
                state=robotState.CircumNavigate;
                intensityH = left;
                //sp = null;
                //circle=false;
            }
        }
        if (state==robotState.CircumNavigate) {
            if (intensityH < left) {
                circumNavigate();
            }
            if (intensityH>intensityL){
                myRobot.setRotationalVelocity(Math.signum(right-left));

                for(int i=0; i<sonars.getNumSensors(); i++){
                    if(sonars.getMeasurement(i)>=SAFETY){
                        temp++;
                    }
                }
                System.out.println("temp is: " + temp);
                if(temp==4)
                    state = robotState.MoveToGoal;

                //if(sonars.getMeasurement(0)==1.5){
                    //state = robotState.MoveToGoal;
                //}
            }
        }
            /*else            {
                if (cp.distance(sp)>ZERO && !circle)
                    circle=true;
                if (cp.distance(sp)<=ZERO && circle)
                    SimpleBehaviors.stop(rob);
                else{
                    double f= Tools.getGlobalAngleToGoal(rob,goal);
                    if (Math.abs(f-AngleToGoal)<0.05 && sp.distance(goal)>cp.distance(goal))
                        state = robotState.MoveToGoal;
                    else
                        SimpleBehaviors.circumNavigate(rob,sonars,CLOCKWISE);
                }*/



    }

}
