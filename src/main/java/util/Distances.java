package util;

import java.lang.Math;

public class Distances {

    public static int euclidian(int x1, int y1, int x2, int y2) {
        return (x2-x1)*(x2-x1) + (y2-y1)*(y2-y1);
    }

    public static int manhattan(int x1, int y1, int x2, int y2) {
        return Math.abs(x2-x1) + Math.abs(y2-y1);
    }

    public static int numberOfSteps(int x1, int y1, int x2, int y2) {
        return Math.max(Math.abs(x2-x1), Math.abs(y2-y1));
    }

}