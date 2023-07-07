package model.characters;

import java.awt.Point;

import engine.Game;
import exceptions.*;
import javafx.scene.image.Image;
import model.collectibles.*;
import model.world.*;

public class Fighter extends Hero {

	public Fighter(String name, int maxHp, int attackDmg, int maxActions) {
		super(name, maxHp, attackDmg, maxActions);

	}

	@Override
	public void useSpecial() throws NoAvailableResourcesException, InvalidTargetException {
		if(this.isSpecialAction())return;
		super.useSpecial();
	}

	@Override
	public void attack() throws InvalidTargetException, NotEnoughActionsException {
		if (!this.isSpecialAction()) {
			super.attack();
			return;
		}
		if (this.getTarget() == null || this.getTarget() instanceof Hero)
			throw new InvalidTargetException();
		else if (!isAdjacent(getTarget()))
			throw new InvalidTargetException();
		else {
			this.getTarget().defend(this);
			this.getTarget().setCurrentHp(this.getTarget().getCurrentHp() - this.getAttackDmg());
			if(this.getTarget().getCurrentHp()<=0)this.getTarget().onCharacterDeath();
			if(getTarget().getCurrentHp() == 0) {
				setTarget(null);
			}
		}

	}

	private boolean isAdjacent(Character target) {
		if(Math.abs(getLocation().x - target.getLocation().x) ==1 && Math.abs(getLocation().y - target.getLocation().y) == 1)
			return true;
		if(Math.abs(getLocation().x - target.getLocation().x)+ Math.abs(getLocation().y - target.getLocation().y) == 1)
			return true;
		return false;
	}
	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return "Fighter";
	}
	@Override
	public Image getImage() {
		 return new Image("images//char_0.png",700,700,true,true);
	}

}
