package views;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Testo extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Load the custom font
        Font customFont = Font.loadFont(Testo.class.getResourceAsStream("images//Rubik.ttf"), 16);

        // Create the button
        Button startButton = new Button("Start Game");
        startButton.setFont(customFont);
        startButton.setStyle("-fx-background-color: red;"
                + "-fx-text-fill: white;"
                + "-fx-effect: dropshadow(gaussian, black, 5, 0, 0, 1);");

        StackPane root = new StackPane(startButton);
        Scene scene = new Scene(root, 400, 300);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
