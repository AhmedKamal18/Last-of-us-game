package views;


import engine.Game;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class CircularPane extends StackPane {
    private final double startAngle;
    private final double deltaAngle;

    private final double rx, ry;

    private int cycle;

    private Duration scaleDuration;

    private Duration rotateDuration;

    private double scale;

    private  EventHandler<ActionEvent> onFinish;

    public boolean active;
    

    public CircularPane(double rx, double ry, int count, double startAngle) {
        this.rx = rx;
        this.ry = ry;
        this.startAngle = startAngle;
        this.deltaAngle = Math.PI * 2 / count;
        this.scaleDuration = Duration.millis(100);
        this.rotateDuration = Duration.millis(500);
        this.cycle = 0;
        this.scale = 1;
        this.onFinish = e -> {};
        this.active = false;
    }

    public CircularPane(double rx, double ry, int count){
        this(rx, ry, count, -Math.PI / 2);
    }

    public CircularPane(double radius, int count){
        this(radius, radius, count, -Math.PI / 2);
    }

    public void add(Node node){
        int n = getChildren().size();
        double angle = startAngle + n * deltaAngle;
        Point2D p = CircularTransition.from(rx, ry, angle);
        node.setTranslateX(p.getX());
        node.setTranslateY(p.getY());
        getChildren().add(node);
    }

    public void rotate(int dir){
    	
        if (!active) return;
        active = false;
        scale(-1, r->{
            CircularTransition t = new CircularTransition(this, dir, rotateDuration);
            t.onFinishedProperty().set(e -> {
                cycle += dir;
                scale(1, a ->{
                    active = true;
                    onFinish.handle(a);
                });
            });
            t.play();

        });
        
    }
    

    private void scale(int sign, EventHandler<ActionEvent> handler){
        ScaleTransition transition = new ScaleTransition(scaleDuration, getChildren().get(getCurrentIndex()));
        transition.setByX(sign * scale);
        transition.setByY(sign * scale);
        transition.onFinishedProperty().set(handler);
        transition.play();
    }

    public void scale(){
        scale(1, e ->{
            active = true;
            onFinish.handle(e);
        });
    }

    public int getCurrentIndex(){
        int n = (getChildren().size() - (cycle % getChildren().size())) % getChildren().size();
        return n;
    }

    public double getStartAngle() {
        return startAngle;
    }

    public double getDeltaAngle() {
        return deltaAngle;
    }

    public double getRx() {
        return rx;
    }

    public double getRy() {
        return ry;
    }

    public int getCycle() {
        return cycle;
    }

    public Duration getScaleDuration() {
        return scaleDuration;
    }

    public void setScaleDuration(Duration scaleDuration) {
        this.scaleDuration = scaleDuration;
    }

    public Duration getRotateDuration() {
        return rotateDuration;
    }

    public void setRotateDuration(Duration rotateDuration) {
        this.rotateDuration = rotateDuration;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public EventHandler<ActionEvent> getOnFinish() {
        return onFinish;
    }

    public void setOnFinish(EventHandler<ActionEvent> onFinish) {
        this.onFinish = onFinish;
    }
}
