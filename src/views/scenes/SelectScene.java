package views.scenes;

import engine.Game;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import views.CircularPane;
import views.DetailsLabel;
import views.Main;
import views.NavPane;

import views.scenes.IntroScene;


public class SelectScene extends Scene {

	private static  int divisions=Game.availableHeroes.size();
	private static CircularPane pane;
	
	
	public SelectScene() {
		super(root(), Main.stage.getScene().getWidth(), Main.stage.getScene().getHeight());
	}

	public static Parent root() {
		pane = new CircularPane(280,200, divisions);
        pane.setPrefSize(800,600);
        for (int i = 0; i < Game.availableHeroes.size(); i++) {
            Pane p = new Pane();
            
            ImageView imgView = new ImageView(Game.availableHeroes.get(i).getImage());
            imgView.setFitWidth(100);
            imgView.setFitHeight(120);
            p.getChildren().add(imgView);
            p.setMaxSize(100,100);
            
            pane.add(p);
        }
      
        NavPane navPane = new NavPane(pane);
        navPane.setFocusTraversable(true);
        Platform.runLater(() -> navPane.requestFocus());
        pane.scale();
        
        navPane.setOnKeyPressed(e -> {
			int size = pane.getChildren().size();
            if (e.getCode() == KeyCode.ENTER && pane.active) {
            	int idx = pane.getCurrentIndex();
            	Main.stage.getScene().setRoot(GameScene.root(Game.availableHeroes.get(idx)));
            }
            else if (e.getCode() == KeyCode.LEFT) {
            	int idx = (pane.getCurrentIndex() - 1)%size;
            	if(idx < 0) idx+=size;
            	NavPane.updateDetails(Game.availableHeroes.get(idx));
            	pane.rotate(1);
            	
            	
            }
            else if (e.getCode() == KeyCode.RIGHT) {
            	int idx = (pane.getCurrentIndex() + 1)%size;
            	NavPane.updateDetails(Game.availableHeroes.get(idx));
            	pane.rotate(-1);
            }            
            
        });
        Image buttonImg = new Image("images/pngegg.png");
		BackgroundImage buttonBgImage = new BackgroundImage(buttonImg, BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);

		Button AiMode = IntroScene.createButton("Let Computer Play","black",buttonBgImage);
		
		AiMode.setFocusTraversable(false);
		AiMode.setOnMouseClicked(e->{
			GameScene.isAIMatch = true;
			int idx = pane.getCurrentIndex();
			Main.stage.getScene().setRoot(GameScene.root(Game.availableHeroes.get(idx)));
		});
		BorderPane box = new BorderPane();
		box.setTranslateY(-80);
		box.setCenter(AiMode);
        navPane.setBottom(box);
        
		return navPane;
	}

}
