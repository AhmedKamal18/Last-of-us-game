package views;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

public class CircularCanvas extends Canvas {
	private Image img;
    public CircularCanvas(Image img, double w, double h) {
        super(w,h);
        this.img = img;
        draw(w, h);
    }
    
    public void draw(double w, double h) {
    	getGraphicsContext2D().drawImage(img, 0, 0, w, h);
    }
}
