package views;

import java.awt.ActiveEvent;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class AnimatedErrorMessage {
	
	public static boolean active;
	
    public static void animateErrorMessage(String errorMessage, StackPane root) {
        // Create a label to display the error message
    	active = true;
        Label errorLabel = new Label(errorMessage);
        errorLabel.setTextFill(Color.RED);
        errorLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Add the label to the root pane
        root.getChildren().add(errorLabel);

        // Set the label's initial position above the window
        errorLabel.setTranslateY(-50);

        // Create a fade transition to gradually show and hide the label
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), errorLabel);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setDelay(Duration.seconds(2)); // Wait for 2 seconds before starting to fade out

        // Create a translate transition to animate the label's position
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(1), errorLabel);
        translateTransition.setFromY(-50);
        translateTransition.setToY(0);
        
        // Combine the fade and translate transitions
        translateTransition.setOnFinished(event -> {
            fadeTransition.play();
            fadeTransition.setOnFinished(e -> {
            	root.getChildren().remove(errorLabel);
            	active = false;
            });
            
        });

        // Start the animation
        translateTransition.play();
    }
}
