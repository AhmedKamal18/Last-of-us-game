package model.characters;

import java.awt.Point;
import java.util.Random;

import engine.Game;
import exceptions.*;
import javafx.scene.image.Image;
import model.collectibles.*;
import model.world.*;



public class Zombie extends Character {
	static int ZOMBIES_COUNT = 1;
	
	private static int[] dx = new int[] { 1, 1, 1, -1, -1, -1, 0, 0 };
	private static int[] dy = new int[] { 0, -1, 1, 0, 1, -1, 1, -1 };
	
	public Zombie() {
		super("Zombie " + ZOMBIES_COUNT, 40, 10);
		ZOMBIES_COUNT++;
	}

	@Override
	public void attack() {
		
		int x = getLocation().x;
		int y = getLocation().y;
		for (int k = 0; k < 8; k++) {
			int newX = x + dx[k];
			int newY = y + dy[k];
			if (isValidCell(newX, newY)) {
				if (Game.map[newX][newY] instanceof CharacterCell) {
					CharacterCell characterCell = (CharacterCell) (Game.map[newX][newY]);
					if (characterCell.getCharacter()!=null && characterCell.getCharacter() instanceof Hero) {
						Hero hero=(Hero) characterCell.getCharacter();
						setTarget(hero);
						try {
							super.attack();
						}
						catch(InvalidTargetException | NotEnoughActionsException exception) {
							
						}
						setTarget(null);
						return;
					}
				}

			}
		}
		
	}
	
	@Override
	public void onCharacterDeath() {
		for(Hero hero:Game.heroes) {
			if(hero.getTargetChars().contains(this)) {
				hero.getTargetChars().remove(this);
			}
		}
		Game.removeCell((int) getLocation().getX() , (int) getLocation().getY());
		Game.zombies.remove(this);
		generateZombie();
	}
	
	
	private static void generateZombie() {
		int x, y;
		Random random = new Random();
		while (true) {
			x = random.nextInt(15);
			y = random.nextInt(15);
			if (Game.map[x][y] instanceof CharacterCell && ((CharacterCell)Game.map[x][y]).getCharacter()==null) {
				Zombie zombie = new Zombie();
				Game.map[x][y] = new CharacterCell(zombie);
				zombie.setLocation(new Point(x, y));
				Game.zombies.add(zombie);
				break;
			}
		}
	}
	
	private static boolean isValidCell(int x, int y) {
		return x > -1 && y > -1 && x < 15 && y < 15;
	}

	@Override
	public Image getImage() {
		return new Image("images//zombie.png");
	}


}


