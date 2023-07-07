package model.characters;

import java.awt.Point;
import java.security.InvalidAlgorithmParameterException;

import engine.Game;
import exceptions.*;
import javafx.scene.image.Image;
import model.collectibles.*;
import model.world.*;
import views.scenes.GameScene;

public abstract class Character {
	private String name;
	private Point location;
	private int maxHp;
	private int currentHp;
	private int attackDmg;
	private Character target;

	public Character() {
	}

	private boolean isAdjacent(Character target) {
		if (Math.abs(location.x - target.location.x) == 1 && Math.abs(location.y - target.location.y) == 1)
			return true;
		if (Math.abs(location.x - target.location.x) + Math.abs(location.y - target.location.y) == 1)
			return true;
		return false;
	}

	public void attack() throws InvalidTargetException, NotEnoughActionsException {
		if (this.target == null || !isAdjacent(getTarget())) {
			throw new InvalidTargetException();
		}
		this.getTarget().defend(this);
		this.getTarget().setCurrentHp(this.getTarget().currentHp - attackDmg);
		if (getTarget().getCurrentHp() == 0) {
			setTarget(null);
		}
	}

	public abstract void onCharacterDeath();

	public void defend(Character hero) {
		hero.setCurrentHp(hero.getCurrentHp() - this.attackDmg / 2);
	}

	public Character(String name, int maxHp, int attackDmg) {
		this.name = name;
		this.maxHp = maxHp;
		this.currentHp = maxHp;
		this.attackDmg = attackDmg;
	}

	public Character getTarget() {
		return target;
	}

	public void setTarget(Character target) {
		this.target = target;
	}

	public String getName() {
		return name;
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public int getMaxHp() {
		return maxHp;
	}

	public int getCurrentHp() {
		return currentHp;
	}

	public void setCurrentHp(int currentHp) {
		if (currentHp <= 0) {
			if (this instanceof Zombie) {
				for (Hero hero : Game.heroes) {
					if (hero.getTargetChars().contains(this)) {
						hero.getTargetChars().remove(this);
					}
				}
				if (GameScene.prevTargetRectangle != null) {
					GameScene.prevTargetRectangle.setBorder(null);
				}
				
			}
			this.currentHp = 0;
			onCharacterDeath();
			Game.map[location.x][location.y] = new CharacterCell(null);
		} else if (currentHp > maxHp)
			this.currentHp = maxHp;
		else
			this.currentHp = currentHp;
	}

	public int getAttackDmg() {
		return attackDmg;
	}

	public abstract Image getImage();

}
