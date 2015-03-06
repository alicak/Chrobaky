package sk.upjs.paz.chrobaky;

import java.awt.Color;

import sk.upjs.jpaz2.ImageShape;
import sk.upjs.jpaz2.Turtle;

public class Dievcatko extends Turtle {

	/**
	 * Rozmer bubliny pri zaciatku nafukovania
	 */
	public static final double POCIATOCNY_POLOMER_BUBLINY = 20;

	/**
	 * Rozmer dievcatka
	 */
	private static final int POLOMER_DIEVCATKA = 20;

	/**
	 * Aktualny pocet zivotov
	 */
	private int pocetZivotov;

	/**
	 * true ak je imunne, false ak nie je
	 */
	private boolean jeImunne;

	/**
	 * Pocet tikov, pocas ktorych je dievcatko imunne
	 */
	private double dlzkaImunity;

	/**
	 * true ak dievcatko fuka bublinu, false ak nie
	 */
	private boolean fukaBublinu;

	/**
	 * Polomer aktualne nafuknutej bubliny. Ak je rovny
	 * POCIATOCNY_POLOMER_BUBLINY, tak bublina nie je nafuknuta
	 */
	private double polomerBubliny;

	public Dievcatko() {
		pocetZivotov = 3;
		jeImunne = true;
		polomerBubliny = POCIATOCNY_POLOMER_BUBLINY;
		setShape(new ImageShape("images", "dievcatko.png"));
		setFillColor(new Color(255, 138, 197));
		penUp();
	}

	/**
	 * Vrati, ci sa dievcatko svojou plochou nachadza v bode so zadanymi
	 * suradnicami
	 * 
	 * @param x
	 *            x-suradnica bodu
	 * @param y
	 *            y-suradnica bodu
	 * @return true ak sa nachadza, false ak nie
	 */
	public boolean somTu(int x, int y) {
		return (distanceTo(x, y) < POLOMER_DIEVCATKA);
	}

	/**
	 * Vykona sa pri strate zivota
	 */
	public void stratilSaZivot() {
		pocetZivotov--;
		if (pocetZivotov != 0) {
			jeImunne = true;
			dlzkaImunity++;
		}
		setFukaBublinu(false);
	}

	/**
	 * Nastavi hodnoty ako v konstruktore
	 */
	public void resetujSa() {
		pocetZivotov = 3;
		jeImunne = true;
		polomerBubliny = POCIATOCNY_POLOMER_BUBLINY;
		dlzkaImunity = 0;
	}

	public int getPocetZivotov() {
		return pocetZivotov;
	}

	public void setPocetZivotov(int pocetZivotov) {
		this.pocetZivotov = pocetZivotov;
	}

	public boolean isJeImunne() {
		return jeImunne;
	}

	public void setJeImunne(boolean jeImunne) {
		this.jeImunne = jeImunne;
	}

	public double getDlzkaImunity() {
		return dlzkaImunity;
	}

	public void setDlzkaImunity(double dlzkaImunity) {
		this.dlzkaImunity = dlzkaImunity;
	}

	public double getPolomerBubliny() {
		return polomerBubliny;
	}

	public void setPolomerBubliny(double polomerBubliny) {
		this.polomerBubliny = polomerBubliny;
	}

	public boolean isFukaBublinu() {
		return fukaBublinu;
	}

	public void setFukaBublinu(boolean fukaBublinu) {
		this.fukaBublinu = fukaBublinu;
	}
}
