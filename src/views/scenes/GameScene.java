package views.scenes;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.shape.Line;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.characters.*;
import model.characters.Character;
import model.world.CharacterCell;
import views.AnimatedErrorMessage;
import views.DetailsLabel;
import views.Main;
import views.MapPane;
import views.RectanglePane;

import java.awt.Point;
import java.lang.module.ModuleDescriptor.Builder;
import java.util.Timer;
import java.util.TimerTask;

import ai.MoveCreator;
import engine.Game;
import exceptions.InvalidTargetException;
import exceptions.MovementException;
import exceptions.NoAvailableResourcesException;
import exceptions.NotEnoughActionsException;

public class GameScene extends Scene {
	private static final int GRID_SIZE = 15;
	private static final double RECTANGLE_SIZE = 40.0;
	public static MapPane gridPane;
	public static Hero mainHero;
	public static RectanglePane prevTargetRectangle = null;
	public static RectanglePane mainHeroRectangle = null;
	public static int cnt = 0; 
	public static boolean isAIMatch=false;

	public GameScene(Hero hero) {
		super(root(hero), Main.stage.getScene().getWidth(), Main.stage.getScene().getHeight());
	}
	
	private static void updateInfoBox(BorderPane borderPane) {
		ScrollPane heros = ((ScrollPane)(borderPane.getLeft()));		
		heros.setContent(createInfoBox());
		heros.setPrefWidth(200);
		heros.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		heros.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
	}

	public static BorderPane root(Hero hero) {
		
		gridPane = new MapPane();
		VBox infoBox = createInfoBox();
		infoBox.setPrefWidth(200);
		BorderPane borderPane = new BorderPane();
		StackPane stackPane = new StackPane();
		stackPane.getChildren().add(gridPane);
		borderPane.setCenter(stackPane);
		ScrollPane heros = new ScrollPane();
		heros.setContent(infoBox);
		heros.setPrefWidth(Main.stage.getWidth() - gridPane.getWidth());
		heros.setPrefHeight(Main.stage.getHeight());
		heros.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		heros.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		borderPane.setLeft(heros);
		borderPane.setFocusTraversable(true);
        Platform.runLater(() -> borderPane.requestFocus());
		
		Game.startGame(hero);
		mainHero = hero;
		
		int x1 = mainHero.getLocation().x;
		int y1 = mainHero.getLocation().y;
		mainHeroRectangle = ((RectanglePane) gridPane.getChildren().get(y1 * 15 + x1));
		mainHeroRectangle.setBorder(new Border(
					new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
		updateInfoBox(borderPane);
		if(isAIMatch) {
				isAIMatch = false;
				Timer t = new Timer();
		        t.scheduleAtFixedRate(new TimerTask() {
		          @Override
		          public void run() {
		            // TODO Auto-generated method stub
		            if (Game.checkGameOver()) {
		            	 t.cancel();
		            	if (Game.checkWin()) {
	    					Main.stage.getScene().setRoot(WinScene.root());
	    				} else {
	    					Main.stage.getScene().setRoot(LossScene.root());
	    				}
		             
		              return;
		            }
		            Platform.runLater(() -> {
		              try {
		                MoveCreator.AIGame();
		            	updateInfoBox(borderPane);
		    			Hero.updateTargetCharacters();
		    			int x2 = mainHero.getLocation().x;
		    			int y2 = mainHero.getLocation().y;
		    			mainHeroRectangle.setBorder(null);
		    			mainHeroRectangle = ((RectanglePane) gridPane.getChildren().get(y2 * 15 + x2));
		    			mainHeroRectangle.setBorder(new Border(
		    						new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
		    			
		              } catch (NoAvailableResourcesException | InvalidTargetException | NotEnoughActionsException
		                  | MovementException | InterruptedException e) {
		                // TODO Auto-generated catch block
		               
		              }
		            });
		          }
		        }, 0, 300);
			}
		
		else {
		borderPane.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.LEFT) {
				try {
					mainHero.move(Direction.LEFT);
				} catch (MovementException | NotEnoughActionsException e) {
					// TODO popup window
					if (!AnimatedErrorMessage.active)
						AnimatedErrorMessage.animateErrorMessage(e.getMessage(),
								(StackPane) (borderPane).getCenter());
				}
			} else if (event.getCode() == KeyCode.RIGHT) {
				try {
					mainHero.move(Direction.RIGHT);
				} catch (MovementException | NotEnoughActionsException e) {
					// TODO popup window
					if (!AnimatedErrorMessage.active)
						AnimatedErrorMessage.animateErrorMessage(e.getMessage(),
								(StackPane) (borderPane).getCenter());
				}
			} else if (event.getCode() == KeyCode.UP) {
				try {
					mainHero.move(Direction.UP);
				} catch (MovementException | NotEnoughActionsException e) {
					// TODO popup window
					if (!AnimatedErrorMessage.active)
						AnimatedErrorMessage.animateErrorMessage(e.getMessage(),
								(StackPane) (borderPane).getCenter());
				}
			} else if (event.getCode() == KeyCode.DOWN) {
				try {
					mainHero.move(Direction.DOWN);
				} catch (MovementException | NotEnoughActionsException e) {
					// TODO popup window
					if (!AnimatedErrorMessage.active)
						AnimatedErrorMessage.animateErrorMessage(e.getMessage(),
								(StackPane) (borderPane).getCenter());
				}
			} else if (event.getCode() == KeyCode.A) {
				// attack
				try {
					mainHero.attack();
				} catch (InvalidTargetException | NotEnoughActionsException e) {
					// TODO Auto-generated catch block
					if (!AnimatedErrorMessage.active)
						AnimatedErrorMessage.animateErrorMessage(e.getMessage(),
								(StackPane) (borderPane).getCenter());
				}
			} else if (event.getCode() == KeyCode.C) {
				// cure | heal
				try {
					mainHero.cure();
					prevTargetRectangle.setBorder(null);
				} catch (NoAvailableResourcesException | InvalidTargetException | NotEnoughActionsException e) {
					// TODO Auto-generated catch block
					if (!AnimatedErrorMessage.active)
						AnimatedErrorMessage.animateErrorMessage(e.getMessage(),
								(StackPane) (borderPane).getCenter());
				}
			} else if (event.getCode() == KeyCode.S) {
				// useSpecial
				try {
					mainHero.useSpecial();
				} catch (NoAvailableResourcesException | InvalidTargetException e) {
					// TODO Auto-generated catch block
					if (!AnimatedErrorMessage.active)
						AnimatedErrorMessage.animateErrorMessage(e.getMessage(),
								(StackPane) (borderPane.getCenter()));
				}
			} else if (event.getCode() == KeyCode.E) {
				// endTurn
				Game.endTurn();
//				Game.printZombies();
			} else if (event.getCode() == KeyCode.R && mainHero != null) {
				Hero.updateTargetCharacters();
				if (mainHero.getTargetChars().size() != 0) {
					
					cnt %= mainHero.getTargetChars().size();

					if (GameScene.prevTargetRectangle != null) {
						GameScene.prevTargetRectangle.setBorder(null);
					}

					int x = mainHero.getTargetChars().get(cnt).getLocation().x;
					int y = mainHero.getTargetChars().get(cnt).getLocation().y;
					((RectanglePane) gridPane.getChildren().get(y * 15 + x))
							.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID,
									CornerRadii.EMPTY, new BorderWidths(3))));
					Character character = mainHero.getTargetChars().get(cnt);
					mainHero.setTarget(character);
					prevTargetRectangle = ((RectanglePane) gridPane.getChildren().get(y * 15 + x));

					cnt++;
				}

			} else if (event.getCode() == KeyCode.SPACE) {
				if (mainHero != null) {
					int curHeroInd = Game.heroes.indexOf(mainHero);
					if(Game.heroes.size()>0)
						mainHero = Game.heroes.get((curHeroInd + 1) % Game.heroes.size());
				} else if (Game.heroes.size() > 0) {
					mainHero = Game.heroes.get(0);
				}
			} 
			updateInfoBox(borderPane);
			Hero.updateTargetCharacters();
			int x2 = mainHero.getLocation().x;
			int y2 = mainHero.getLocation().y;
			mainHeroRectangle.setBorder(null);
			mainHeroRectangle = ((RectanglePane) gridPane.getChildren().get(y2 * 15 + x2));
			mainHeroRectangle.setBorder(new Border(
						new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
			if (Game.checkGameOver()) {
				if (Game.checkWin()) {
					Main.stage.getScene().setRoot(WinScene.root());
				} else {
					Main.stage.getScene().setRoot(LossScene.root());
				}
			}
		});
		
		}
		
		return borderPane;
	}

	private static VBox createInfoBox() {
		VBox info = new VBox();
		info.setSpacing(10);
		info.setBackground(Background.fill(Color.TRANSPARENT));
		for (Hero hero : Game.heroes) {
			VBox vbox = new VBox();
			vbox.setPadding(new Insets(10));
			vbox.setSpacing(10);
			vbox.setBackground(
					new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0.7), new CornerRadii(20), Insets.EMPTY)));
			Label l = new Label();
			l.setText(description(hero));
			l.setFont(Font.font("Arial", 14));
			l.setTextFill(Color.CYAN);
			l.setMaxSize(250, 150);
			l.setPadding(new Insets(10));
			if (hero == mainHero) {
				l.setTextFill(Color.RED);
				vbox.setBackground(
						new Background(new BackgroundFill(Color.rgb(0, 0, 0, 1), new CornerRadii(20), Insets.EMPTY)));
			}
			vbox.getChildren().add(l);
			ProgressBar hpBar = new ProgressBar();
			updateHPBarStyle(hpBar, hero.getCurrentHp(), hero.getMaxHp());
			StackPane hpStack = new StackPane();
			Label hp = new Label("HP");
			hpStack.getChildren().addAll(hpBar, hp);
			
			ProgressBar actionsBar = new ProgressBar();
			updateActionsBarStyle(actionsBar, hero.getActionsAvailable(), hero.getMaxActions());
			StackPane actionsStack = new StackPane();
			Label actions = new Label("ACTIONS");
			actionsStack.getChildren().addAll(actionsBar, actions);
			vbox.getChildren().addAll(hpStack, actionsStack);
			info.getChildren().add(vbox);
		}
		return info;
	}
	
	
	private static void updateActionsBarStyle(ProgressBar actionsBar, double actionsAvailable, double MAX_ACTIONS) {
		double actionsPercentage = actionsAvailable / MAX_ACTIONS;
		Color color = getColorForHPPercentage(actionsPercentage);

		// Update the CSS style dynamically based on the HP percentage
		actionsBar.setStyle("-fx-accent: blue;");
		actionsBar.setProgress(actionsPercentage);
	}

	private static void updateHPBarStyle(ProgressBar hpBar, double currentHP, double MAX_HP) {
		double hpPercentage = currentHP / MAX_HP;
		Color color = getColorForHPPercentage(hpPercentage);

		// Update the CSS style dynamically based on the HP percentage
		hpBar.setStyle("-fx-accent: " + toRGBCode(color) + ";");
		hpBar.setProgress(hpPercentage);
	}
	

	private static Color getColorForHPPercentage(double hpPercentage) {
		if (hpPercentage >= 0.7) {
			return Color.GREEN;
		} else if (hpPercentage >= 0.4) {
			return Color.YELLOW;
		} else {
			return Color.RED;
		}
	}

	private static String toRGBCode(Color color) {
		return String.format("#%02X%02X%02X", (int) (color.getRed() * 255), (int) (color.getGreen() * 255),
				(int) (color.getBlue() * 255));
	}

	public static void setMainHero(Hero hero) {
		mainHero = hero;

	}

	public static String description(Hero hero) {
		String s = "";
		s += "Name: " + hero.getName() + "\n";
		s += "Type: " + hero.getType() + "\n";
		s += "Attack Damage: " + hero.getAttackDmg() + "\n";
		s += "Vaccines: " + hero.getVaccineInventory().size();
		s += ",Supplies: " + hero.getSupplyInventory().size();
		return s;
	}
	

}