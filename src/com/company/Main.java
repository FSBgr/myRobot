package com.company;
import simbad.gui.Simbad;

import javax.vecmath.Vector3d;

public class Main {
    public static void main(String[] args) {
        Vector3d bottomRight = new Vector3d(7, 0, 7);
        Vector3d topRight = new Vector3d(7, 0, -7);
        Vector3d topLeft = new Vector3d(-7, 0, -7);
        Vector3d bottomLeft = new Vector3d(-7, 0, 7);

        Simbad frame = new Simbad(new Env(1, 7,2,7, topLeft) ,false); }


    //TODO: add javadoc for fanciness
}