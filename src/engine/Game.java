package engine;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.CellRendererPane;

import exceptions.InvalidTargetException;
import exceptions.MovementException;
import exceptions.NotEnoughActionsException;
import javafx.scene.image.ImageView;
import model.characters.Direction;
import model.characters.Explorer;
import model.characters.Fighter;
import model.characters.Hero;
import model.characters.Medic;
import model.characters.Zombie;
import model.collectibles.Supply;
import model.collectibles.Vaccine;
import model.world.Cell;
import model.world.CharacterCell;
import model.world.CollectibleCell;
import model.world.TrapCell;
import views.scenes.GameScene;

public class Game {
	private static int[] dx = new int[] { 1, 1, 1, -1, -1, -1, 0, 0 };
	private static int[] dy = new int[] { 0, -1, 1, 0, 1, -1, 1, -1 };
	public static Cell[][] map = new Cell[15][15];
	public static ArrayList<Hero> availableHeroes = new ArrayList<Hero>();
	public static ArrayList<Hero> heroes = new ArrayList<Hero>();
	public static ArrayList<Zombie> zombies = new ArrayList<Zombie>();
	public static ArrayList<Point>points =new ArrayList<>();
	private static int countEndTurns=0;
	public static int[][] countOfVisits=new int[15][15];
	public static void loadHeroes(String filePath) throws IOException {

		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = br.readLine();
		while (line != null) {
			String[] content = line.split(",");
			Hero hero = null;
			switch (content[1]) {
			case "FIGH":
				hero = new Fighter(content[0], Integer.parseInt(content[2]), Integer.parseInt(content[4]),
						Integer.parseInt(content[3]));
				break;
			case "MED":
				hero = new Medic(content[0], Integer.parseInt(content[2]), Integer.parseInt(content[4]),
						Integer.parseInt(content[3]));
				break;
			case "EXP":
				hero = new Explorer(content[0], Integer.parseInt(content[2]), Integer.parseInt(content[4]),
						Integer.parseInt(content[3]));
				break;
			}
			availableHeroes.add(hero);
			line = br.readLine();

		}
		br.close();

	}

	public static void startGame(Hero hero) {
		for(int i = 0; i<15; i++) {
			for(int j = 0; j<15; j++) {
				map[i][j] = null;
			}
		}
		
		hero.setLocation(new Point(14,0));
		map[14][0] = new CharacterCell(hero, true);
		GameScene.gridPane.setAt(14,0, map[14][0].getImage());
		availableHeroes.remove(hero);
		heroes.add(hero);
		
		for(int i = 0; i<5; i++) {
			generateRandomCell("Supply");
			generateRandomCell("Vaccine");
			generateRandomCell("Trap");
		}
		
		for(int i = 0; i<15; i++) {
			for(int j = 0; j<15; j++) {
				if(map[i][j]==null) {
					map[i][j] = new CharacterCell(null);
					GameScene.gridPane.setAt(i, j, map[i][j].getImage());
				}
			}
		}
		
		for (int i = 0; i < 10; i++) {
			generateZombie();
		}
		setVisibility();


	}
	
	
	public static boolean checkWin() {
		return checkVaccines() && heroes.size() >= 5;
	}

	public static boolean checkGameOver() {

		return heroes.isEmpty() || checkVaccines();
	}
	
	public static void endTurn() {
		countEndTurns++;
	    countEndTurns%=3;
	    if(countEndTurns==0)
	      points.clear();
	    if(GameScene.prevTargetRectangle != null) {
	    	GameScene.prevTargetRectangle.setBorder(null);
	    	GameScene.prevTargetRectangle = null;
	    }
		ArrayList<Zombie>tempZombies=new ArrayList<>(zombies);
		for (Zombie zombie : tempZombies) {
			zombie.attack();
			zombie.setTarget(null);
		}
		for (Hero hero : heroes) {
			hero.setActionsAvailable(hero.getMaxActions());
			hero.setSpecialAction(false);
			hero.setTarget(null);
		}
		generateZombie();
		darken();
		setVisibility();
		

	}
	
//	public static void setAllVisible() {
//		for(int i =0; i<15; i++) {
//			for(int j = 0; j<15; j++) {
//				map[i][j].setVisible(true);
//				GameScene.gridPane.setVisible(i,j,true);
//			}
//		}
//	}
	
	public static void removeCell(int x , int y) {
		map[x][y] = new CharacterCell(null);
		GameScene.gridPane.setAt(x, y, map[x][y].getImage());
		
	}

	private static boolean checkVaccines() {
		for (Hero h : heroes) {
			if (!h.getVaccineInventory().isEmpty())
				return false;
		}
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				if (map[i][j] instanceof CollectibleCell) {
					CollectibleCell collectibleCell = (CollectibleCell) (map[i][j]);
					if (collectibleCell.getCollectible() instanceof Vaccine)
						return false;
				}

			}
		}
		return true;
	}

	

	private static boolean isValidCell(int x, int y) {
		return x > -1 && y > -1 && x < 15 && y < 15;
	}



	public static void setVisibility() {
		for (Hero hero : heroes) {
			int x = hero.getLocation().x;
			int y = hero.getLocation().y;
			map[x][y].setVisible(true);
			GameScene.gridPane.setVisible(x,y,true);
			for (int k = 0; k < 8; k++) {
				int newx = x + dx[k];
				int newy = y + dy[k];
				
				if (isValidCell(newx, newy)) {
					map[newx][newy].setVisible(true);
					GameScene.gridPane.setVisible(newx,newy,true);
				}
			}
		}
	}

	public static void darken() {
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				if (map[i][j] != null) {
					map[i][j].setVisible(false);
					GameScene.gridPane.setVisible(i,j,false);
				}
			}
		}
	}

	


	
	private static void generateZombie() {
		int x, y;
		Random random = new Random();
		while (true) {
			x = random.nextInt(15);
			y = random.nextInt(15);
			if (map[x][y] instanceof CharacterCell && ((CharacterCell)map[x][y]).getCharacter()==null) {
				Zombie zombie = new Zombie();
				map[x][y] = new CharacterCell(zombie);
				zombie.setLocation(new Point(x, y));
				zombies.add(zombie);
				GameScene.gridPane.setAt(x, y, zombie.getImage());
				break;
			}
		}
	}
	
	
	private static void generateRandomCell(String cellType) {
		int x, y;
		Random random = new Random();
		while (true) {
			x = random.nextInt(15);
			y = random.nextInt(15);
			if (map[x][y] == null) {
				if(cellType.equals("Trap")) {
					map[x][y] = new TrapCell();
					GameScene.gridPane.setAt(x, y, map[x][y].getImage());
				}
				else if(cellType.equals("Supply")) {
					map[x][y] = new CollectibleCell(new Supply());
					GameScene.gridPane.setAt(x, y, map[x][y].getImage());
				}
				else if(cellType.equals("Vaccine")) {
					map[x][y] = new CollectibleCell(new Vaccine());
					GameScene.gridPane.setAt(x, y, map[x][y].getImage());
				}
				break;
			}
		}
	}
	
//	public static void printZombies() {
//		for(int i = 0; i<15; i++) {
//			for(int j = 0; j<15; j++) {
//				if(map[i][j] instanceof CharacterCell) {
//					if(((CharacterCell)map[i][j]).getCharacter() == null)
//						System.out.print("N ");
//					else if(((CharacterCell)map[i][j]).getCharacter() instanceof Zombie)
//						System.out.print("Z ");
//					else if(((CharacterCell)map[i][j]).getCharacter() instanceof Hero)
//						System.out.print("H ");
//				}
//				else if(map[i][j] instanceof CollectibleCell) {
//					if(((CollectibleCell)map[i][j]).getCollectible() instanceof Supply)
//						System.out.print("S ");
//					else
//						System.out.print("V ");
//				}
//				else
//					System.out.print("T ");
//			}
//			System.out.println();
//		}
//		System.out.println();
//	}
	
	public static void init() {
		map = new Cell[15][15];
		availableHeroes = new ArrayList<Hero>();
		heroes = new ArrayList<Hero>();
		zombies = new ArrayList<Zombie>();
		try {
			loadHeroes("Heros.csv");
		}
		catch(Exception e){}
	}

}
