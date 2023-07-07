package model.collectibles;


import java.util.ArrayList;
import java.util.Arrays;

import ai.MoveCreator;
import engine.Game;
import exceptions.*;
import javafx.scene.image.Image;
import model.characters.*;
import model.world.*;

public class Supply implements Collectible  {

	

	
	public Supply() {
		
	}

	@Override
	public void pickUp(Hero h) {
		h.getSupplyInventory().add(this);

//		MoveCreator.vis=new boolean[15][15];
//		MoveCreator.bfs(h.getLocation().x, h.getLocation().y, 60, -1);
//		for(int[] arr: MoveCreator.nearCollectible) System.out.println(Arrays.toString(arr));
	}

	@Override
	public void use(Hero h) throws NoAvailableResourcesException {
		h.getSupplyInventory().remove(this);	
	}

	@Override
	public Image getImage() {
		// TODO Auto-generated method stub
		return new Image("images//supply.png",700,700,true,true);
	}
	
		

}
