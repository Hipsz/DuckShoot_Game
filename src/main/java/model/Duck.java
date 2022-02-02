package model;

import javafx.geometry.Point2D;

public class Duck extends MovableObject {

    private static final double DUCK_FALLING_DOWN_STEP = 10;
    private static final double DUCK_FALLING_ROTATION_STEP = 20;

    private final DuckType type;
    private int hitsCount = 0;
    private boolean killed = false;

    public Duck(DuckType type, boolean flipped) {
        super(flipped);
        this.type = type;
    }

    @Override
    public void animate() {
        if (killed) {
            imageView.setRotate(imageView.getRotate() + (flipped ? -DUCK_FALLING_ROTATION_STEP : DUCK_FALLING_ROTATION_STEP));
        }
    }

    @Override
    public Point2D getStepSize() {
        return new Point2D(type.getHorizontalStepSize(), killed ? DUCK_FALLING_DOWN_STEP : 0);
    }

    public DuckType getType() {
        return type;
    }

    public void hit() {
        if (!killed) {
            hitsCount++;
            System.out.println("Duck hitted: " + hitsCount + " / " + type.getShootsNeededToKill());
            if (hitsCount >= type.getShootsNeededToKill()) {
                killed = true;
            }
        }
    }

    public boolean isKilled() {
        return killed;
    }
}
