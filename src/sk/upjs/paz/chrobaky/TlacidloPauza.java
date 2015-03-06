package sk.upjs.paz.chrobaky;

import sk.upjs.jpaz2.ImageShape;
import sk.upjs.jpaz2.Pane;
import sk.upjs.jpaz2.Turtle;

public class TlacidloPauza extends Pane {

	public TlacidloPauza() {
		super(40, 40);
		setBorderWidth(0);
		setTransparentBackground(true);

		// Defaultne nastavenie tlacidla je beziaca hra
		play();
	}

	/**
	 * Pri pozastaveni zmeni tlacitko na play
	 */
	public void pause() {
		clear();
		Turtle pomocnicka = new Turtle();
		add(pomocnicka);
		pomocnicka.center();
		pomocnicka.setShape(new ImageShape("images", "play.png"));
		pomocnicka.stamp();
		remove(pomocnicka);
	}

	/**
	 * Pri spusteni zmeni tlacitko na pause
	 */
	public void play() {
		clear();
		Turtle pomocnicka = new Turtle();
		add(pomocnicka);
		pomocnicka.center();
		pomocnicka.setShape(new ImageShape("images", "pause.png"));
		pomocnicka.stamp();
		remove(pomocnicka);
	}
}
