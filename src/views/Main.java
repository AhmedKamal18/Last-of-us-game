package views;

import engine.Game;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import views.scenes.IntroScene;
import javafx.scene.Group;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

	public static Stage stage;

	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;
		Image logo = new Image("images//logo.jpeg");
		stage.getIcons().add(logo);
		stage.setTitle("Last Of Us - Legacy");
		stage.setFullScreen(true);
		stage.setFullScreenExitHint("");
		stage.setScene(new IntroScene());
		stage.show();
	}

	public static void main(String[] args) {
		try {
			Game.loadHeroes("Heros.csv");
			launch(args);
		} catch (Exception e) {

		}
	}
}