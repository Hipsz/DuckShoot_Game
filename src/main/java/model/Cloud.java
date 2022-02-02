package model;

import javafx.geometry.Point2D;

public class Cloud extends MovableObject {

    private static final double STEP_SIZE = 4;

    public Cloud(boolean flipped) {
        super(flipped);
    }

    @Override
    public void animate() {
        // empty
    }

    @Override
    public Point2D getStepSize() {
        return new Point2D(STEP_SIZE, 0);
    }
}
