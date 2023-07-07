package views;

import java.util.ArrayList;

import engine.Game;
import javafx.scene.Cursor;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import model.characters.Character;
import model.characters.Hero;
import model.world.CharacterCell;
import views.scenes.GameScene;

public class RectanglePane extends BorderPane {
	private int x, y;
	private ArrayList<Character> targetChars = new ArrayList<>();;
	static int[] dx = new int[] { -1, -1, 0, 1, 1, 1, 0, -1 };
	static int[] dy = new int[] { 0, 1, 1, 1, 0, -1, -1, -1 };

	public ArrayList<Character> getTargetChars() {
		return this.targetChars;
	}

	public RectanglePane(int x, int y) {
		super();
		this.x = x;
		this.y = y;
		setFocusTraversable(false);
		setOnMouseClicked(e -> {

			if (e.getClickCount() == 2) {
				if (Game.map[x][y] instanceof CharacterCell) {
					Character character = ((CharacterCell) Game.map[x][y]).getCharacter();
					if (character instanceof Hero) {
						GameScene.setMainHero((Hero) character);
						
					}
				}
			} else if (e.getClickCount() == 1 && GameScene.mainHero != null) {
				if (GameScene.prevTargetRectangle != null) {
					GameScene.prevTargetRectangle.setBorder(null);
				}
				if (Game.map[x][y] instanceof CharacterCell) {
					setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
							new BorderWidths(3))));
					Character character = ((CharacterCell) Game.map[x][y]).getCharacter();
					GameScene.mainHero.setTarget(character);
					GameScene.prevTargetRectangle = this;
				}

			}
		});
		setCursor(Cursor.HAND);
		setVisible(false);
		setBackground(Background.fill(Color.WHITE));
		setPrefSize(35, 35);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

}
