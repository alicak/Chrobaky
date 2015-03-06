package sk.upjs.paz.chrobaky;

import sk.upjs.jpaz2.ImageShape;
import sk.upjs.jpaz2.Pane;
import sk.upjs.jpaz2.Turtle;

public class UkazovatelZivotov extends Pane {

	public UkazovatelZivotov(int pociatocnyPocetZivotov) {
		super(120, 40);
		setBorderWidth(0);
		setTransparentBackground(true);
		nastavZivoty(pociatocnyPocetZivotov);
	}

	/**
	 * Vykresli pocet srdiecok rovnaky ako aktualny pocet zivotov
	 * 
	 * @param pocetZivotov
	 */
	public void nastavZivoty(int pocetZivotov) {
		clear();
		Turtle srdiecko = new Turtle();
		add(srdiecko);
		srdiecko.setShape(new ImageShape("images", "zivot.png"));
		srdiecko.setPosition(100, 20);
		for (int i = 1; i <= pocetZivotov; i++) {
			srdiecko.stamp();
			srdiecko.setPosition(srdiecko.getX() - 40, 20);
		}
		remove(srdiecko);
	}

	/**
	 * Nastavi pocet zivotov na pociatocnu hodnotu (3)
	 */
	public void vynulujSa() {
		nastavZivoty(3);
	}
}
