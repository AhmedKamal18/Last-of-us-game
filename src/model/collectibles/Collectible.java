package model.collectibles;

import engine.Game;
import exceptions.*;
import javafx.scene.image.Image;
import model.characters.*;
import model.world.*;

public interface Collectible {
	public void pickUp(Hero h);
	public void use(Hero h) throws NoAvailableResourcesException;
	public Image getImage();
	

}
