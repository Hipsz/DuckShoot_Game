package model;

public enum DuckType {
    YELLOW(1, 2.0, -0.8),
    RED(2, 2.5, 0.9),
    PURPLE(3, 3.0, 0.4),
    PINK(4, 3.5, 0.6);

    private final int shootsNeededToKill;
    private final double horizontalStepSize;
    private final double imageEffectHue;

    DuckType(int shoots, double step, double hue) {
        this.shootsNeededToKill = shoots;
        this.horizontalStepSize = step;
        this.imageEffectHue = hue;
    }

    public int getShootsNeededToKill() {
        return shootsNeededToKill;
    }

    public double getImageEffectHue() {
        return imageEffectHue;
    }

    public double getHorizontalStepSize() {
        return horizontalStepSize;
    }
}
