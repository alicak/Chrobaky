package sk.upjs.paz.chrobaky;

import java.awt.Point;
import java.awt.geom.Point2D;

import sk.upjs.jpaz2.ImageShape;
import sk.upjs.jpaz2.Turtle;

public class Chrobak extends Turtle {

	// pomocne konstanty
	private static final double POLOMER_HRACEJ_PLOCHY = HraciaPlocha.POLOMER_PLOCHY_DIEVCATKA;
	private static final double POLOMER_POCHODZKY = HraciaPlocha.POLOMER_POCHODZKY;
	private static final double STRED = HraciaPlocha.STRED_X;

	/**
	 * Pocet vykonanych ciastkovych krokov v aktualnom nahodnom kroku
	 */
	private int pocetKrokov;

	/**
	 * Smer aktualneho nahodneho kroku
	 */
	private double smer;

	/**
	 * Pocet tikov, pocas ktorych je uz chrobak mrtvy
	 */
	private int akoDlhoJeChrobakMrtvy;

	/**
	 * Vygeneruje noveho ziveho chrobaka na nahodnych suradniciach
	 * 
	 * @param mimoPlochy
	 *            true ak ma byt vo viditelnej casti plochy, false ak nie
	 */
	public Chrobak(boolean mimoPlochy) {
		penUp();
		if (mimoPlochy) {
			setPosition(vygenerujSuradnice());
			directionTowards(STRED, STRED);
		} else {
			setX(Math.random() * POLOMER_HRACEJ_PLOCHY * 2 + 50);
			setY(Math.random() * POLOMER_HRACEJ_PLOCHY * 2 + 50);
		}
		nastavNahodnyObrazok();
	}

	/**
	 * Chrobak urobi nahodny krok v zadanej oblasti. Ak sa dostane mimo, vrati
	 * sa. Smer kroku sa meni po 10 krokoch.
	 * 
	 * @param stredX
	 *            x-suradnica stredu kruhu, v ktorom sa moze chrobak pohybovat
	 * @param stredY
	 *            y-suradnica stredu kruhu, v ktorom sa moze chrobak pohybovat
	 * @param polomer
	 *            polomer kruhu, v ktorom sa moze chrobak pohybovat
	 */
	public void nahodnyKrok(double stredX, double stredY, double polomer) {
		if (pocetKrokov % 10 == 0) {
			smer = vygenerujNahodneZnamienko() * (Math.random() * 60);
			turn(smer);
			pocetKrokov = 0;
		}
		step(1);
		// Ak sa nachadza v neviditelnej casti plochy, tak je tiez neviditelny
		if (distanceTo(stredX, stredY) > POLOMER_HRACEJ_PLOCHY) {
			setVisible(false);
		} else {
			setVisible(true);
		}
		if (distanceTo(stredX, stredY) >= polomer) {
			step(-1);
		}
		pocetKrokov++;
	}

	/**
	 * Skontroluje, ci sa chrobak nedotkol dievcatka alebo bubliny
	 * 
	 * @param dievcatko
	 *            dievcatko, ktore treba skontrolovat
	 * @param polomer
	 *            polomer bubliny
	 */
	public void skontroluj(Dievcatko dievcatko, double polomer) {
		if (distanceTo(dievcatko.getX(), dievcatko.getY()) < polomer) {
			int pocetZivotov = dievcatko.getPocetZivotov();
			// Ked je pocet zivotov 0, konci hra a vykonavaju sa ine prikazy
			if (pocetZivotov > 0) {
				dievcatko.stratilSaZivot();
			}
		}
	}

	/**
	 * Zmeni farbu mrtveho chrobaka
	 */
	public void chrobakZomrel() {
		setShape(new ImageShape("images", "chrobak-mrtvy.png"));
		akoDlhoJeChrobakMrtvy++;
	}

	/**
	 * Nastavi chrobakovi nahodny obrazok
	 */
	private void nastavNahodnyObrazok() {
		double nahodneCislo = Math.random() * 4;
		if (nahodneCislo < 1)
			setShape(new ImageShape("images", "chrobak-cerveny.png"));
		else if (nahodneCislo < 2)
			setShape(new ImageShape("images", "chrobak-zeleny.png"));
		else if (nahodneCislo < 3)
			setShape(new ImageShape("images", "chrobak-modry.png"));
		else if (nahodneCislo < 4)
			setShape(new ImageShape("images", "chrobak-oranzovy.png"));
	}

	/**
	 * Vygeneruje suradnice bodu v neviditelnej casti plochy
	 * 
	 * @return bod so ziadanymi suradnicami
	 */
	private static Point2D vygenerujSuradnice() {
		Point2D suradnice = new Point(1000, 1000);
		while ((suradnice.distance(STRED, STRED) < POLOMER_HRACEJ_PLOCHY)
				|| (suradnice.distance(STRED, STRED) > POLOMER_POCHODZKY)) {
			double x = Math.random() * POLOMER_POCHODZKY * 2;
			double y = Math.random() * POLOMER_POCHODZKY * 2;
			suradnice.setLocation(x, y);
		}
		return suradnice;
	}

	/**
	 * Nahodne vrati cislo -1 alebo 1
	 * 
	 * @return znamienko
	 */
	private static int vygenerujNahodneZnamienko() {
		double nahodneCislo = Math.random();
		if (nahodneCislo > 0.5)
			return 1;
		else
			return -1;
	}

	public int getAkoDlhoJeChrobakMrtvy() {
		return akoDlhoJeChrobakMrtvy;
	}

	public void setAkoDlhoJeChrobakMrtvy(int akoDlhoJeChrobakMrtvy) {
		this.akoDlhoJeChrobakMrtvy = akoDlhoJeChrobakMrtvy;
	}

}
