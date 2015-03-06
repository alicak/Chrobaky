package sk.upjs.paz.chrobaky;

import java.awt.Font;

import sk.upjs.jpaz2.Pane;
import sk.upjs.jpaz2.Turtle;

public class UkazovatelSkore extends Pane {

	public UkazovatelSkore() {
		super(120, 40);
		setBorderWidth(0);
		setTransparentBackground(true);
		nastavSkore(0);
	}

	/**
	 * Vypise aktualne skore
	 * 
	 * @param skore
	 */
	public void nastavSkore(int skore) {
		clear();
		Turtle pomocnicka = new Turtle();
		pomocnicka.setVisible(false);
		add(pomocnicka);
		pomocnicka.setFont(new Font("Lucida Sans", Font.BOLD, 20));
		pomocnicka.turn(90);
		pomocnicka.center();
		pomocnicka.printCenter(Integer.toString(skore));
	}

	/**
	 * Nastavi skore na nulu
	 */
	public void vynulujSa() {
		nastavSkore(0);
	}

}
