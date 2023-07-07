package model.characters;

import java.awt.Point;

import engine.Game;
import exceptions.*;
import javafx.scene.image.Image;
import model.collectibles.*;
import model.world.*;
import views.scenes.GameScene;

public class Medic extends Hero {
	// Heal amount attribute - quiz idea

	public Medic(String name, int maxHp, int attackDmg, int maxActions) {
		super(name, maxHp, attackDmg, maxActions);

	}

	@Override
	public void useSpecial() throws NoAvailableResourcesException, InvalidTargetException {
		if (this.isSpecialAction())
			return;
		if (this.getTarget() == null || !(this.getTarget() instanceof Hero)) {
			throw new InvalidTargetException();
		}
		if (!isAdjacent(getTarget())) {
			throw new InvalidTargetException();
		}
		super.useSpecial();
		this.setSpecialAction(this.getSupplyInventory().isEmpty());
		if (GameScene.prevTargetRectangle != null)
			GameScene.prevTargetRectangle.setBorder(null);
		GameScene.prevTargetRectangle = null;
		this.getTarget().setCurrentHp(this.getTarget().getMaxHp());
	}

	private boolean isAdjacent(Character target) {
		if (Math.abs(getLocation().x - target.getLocation().x) == 1
				&& Math.abs(getLocation().y - target.getLocation().y) == 1)
			return true;
		if (Math.abs(getLocation().x - target.getLocation().x)
				+ Math.abs(getLocation().y - target.getLocation().y) <= 1)
			return true;
		return false;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return "Medic";
	}

	@Override
	public Image getImage() {
		return new Image("images//char_2.png", 700, 700, true, true);
	}

}
