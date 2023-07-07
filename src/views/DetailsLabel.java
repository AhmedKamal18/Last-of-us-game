package views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import model.characters.Hero;

public class DetailsLabel extends Label {
	
	private Hero hero;
	public DetailsLabel(Hero hero) {
		super();
		this.hero = hero;
		this.setText(description());
		setFont(Font.font("Arial",FontWeight.BOLD, 20));
        setTextFill(Color.WHITE);
        setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0.7), new CornerRadii(20), Insets.EMPTY)));
        setPadding(new Insets(10));
        setPrefSize(250, 150);
        setWrapText(true);
        setTextAlignment(TextAlignment.CENTER);
        setAlignment(Pos.CENTER);
        setMouseTransparent(true);
	}
	
	
	public String description() {
		String s = "Name: ";
		s +=hero.getName() +"\n";
		s += "Type: " + hero.getType() + "\n";
		s += "Max HP: " + hero.getCurrentHp() + "\n";
		s += "AttackDamage: " + hero.getAttackDmg() + "\n";
		s += "Max Actions: " + hero.getActionsAvailable();
		return s;
	}
}
