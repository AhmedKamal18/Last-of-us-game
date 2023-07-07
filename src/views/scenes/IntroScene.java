package views.scenes;


import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import views.Main;

public class IntroScene extends Scene {

	public IntroScene() {
		super(root(), 1000, 700);

	}

	private static Parent root() {
		Image img = new Image("images//intro.jpg");
		
		BackgroundImage bImg = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.DEFAULT, new BackgroundSize(1.0, 1.0, true, true, false, false));
		Background bGround = new Background(bImg);
		BorderPane pane = new BorderPane();
		Image buttonImg = new Image("images/pngegg.png");
		BackgroundImage buttonBgImage = new BackgroundImage(buttonImg, BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);

		Button startButton = createButton("NEW GAME","black",buttonBgImage);

		Button quitButton = createButton("QUIT GAME","darkred",buttonBgImage);
		startButton.setOnKeyPressed(e->{
			if (e.getCode().equals(KeyCode.ENTER)) {
			Main.stage.getScene().setRoot(SelectScene.root());
		}
		});
	
		startButton.setOnMouseClicked(e -> {
			Main.stage.getScene().setRoot(SelectScene.root());
		});

//		startButton.setOnKeyPressed(e -> {
//			if (e.getCode().equals(KeyCode.ENTER)) {
//				Main.stage.setScene(new SelectScene());
//			}
//		});
		quitButton.setOnMouseClicked(e -> {
			Main.stage.close();
		});

		VBox buttons = new VBox();
		buttons.getChildren().add(startButton);
		buttons.getChildren().add(quitButton);
		buttons.setLayoutX(650);
		buttons.setLayoutY(400);
		buttons.setSpacing(20);
		buttons.setAlignment(Pos.CENTER);
		pane.setCenter(buttons);
		pane.setTranslateY(-60);
		BorderPane root = new BorderPane();
		root.setBottom(pane);
		root.setBackground(bGround);

		
		return root;
	}
	
	static Button createButton(String buttonName ,String hoverColor,BackgroundImage buttonBgImage) {
        Button button = new Button(buttonName);
        button.setPrefSize(400, 60);
		button.setBackground(new Background(buttonBgImage));
		button.setFont(Font.font("Arial", FontWeight.BOLD, 30));
		button.setScaleX(1.0);
		button.setScaleY(0.9);
		button.styleProperty().bind(
				Bindings.when(button.hoverProperty()).then("-fx-cursor: hand; -fx-scale-x: 1.05;" +
						"-fx-scale-y: 1;" +
						"-fx-text-fill: "+hoverColor+";")
						.otherwise(
								"-fx-text-fill: white;"));
		button.setCursor(Cursor.HAND);
        return button;
    }

}
