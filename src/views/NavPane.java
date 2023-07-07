package views;

import engine.Game;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.characters.Hero;
import views.scenes.GameScene;
import views.scenes.SelectScene;

public class NavPane extends BorderPane {
	public static DetailsLabel details;

	public NavPane(CircularPane pane) {
		Image img = new Image("images//selection.png");
		BackgroundImage bImg = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.DEFAULT, new BackgroundSize(1.0, 1.0, true, true, false, false));
		Background bGround = new Background(bImg);
		setBackground(bGround);

		StackPane center = new StackPane();
		updateDetails(Game.availableHeroes.get(pane.getCurrentIndex()));
		center.getChildren().add(pane);
		center.getChildren().add(details);
//		details.setTranslateX(0);
		details.setTranslateY(20);
		Button b1 = new Button();
		Button b2 = new Button();

		Polygon rightArrow = new Polygon();
		rightArrow.getPoints().addAll(0.0, 0.0, 0.0, 100.0, 50.0, 50.0);
		rightArrow.setFill(Color.GOLD);
		rightArrow.setStyle("-fx-stroke:"+toRGBCode(Color.DARKGOLDENROD)+ ";-fx-stroke-width: 4;");

		Polygon leftArrow = new Polygon();
		leftArrow.getPoints().addAll(50.0, 0.0, 50.0, 100.0, 0.0, 50.0);
		leftArrow.setFill(Color.GOLD);
		leftArrow.setStyle("-fx-stroke:"+toRGBCode(Color.DARKGOLDENROD)+ ";-fx-stroke-width: 4;");

		
		b1.setMaxSize(350, 350);
		b1.setGraphic(rightArrow);
		b1.getStyleClass().add("arrow-button");
		b1.setFocusTraversable(false);
		b1.setScaleX(1.0);
		b1.setScaleY(1.0);
		b1.styleProperty().bind(
				Bindings.when(b1.hoverProperty()).then("-fx-cursor: hand; -fx-scale-x: 1.2;" +
						"-fx-scale-y: 1.2;" +
						"-fx-background-color: transparent;")
						.otherwise(
								"-fx-background-color: transparent;"));
		b1.setCursor(Cursor.HAND);
		b1.setOnMouseClicked(event -> {
			// TODO: make it in method
			int size = pane.getChildren().size();
			int idx = (pane.getCurrentIndex() + 1) % size;
			NavPane.updateDetails(Game.availableHeroes.get(idx));
			pane.rotate(-1);
		});
		

		b2.setMaxSize(350, 350);
		b2.setGraphic(leftArrow);
		b2.getStyleClass().add("arrow-button");
		b2.setFocusTraversable(false);
		b2.setScaleX(1.0);
		b2.setScaleY(1.0);
		b2.styleProperty().bind(
				Bindings.when(b2.hoverProperty()).then("-fx-cursor: hand; -fx-scale-x: 1.2;" +
						"-fx-scale-y: 1.2;" +
						"-fx-background-color: transparent;")
						.otherwise(
								"-fx-background-color: transparent;"));
		b2.setCursor(Cursor.HAND);
		b2.setOnMouseClicked(event -> {
			int size = pane.getChildren().size();
			int idx = (pane.getCurrentIndex() - 1) % size;
			if (idx < 0)
				idx += size;
			NavPane.updateDetails(Game.availableHeroes.get(idx));
			pane.rotate(1);
		});
		
		
		BorderPane container = new BorderPane();
		BorderPane container2 = new BorderPane();
		container.setCenter(b1);
		container2.setCenter(b2);
		setRight(container);
		setLeft(container2);
		setCenter(center);

	}

	public static void updateDetails(Hero hero) {
		if (details == null) {
			details = new DetailsLabel(hero);
			return;
		}
		DetailsLabel label = new DetailsLabel(hero);
		details.setText(label.description());
		details.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		details.setMaxSize(250, 150);
		details.setTextFill(Color.WHITE);
		details.setBackground(
				new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0.7), new CornerRadii(20), Insets.EMPTY)));
		details.setPadding(new Insets(10));
		StackPane.setAlignment(details, Pos.CENTER);
	}
	
	private static String toRGBCode(Color color) {
		return String.format("#%02X%02X%02X", (int) (color.getRed() * 255), (int) (color.getGreen() * 255),
				(int) (color.getBlue() * 255));
	}

}
