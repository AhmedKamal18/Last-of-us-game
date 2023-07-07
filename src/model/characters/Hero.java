package model.characters;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Map;

import ai.MoveCreator;
import engine.Game;
import exceptions.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import model.characters.Character;
import model.collectibles.*;
import model.world.*;
import views.AnimatedErrorMessage;
import views.Main;
import views.RectanglePane;
import views.scenes.GameScene;

public abstract class Hero extends Character {

	private int actionsAvailable;
	private int maxActions;
	private ArrayList<Vaccine> vaccineInventory;
	private ArrayList<Supply> supplyInventory;
	private boolean specialAction;
	private ArrayList<Character> targetChars = new ArrayList<>();;
	static int[] dx = new int[] { -1, -1, 0, 1, 1, 1, 0, -1 };
	static int[] dy = new int[] { 0, 1, 1, 1, 0, -1, -1, -1 };

	public Hero(String name, int maxHp, int attackDmg, int maxActions) {
		super(name, maxHp, attackDmg);
		this.maxActions = maxActions;
		this.actionsAvailable = maxActions;
		this.vaccineInventory = new ArrayList<Vaccine>();
		this.supplyInventory = new ArrayList<Supply>();
		this.specialAction = false;

	}

	@Override
	public void onCharacterDeath() {
		Game.removeCell((int) getLocation().getX(), (int) getLocation().getY());
		Game.heroes.remove(this);
	}

	public void move(Direction d) throws MovementException, NotEnoughActionsException {

		if (getCurrentHp() == 0) {
			onCharacterDeath();
			return;
		}
		if (getActionsAvailable() <= 0)
			throw new NotEnoughActionsException();

		int x = (int) this.getLocation().getX();
		int y = (int) this.getLocation().getY();

		switch (d) {
		case LEFT:
			y--;
			break;
		case RIGHT:
			y++;
			break;
		case UP:
			x--;
			break;
		case DOWN:
			x++;
		}
		if (!isValid(x, y))
			throw new MovementException();

		else if (Game.map[x][y] instanceof TrapCell) {
			TrapCell tc = (TrapCell) Game.map[x][y];
			BorderPane borderPane = (BorderPane) Main.stage.getScene().getRoot();
			AnimatedErrorMessage.animateErrorMessage("YOU WENT INTO A TRAP CELL!",
					(StackPane) (borderPane).getCenter());
			GameScene.gridPane.setAt(getLocation().x, getLocation().y, null);
			((RectanglePane) (GameScene.gridPane.getChildren().get(y * 15 + x))).setBorder(new Border(
					new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
			this.setCurrentHp(this.getCurrentHp() - tc.getTrapDamage());
			Game.map[x][y] = new CharacterCell(null);
			if (this.getCurrentHp() == 0) {
				this.onCharacterDeath();
				return;
			}
		} else if (Game.map[x][y] instanceof CollectibleCell) {
			((CollectibleCell) Game.map[x][y]).getCollectible().pickUp(this);
			MoveCreator.updateCollectible(new Point(x,y));
			GameScene.gridPane.setAt(getLocation().x, getLocation().y, null);
			Game.map[x][y] = new CharacterCell(null);
			GameScene.gridPane.setAt(x, y, getImage());
		}

		if (((CharacterCell) Game.map[x][y]).getCharacter() != null)
			throw new MovementException();
		else {

			((CharacterCell) Game.map[x][y]).setCharacter(this);
			((CharacterCell) Game.map[(int) this.getLocation().getX()][(int) this.getLocation().getY()])
					.setCharacter(null);
			GameScene.gridPane.setAt(getLocation().x, getLocation().y, null);
			((RectanglePane) GameScene.gridPane.getChildren().get(this.getLocation().y * 15 + this.getLocation().x))
					.setBorder(null);
			this.setLocation(new Point(x, y));
			Game.map[x][y].setVisible(true);
			GameScene.gridPane.setAt(x, y, getImage());
			GameScene.gridPane.setVisible(x, y, true);
			setHeroLit(this);
		}
		updateTargetCharacters();

		// added by ahmed
		setActionsAvailable(getActionsAvailable() - 1);
		int inc = Game.countOfVisits[getLocation().x][getLocation().y];
		Game.countOfVisits[getLocation().x][getLocation().y] += (inc + 1) * (inc + 1);
	}

	public static void updateTargetCharacters() {
		for (Hero hero : Game.heroes) {
			hero.targetChars.clear();
			int x = (int) hero.getLocation().x;
			int y = (int) hero.getLocation().y;
			for (int i = 0; i < 8; i++) {
				if (isValid(x + dx[i], y + dy[i])) {
					if (Game.map[x + dx[i]][y + dy[i]] instanceof CharacterCell) {
						Character target = ((CharacterCell) Game.map[x + dx[i]][y + dy[i]]).getCharacter();
						if (target != null) {
							if (!(hero instanceof Medic) && target instanceof Hero)
								continue;
							hero.targetChars.add(target);
						}

					}
				}
			}
			if ((hero instanceof Medic))
				hero.targetChars.add(hero);
		}
	}

	public ArrayList<Character> getTargetChars() {
		return this.targetChars;
	}

	public static boolean isValid(int x, int y) {
		return x > -1 && y > -1 && x < 15 && y < 15;
	}

	public static void setHeroLit(Hero h) {
		int[] dx = new int[] { 1, 1, 1, -1, -1, -1, 0, 0 };
		int[] dy = new int[] { 0, -1, 1, 0, 1, -1, 1, -1 };
		Point p = h.getLocation();
		int x = (int) p.getX();
		int y = (int) p.getY();
		Game.map[x][y].setVisible(true);
		GameScene.gridPane.setVisible(x, y, true);
		for (int i = 0; i < 8; i++) {
			if (!isValid(x + dx[i], y + dy[i]) || Game.map[x + dx[i]][y + dy[i]] == null)
				continue;
			Game.map[x + dx[i]][y + dy[i]].setVisible(true);
			GameScene.gridPane.setVisible(x + dx[i], y + dy[i], true);
			GameScene.gridPane.setAt(x + dx[i], y + dy[i], Game.map[x + dx[i]][y + dy[i]].getImage());
		}

	}

	public void useSpecial() throws NoAvailableResourcesException, InvalidTargetException {
		if (this.getSupplyInventory().isEmpty()) {
			throw new NoAvailableResourcesException();
		}
		this.setSpecialAction(true);
		this.getSupplyInventory().get(0).use(this);

	}

	public void cure() throws NoAvailableResourcesException, InvalidTargetException, NotEnoughActionsException {
		if (getActionsAvailable() <= 0)
			throw new NotEnoughActionsException();

		if (this.getTarget() == null || !(getTarget() instanceof Zombie) || !isAdjacent(getTarget())) {
			throw new InvalidTargetException();
		} else if (this.vaccineInventory.isEmpty())
			throw new NoAvailableResourcesException();
		else {

			this.vaccineInventory.get(0).use(this);
			for (Hero hero : Game.heroes) {
				if (hero.targetChars.contains(getTarget()))
					hero.targetChars.remove(getTarget());
			}
			setTarget(null);
			if (GameScene.prevTargetRectangle != null)
				GameScene.prevTargetRectangle.setBorder(null);
			setActionsAvailable(getActionsAvailable() - 1);
		}
		updateTargetCharacters();
	}

	public boolean isSpecialAction() {
		return specialAction;
	}

	public void setSpecialAction(boolean specialAction) {
		this.specialAction = specialAction;
	}

	public int getActionsAvailable() {
		return actionsAvailable;
	}

	public void setActionsAvailable(int actionsAvailable) {
		this.actionsAvailable = actionsAvailable;
	}

	@Override
	public void attack() throws InvalidTargetException, NotEnoughActionsException {
		if (this.getActionsAvailable() <= 0)
			throw new NotEnoughActionsException();
		else if (this.getTarget() instanceof Hero)
			throw new InvalidTargetException();
		Zombie target = (Zombie) getTarget();
		super.attack();
		setActionsAvailable(getActionsAvailable() - 1);
		updateTargetCharacters();

	}

	private boolean isAdjacent(Character target) {
		if (Math.abs(getLocation().x - target.getLocation().x) == 1
				&& Math.abs(getLocation().y - target.getLocation().y) == 1)
			return true;
		if (Math.abs(getLocation().x - target.getLocation().x)
				+ Math.abs(getLocation().y - target.getLocation().y) == 1)
			return true;
		return false;
	}

	public int getMaxActions() {
		return maxActions;
	}

	public abstract String getType();

	public ArrayList<Vaccine> getVaccineInventory() {
		return vaccineInventory;
	}

	public ArrayList<Supply> getSupplyInventory() {
		return supplyInventory;
	}

}
