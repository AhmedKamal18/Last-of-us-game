package model.collectibles;


import java.util.ArrayList;
import java.util.Arrays;

import ai.MoveCreator;
import engine.Game;
import exceptions.*;
import javafx.scene.image.Image;
import model.characters.*;
import model.world.*;
import views.scenes.GameScene;

public class Vaccine implements Collectible {

	public Vaccine() {
		
	}

	@Override
	public void pickUp(Hero h) {
		h.getVaccineInventory().add(this);
		
//		MoveCreator.vis=new boolean[15][15];
//		MoveCreator.bfs(h.getLocation().x, h.getLocation().y, 100, -1);
//		for(int[] arr: MoveCreator.nearCollectible) System.out.println(Arrays.toString(arr));
	}

	@Override
	public void use(Hero h) throws NoAvailableResourcesException {
		h.getVaccineInventory().remove(this);
		Zombie z = (Zombie) h.getTarget();
		Game.zombies.remove(z);
		Hero hero = Game.availableHeroes.remove(0);
		((CharacterCell) Game.map[(int) z.getLocation().getX()][(int) z.getLocation().getY()]).setCharacter(hero);
		Game.heroes.add(hero);
		hero.setLocation(z.getLocation());
		GameScene.gridPane.setAt(z.getLocation().x, z.getLocation().y, hero.getImage());
		Hero.setHeroLit(hero);
	}

	@Override
	public Image getImage() {
		// TODO Auto-generated method stub
		return new Image("images//vaccine.png",700,700,true,true);
	}

}
