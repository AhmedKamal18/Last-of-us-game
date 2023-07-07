package ai;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;

import engine.Game;
import exceptions.InvalidTargetException;
import exceptions.MovementException;
import exceptions.NoAvailableResourcesException;
import exceptions.NotEnoughActionsException;
import model.characters.Character;
import model.characters.Direction;
import model.characters.Explorer;
import model.characters.Fighter;
import model.characters.Hero;
import model.characters.Medic;
import model.characters.Zombie;
import model.collectibles.Collectible;
import model.collectibles.Vaccine;
import model.world.CharacterCell;
import model.world.CollectibleCell;
import model.world.TrapCell;
import views.scenes.GameScene;

public class MoveCreator {
//	static int CANTENTER=-100;
//	static int ATTACK=-10;
//	static int CURE=10000;
//	static int USESPECIALEXPLORER

	private static int[] dx = new int[] { 1, 0, 0, -1 };
	private static int[] dy = new int[] { 0, 1, -1, 0 };
	private static int[] di = new int[] { 1, 1, 1, -1, -1, -1, 0, 0, 0 };
	private static int[] dj = new int[] { 0, -1, 1, 0, 1, -1, 1, -1, 0 };
	private static int[][] win;
	public static boolean[][] vis;
	public static int[][] nearCollectible = new int[15][15];
	private static boolean isUseSpecial = false;
	private static boolean isLastEndGame = false;
	private static boolean isDiffused = false;
	private static Hero heroInCalculation;
	private static int depth = 678;
	private static int explorerBonus = 15;
	private static int vaccineHolderBonus = 20;
	private static ArrayList<Point> vaccinesLocations = null;
	private static ArrayList<Point> suppliesLocations = null;

	public static void AIGame() throws InterruptedException, NoAvailableResourcesException, InvalidTargetException,
			NotEnoughActionsException, MovementException {
		Point pnt = MoveCreator.getBestMove(depth);

		if (GameScene.mainHero.getActionsAvailable() == 0) {
			int countRemActions = 0;
			Hero maxHero = null;
			int maxActions = 0;
			for (Hero hero : Game.heroes) {
				countRemActions += hero.getActionsAvailable();
				if (hero.getActionsAvailable() > maxActions) {
					maxActions = hero.getActionsAvailable();
					maxHero = hero;
				}
			}
			if (isLastEndGame || countRemActions > 2) {
				GameScene.mainHero = maxHero;
				pnt = bestMoveblaCell(GameScene.mainHero.getLocation());
				moveTurn(pnt);
				isLastEndGame = false;
			} else {
				isLastEndGame = true;
				Game.endTurn();
			}
		} else if (collectibleMove() != null) {
			Point point = collectibleMove();
			GameScene.mainHero = heroInCalculation;
			moveTurn(point);
			return;
		} else if (MoveCreator.getUseSpecial()) {
			if (GameScene.mainHero.getSupplyInventory().size() > 0) {
				Character target = null;
				if (GameScene.mainHero instanceof Medic) {
					int max = 0;
					for (int i = 0; i < 9; i++) {
						if (isValid(pnt.x + di[i], pnt.y + dj[i])) {
							if (Game.map[pnt.x + di[i]][pnt.y + dj[i]] instanceof CharacterCell) {
								CharacterCell cc = (CharacterCell) Game.map[pnt.x + di[i]][pnt.y + dj[i]];
								if (cc.getCharacter() instanceof Hero) {
									int gain = cc.getCharacter().getMaxHp()
											* (cc.getCharacter().getMaxHp() - cc.getCharacter().getCurrentHp());
									if (max <= gain) {
										target = cc.getCharacter();
										max = gain;
									}
								}
							}
						}

					}
					if (target == null) {
						pnt = bestMoveblaCell(GameScene.mainHero.getLocation());
						MoveCreator.setUseSpecial(false);
						isLastEndGame = false;
						moveTurn(pnt);
						return;

					}

				} else if (GameScene.mainHero instanceof Fighter) {
					if (GameScene.mainHero.getTargetChars().size() == 0) {
						pnt = bestMoveblaCell(GameScene.mainHero.getLocation());
						MoveCreator.setUseSpecial(false);
						isLastEndGame = false;
						moveTurn(pnt);
						return;
					} else
						target = GameScene.mainHero.getTargetChars().get(0);

				}
				if (GameScene.mainHero.isSpecialAction()) {
					pnt = bestMoveblaCell(GameScene.mainHero.getLocation());
					MoveCreator.setUseSpecial(false);
					isLastEndGame = false;
					moveTurn(pnt);
					return;
				}
				GameScene.mainHero.setTarget(target);
				GameScene.mainHero.useSpecial();
				MoveCreator.setUseSpecial(false);
				isLastEndGame = false;

				if (GameScene.mainHero instanceof Explorer && !isDiffused) {
					if (vaccinesLocations == null)
						vaccinesLocations = new ArrayList<>();
					if (suppliesLocations == null)
						suppliesLocations = new ArrayList<>();
					for (int i = 0; i < 15; i++) {
						for (int j = 0; j < 15; j++) {
							if (Game.map[i][j] instanceof CollectibleCell) {
								CollectibleCell cc = (CollectibleCell) Game.map[i][j];
								if (cc.getCollectible() instanceof Vaccine)
									vaccinesLocations.add(new Point(i, j));
								else
									suppliesLocations.add(new Point(i, j));

							}
						}
					}
					diffuse();

				}
			} else {
				pnt = bestMoveblaCell(GameScene.mainHero.getLocation());
				MoveCreator.setUseSpecial(false);
				isLastEndGame = false;
				moveTurn(pnt);
			}
		}

		else if (MoveCreator.isMoveable(pnt.x, pnt.y) && is4DirctionCell(GameScene.mainHero.getLocation().x,
				GameScene.mainHero.getLocation().y, pnt.x, pnt.y)) {
			moveTurn(pnt);
			isLastEndGame = false;

		} else if (Game.map[pnt.x][pnt.y] instanceof CharacterCell
				&& ((CharacterCell) Game.map[pnt.x][pnt.y]).getCharacter() instanceof Zombie) {
			GameScene.mainHero.setTarget(((CharacterCell) Game.map[pnt.x][pnt.y]).getCharacter());
			if (GameScene.mainHero.getVaccineInventory().size() > 0) {
				GameScene.mainHero.cure();
				isLastEndGame = false;
			} else {
				GameScene.mainHero.attack();
				isLastEndGame = false;
			}
		}

		else {
			int countRemActions = 0;
			Hero maxHero = null;
			int maxActions = 0;
			for (Hero hero : Game.heroes) {
				countRemActions += hero.getActionsAvailable();
				if (hero.getActionsAvailable() > maxActions) {
					maxActions = hero.getActionsAvailable();
					maxHero = hero;
				}
			}
			if (isLastEndGame || countRemActions > 2) {
				GameScene.mainHero = maxHero;
				pnt = bestMoveblaCell(GameScene.mainHero.getLocation());
				moveTurn(pnt);
				isLastEndGame = false;
			} else {

				Game.endTurn();
				isLastEndGame = true;
			}
		}

	}

	public static Point getBestMove(int depth) {

		GameScene.mainHero = null;
		Point retPoint = null;
		int mainHeroInd = -1;
		int curMax = Integer.MIN_VALUE;
		int cur = 0;

		// the part of handling the use of special power;
		int gainOfSpecialAction = Integer.MIN_VALUE;
		Hero specialHero = null;
		for (Hero hero : Game.heroes) {
			if (hero.getSupplyInventory().size() > 0) {
				if (hero instanceof Medic) {
					int var = gainOfMedicSpecialAction((Medic) hero);
					if (var > gainOfSpecialAction) {
						gainOfSpecialAction = var;
						specialHero = hero;
					}
				} else if (hero instanceof Explorer) {
					int var = gainOfExplorerSpecialAction((Explorer) hero) * 1000;
					if (var > gainOfSpecialAction) {
						gainOfSpecialAction = var;
						specialHero = hero;
					}
				} else {
					int var = gainOfFighterSpecialAction((Fighter) hero);
					if (var > gainOfSpecialAction) {
						gainOfSpecialAction = var;
						specialHero = hero;
					}
				}
			}
		}
		// the part for move,cure,attack
		for (Hero hero : Game.heroes) {

			heroInCalculation = hero;
			int x = hero.getLocation().x;
			int y = hero.getLocation().y;
			vis = new boolean[15][15];
			win = new int[15][15];
			int dpth = Math.min(depth, hero.getActionsAvailable());
			bfs(x, y, dpth);
			int heroMax = Integer.MIN_VALUE;
			int px = -1, py = -1;
			for (int i = 0; i < 8; i++) {
				if (isValid(x + di[i], y + dj[i])) {
					if (heroMax < win[x + di[i]][y + dj[i]]) {
						heroMax = win[x + di[i]][y + dj[i]];
						px = x + di[i];
						py = y + dj[i];
					}
				}
			}
			if (hero instanceof Explorer)
				heroMax += explorerBonus;
			heroMax += vaccineHolderBonus * hero.getVaccineInventory().size();
			if (heroMax > curMax) {
				mainHeroInd = cur;
				curMax = heroMax;
				retPoint = new Point(px, py);
			}
			cur++;

		}
		if (gainOfSpecialAction > curMax) {
			isUseSpecial = true;
			GameScene.mainHero = specialHero;
			return new Point(3423, 3242);
		}

		if (Game.heroes.size() == 0) {
			Game.endTurn();
		}
		GameScene.mainHero = Game.heroes.get(mainHeroInd);

		return retPoint;

	}

	private static void bfs(int x, int y, int dpth) {
		if (dpth <= 0)
			return;
		vis[x][y] = true;
		for (int i = 0; i < 4; i++) {
			int newx = x + dx[i], newy = y + dy[i];
			if (isValid(newx, newy) && !vis[newx][newy] && Game.map[newx][newy].isVisible()) {
				win[newx][newy] += solvePosition(newx, newy) * dpth;// if it is far, give it less interest
				if (isMoveable(newx, newy)) {
					bfs(newx, newy, dpth - 1);
					win[newx][newy] += NearToCenter(x, y, newx, newy);
					win[newx][newy] += 10 * heroesCondensed(newx, newy, x, y)
							* (heroInCalculation instanceof Medic ? -1 : 1);
					win[newx][newy] -= Game.countOfVisits[newx][newy];
					win[newx][newy] += nearCollectible[newx][newy];
//					win[newx][newy] += collectibleGain(newx, newy);

					if (Game.countOfVisits[newx][newy] == 0) {
						win[newx][newy] += 300;
					}

					if (Game.points.contains(new Point(newx, newy)))
						win[newx][newy] -= 15000000;

					win[x][y] += win[newx][newy];

				}

			}
		}

	}

	private static int solvePosition(int x, int y) {
		int winValue = 0;

		if (Game.map[x][y] instanceof CollectibleCell) {
			// more winning points to collecting;
			winValue = 90000000;

		} else if (Game.map[x][y] instanceof TrapCell) {
			// can enter
			winValue = 10;
		} else if (Game.map[x][y] instanceof CharacterCell) {
			CharacterCell cc = ((CharacterCell) Game.map[x][y]);
			if (cc.getCharacter() == null) {
				// can enter
				winValue = 10;
			}

			else if (cc.getCharacter() instanceof Hero) {

				if (heroInCalculation instanceof Medic && heroInCalculation.getSupplyInventory().size() > 0) {

					if (needsHealing(heroInCalculation) || needsHealing(cc.getCharacter())) {
						winValue = 3000;
					} else
						winValue = 10;
				} else {
					// cant enter the cell;
					winValue = -100;
				}

			} else {
				// zombie case;

				if (heroInCalculation.getVaccineInventory().size() > 0) {
					// can cure the zombie
					winValue = 30000000;
				} else { // doesnt have enough vaccines to cure

					if (cc.getCharacter().getCurrentHp() - heroInCalculation.getAttackDmg() <= 0) {
						// the attack will kill the zombie;
						if (isNotBadDamage(heroInCalculation)) {// will not be badly damaged
							winValue = 20;
						}

						else {
							winValue = 0;
						}
					} else {
						// the attack will not kill the zombie;
						if (isNotBadDamage(heroInCalculation))
							winValue = 2;
						else
							winValue = -10;

					}

				}
				if (heroInCalculation.getActionsAvailable() < 3) {
					winValue -= 20;
				}

			}

		}
		return winValue;
	}

	private static boolean isNotBadDamage(Hero heroInCalculation2) {
		return heroInCalculation.getCurrentHp() - 5 > 9
				&& heroInCalculation.getCurrentHp() * 2 > heroInCalculation.getMaxHp();
	}

	private static boolean needsHealing(Character character) {
		return 2 * character.getCurrentHp() < (character.getMaxHp());
	}

	private static boolean isValid(int x, int y) {
		return x > -1 && y > -1 && x < 15 && y < 15;
	}

	public static boolean getUseSpecial() {
		return isUseSpecial;
	}

	public static void setUseSpecial(boolean flg) {
		isUseSpecial = flg;
	}

	public static int NearToCenter(int x, int y, int newx, int newy) {
		int dif1 = Math.abs(7 - x) + Math.abs(7 - y);
		int dif2 = Math.abs(7 - newx) + Math.abs(7 - newy);
		return 100 * (dif1 - dif2);
	}

	public static int heroesCondensed(int x, int y, int lastx, int lasty) {
		int ans = 0;
		for (int i = 0; i < 8; i++) {
			if ((x + di[i] == lastx && y + dj[i] == lasty) || !isValid(x + di[i], y + dj[i])) {
				continue;
			}

			if (Game.map[x + di[i]][y + dj[i]].isVisible()) {
				if (Game.map[x + di[i]][y + dj[i]] instanceof CharacterCell) {
					CharacterCell cc = (CharacterCell) Game.map[x + di[i]][y + dj[i]];
					if (cc.getCharacter() instanceof Hero) {
						ans -= 10;
					}
				}
			}
		}
		return ans;
	}

	public static Point bestMoveblaCell(Point p) {
		int max = Integer.MIN_VALUE;
		int px = -1, py = -1;
		for (int i = 0; i < 4; i++) {
			int newx = p.x + dx[i];
			int newy = p.y + dy[i];
			if (isValid(newx, newy) && isMoveable(newx, newy)) {
				int gain = -Game.countOfVisits[newx][newy];
				gain += NearToCenter(p.x, p.y, newx, newy);
				if (gain > max) {
					max = gain;
					px = newx;
					py = newy;
				}
			}
		}
		return new Point(px, py);
	}

	public static void moveTurn(Point pnt) throws MovementException, NotEnoughActionsException {
		if (pnt.x - GameScene.mainHero.getLocation().x == 1) {
			GameScene.mainHero.move(Direction.DOWN);
		} else if (pnt.x - GameScene.mainHero.getLocation().x == -1) {
			GameScene.mainHero.move(Direction.UP);
		} else if (pnt.y - GameScene.mainHero.getLocation().y == 1) {
			GameScene.mainHero.move(Direction.RIGHT);
		} else if (pnt.y - GameScene.mainHero.getLocation().y == -1) {
			GameScene.mainHero.move(Direction.LEFT);
		}
	}

	public static boolean isMoveable(int newx, int newy) {
		if (Game.map[newx][newy] instanceof CollectibleCell || Game.map[newx][newy] instanceof TrapCell)
			return true;
		if (((CharacterCell) Game.map[newx][newy]).getCharacter() == null)
			return true;
		return false;
	}

	public static boolean is4DirctionCell(int x, int y, int newx, int newy) {
		return (Math.abs(newx - x) + Math.abs(newy - y)) < 2;
	}

	public static int gainOfExplorerSpecialAction(Explorer expl) {

		return 40000;
	}

	public static int gainOfFighterSpecialAction(Fighter fhtr) {
		int numOfZombiesAround = fhtr.getTargetChars().size();
		if (numOfZombiesAround == 0)
			return Integer.MIN_VALUE;
		return numOfZombiesAround;
	}

	public static int gainOfMedicSpecialAction(Medic medic) {
		int ans = 0;
		if (medic.getCurrentHp() * 2 < medic.getMaxHp())
			return 3000;
		for (Character c : medic.getTargetChars()) {
			if (c instanceof Hero) {
				if (c.getCurrentHp() * 2 < c.getMaxHp())
					return 3000;
			}
		}
		return ans;
	}

	public static void diffuse() {
		isDiffused = true;
		for (Point p : vaccinesLocations) {
			vis = new boolean[15][15];
			bfs(p.x, p.y, 80, 1);
		}
		for (Point p : suppliesLocations) {
			vis = new boolean[15][15];
			bfs(p.x, p.y, 50, 1);
		}
		explorerBonus = 0;

	}

	public static void bfs(int x, int y, int curGain, int mul) {
		if (suppliesLocations != null && vaccinesLocations != null) {
			vis[x][y] = true;
			nearCollectible[x][y] += curGain * curGain * mul;
//		if(Game.map[x][y] instanceof CharacterCell) {
//			CharacterCell cc= (CharacterCell)Game.map[x][y];
//			if(cc.getCharacter() instanceof Zombie) {
//				nearCollectible[x][y]-=20;
//			}
//		}
			for (int i = 0; i < 8; i++) {
				if (isValid(x + di[i], y + dj[i]) && !vis[x + di[i]][y + dj[i]]) {
					bfs(x + di[i], y + dj[i], curGain - 10 , mul);
				}
			}
		}
	}

//	public static int collectibleGain(int x, int y) {
//
//		if (Game.map[x][y] instanceof CollectibleCell) {
//			if (nearCollectible[x][y] == 0) {
//				vis = new boolean[15][15];
//				bfs(x, y, 900, 1);
//			}
//			return 1000;
//		}
//		return 0;
//	}

	public static Point collectibleMove() {
		int px = -1, py = -1;
		Hero bestHero = null;
		for (Hero hero : Game.heroes) {
			heroInCalculation = hero;
			if (hero.getActionsAvailable() > 0) {
				for (int i = 0; i < 4; i++) {
					int x = heroInCalculation.getLocation().x + dx[i];
					int y = heroInCalculation.getLocation().y + dy[i];
					if (!isValid(x, y))
						continue;
					if (Game.map[x][y] instanceof CollectibleCell) {
						Collectible collectible = ((CollectibleCell) Game.map[x][y]).getCollectible();
						if (collectible instanceof Vaccine) {
							return new Point(x, y);
						} else {
							bestHero = hero;
							px = x;
							py = y;
						}

					}
				}
			}
		}
		if (px == -1)
			return null;
		heroInCalculation = bestHero;
		return new Point(px, py);
	}

	public static void updateCollectible(Point p1) {
		if(isDiffused) {
			nearCollectible = new int[15][15];
			for(Point p2: vaccinesLocations) {
				if(p1.x == p2.x && p1.y==p2.y) {
					vaccinesLocations.remove(p2);
					break;
				}
			}
			for(Point p2: suppliesLocations) {
				if(p1.x == p2.x && p1.y==p2.y) {
					suppliesLocations.remove(p2);
					break;
				}
			}
			//for(int[] a:nearCollectible)System.out.println(Arrays.toString(a));
			diffuse();
			for(int[] a:nearCollectible)System.out.println(Arrays.toString(a));

		}
		
	}
	
}