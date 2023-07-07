package model.world;

import engine.Game;
import exceptions.*;
import javafx.scene.image.Image;
import model.characters.*;
import model.collectibles.*;

public abstract class Cell {


	private boolean isVisible;
	
	public Cell() {
	
		isVisible = false;
	
	}
	
	public boolean isVisible() {
	
		return isVisible;
	
	}
	
	public void setVisible(boolean isVisible) {
	
		this.isVisible = isVisible;
	
	}

	
	public abstract Image getImage();

}


