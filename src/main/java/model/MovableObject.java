package model;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;

public abstract class MovableObject {
    protected Point2D position;
    protected final boolean flipped;
    protected ImageView imageView;

    public MovableObject(boolean flipped) {
        this.position = Point2D.ZERO;
        this.flipped = flipped;
    }

    public void move(double speedRatio) {
        Point2D stepSize = getStepSize().multiply(speedRatio);
        if (flipped) stepSize = new Point2D(-stepSize.getX(), stepSize.getY());
        position = position.add(stepSize);
    }

    abstract public void animate();

    public boolean isInsideBox(Bounds bounds) {
        Point2D size = getSize();
        Bounds objectBounds = new BoundingBox(
                position.getX(),
                position.getY(),
                size.getX(),
                size.getY());
        return bounds.intersects(objectBounds);
    }

    abstract public Point2D getStepSize();

    public boolean isFlipped() {
        return flipped;
    }

    public Point2D getPosition() {
        return position;
    }

    public void setPosition(Point2D position) {
        this.position = position;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public Point2D getSize() {
        double fitWidth = imageView.getFitWidth();
        double fitHeight = imageView.getFitHeight();
        boolean preserveRatio = imageView.isPreserveRatio();
        double width = imageView.getImage().getWidth();
        double height = imageView.getImage().getHeight();
        if (fitWidth > 0) {
            width = fitWidth;
            if (preserveRatio) {
                height *= width / imageView.getImage().getWidth();
            }
        }
        if (fitHeight > 0) {
            height = fitHeight;
            if (preserveRatio) {
                width *= height / imageView.getImage().getHeight();
            }
        }
        return new Point2D(width, height);
    }

    public double getWidth() {
        return getSize().getX();
    }

    public double getHeight() {
        return getSize().getY();
    }
}
