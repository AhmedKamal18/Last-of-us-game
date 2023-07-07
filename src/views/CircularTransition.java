package views;

import javafx.animation.Transition;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.util.Duration;

public class CircularTransition extends Transition {
    private final CircularPane pane;
    private double angle;

    private double startAngle;
    private double rx, ry;

    private double cycle;
    private int direction;

    public CircularTransition(CircularPane pane,int dir,  Duration duration) {
        this.pane = pane;
        super.setCycleDuration(duration);
        this.direction = dir;
    }

    @Override
    public void play() {
        rx = pane.getRx();
        ry = pane.getRy();
        startAngle = pane.getStartAngle();
        angle = pane.getDeltaAngle();
        cycle = pane.getCycle();
        super.play();
    }

    @Override
    protected void interpolate(double f) {
        double currentAngle = startAngle + ( (cycle + f * direction) * angle);
        for (Node node : pane.getChildren()) {
            Point2D p = from(rx, ry, currentAngle);
            node.setTranslateX(p.getX());
            node.setTranslateY(p.getY());
            currentAngle += angle;
        }
    }

    public static Point2D from(double rx, double ry, double currentAngle){
        if (rx >= ry){
            double x = Math.cos(currentAngle) * rx;
            double y = Math.sqrt((Math.pow(rx * ry, 2) - (Math.pow(x, 2) * Math.pow(ry, 2))) / Math.pow(rx, 2));
            if (Math.sin(currentAngle) < 0)
                y *= -1;
            return new Point2D(x, y);
        }
        else {
            double y = Math.sin(currentAngle) * ry;
            double x = Math.sqrt((Math.pow(rx * ry, 2) - (Math.pow(y, 2) * Math.pow(rx, 2))) / Math.pow(ry, 2));
            if (Math.cos(currentAngle) < 0)
                x *= -1;
            return new Point2D(x, y);
        }
    }
}
