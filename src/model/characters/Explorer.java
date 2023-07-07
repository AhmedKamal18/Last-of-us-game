package model.characters;

import engine.Game;
import exceptions.*;
import javafx.scene.image.Image;
import model.collectibles.*;
import model.world.*;
import views.scenes.GameScene;

public class Explorer extends Hero {
	

	public Explorer(String name,int maxHp, int attackDmg, int maxActions) {
		super( name, maxHp,  attackDmg,  maxActions) ;
		
	}

	@Override
	public void useSpecial() throws NoAvailableResourcesException, InvalidTargetException {
		if(this.isSpecialAction())return;
		super.useSpecial();
		for(int i=0;i<15;i++) {
			for(int j=0;j<15;j++) {
				Game.map[i][j].setVisible(true);
				GameScene.gridPane.setVisible(i,j, true);
			}
		}
		
		
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return "Explorer";
	}

	@Override
	public Image getImage() {
		 return new Image("images//char_1.png",700,700,true,true);
	}

	

	
}
