package views.scenes;

import engine.Game;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import views.Main;
public class LossScene extends Scene {
    
    public LossScene() {
        super(root(), Main.stage.getScene().getWidth(), Main.stage.getScene().getHeight());
    }
    
    public static Parent root() {
        BorderPane borderPane = new BorderPane();
        Image img = new Image("images/Defeat.jpg");
        BackgroundImage bImg = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, new BackgroundSize(1.0, 1.0, true, true, false, false));
        Background bGround = new Background(bImg);
        borderPane.setBackground(bGround);
        
        Image buttonImg = new Image("images/pngegg.png");
		BackgroundImage buttonBgImage = new BackgroundImage(buttonImg, BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        Button playAgainButton = IntroScene.createButton("PLAY AGAIN","black",buttonBgImage);
        Button exitButton = IntroScene.createButton("EXIT GAME","darkred",buttonBgImage);
        playAgainButton.setOnKeyPressed(e->{
            Game.init();
			if (e.getCode().equals(KeyCode.ENTER)) {
			Main.stage.getScene().setRoot(SelectScene.root());
		}
		});
        playAgainButton.setOnMouseClicked(e -> {
            Game.init();
            Main.stage.getScene().setRoot(SelectScene.root());
        });
        
        exitButton.setOnMouseClicked(e -> {
            Main.stage.close();
        });
        HBox buttonBox = new HBox(100);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(playAgainButton, exitButton);
        borderPane.setCenter(buttonBox);
        buttonBox.setTranslateY(200);
        return borderPane;
    }
}
