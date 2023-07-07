package views;

import engine.Game;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.paint.Color;

import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
public class MapPane extends GridPane {
	public static int row,col;
	public MapPane() {
		super();
		this.setPadding(new Insets(1, 1, 1, 1));
		this.setVgap(2);
		this.setHgap(2);
		Image image = new Image("images/gridimage.jpg");
		BackgroundImage bImg = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.DEFAULT, new BackgroundSize(1.0, 1.0, true, true, false, false));
		Background bGround = new Background(bImg);
		this.setBackground(bGround);
		for (row = 0; row < 15; row++) {
			RowConstraints rowConstraints = new RowConstraints();
			rowConstraints.setVgrow(Priority.ALWAYS);
			this.getRowConstraints().add(rowConstraints);
			ColumnConstraints columnConstraints = new ColumnConstraints();
			columnConstraints.setHgrow(Priority.ALWAYS);
			this.getColumnConstraints().add(columnConstraints);
			for (col = 0; col < 15; col++) {
				RectanglePane rectangle = new RectanglePane(col,row);
				this.add(rectangle, row, col);
				rectangle.setBackground(Background.fill(Color.WHEAT));
			}
		}
	}
	public void setAt(int x, int y, Image image) {
		BorderPane borderPane = (BorderPane) getChildren().get(y * 15 + x);
		ImageView imageView = new ImageView(image);
		imageView.setFitWidth(50);
		imageView.setFitHeight(50);
		borderPane.setCenter(imageView);
	}

	public void setVisible(int x, int y, boolean flg) {
		getChildren().get(y * 15 + x).setVisible(flg);
	}


}
