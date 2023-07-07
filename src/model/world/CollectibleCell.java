package model.world;

import engine.Game;
import exceptions.*;
import javafx.scene.image.Image;
import model.characters.*;
import model.collectibles.*;

public class CollectibleCell extends Cell {

	private Collectible collectible;
	
	public CollectibleCell(Collectible collectible) {
		this.collectible = collectible;
	}

	public Collectible getCollectible() {
		return collectible;
	}

	@Override
	public Image getImage() {
		// TODO Auto-generated method stub
		return collectible.getImage();
	}

}
