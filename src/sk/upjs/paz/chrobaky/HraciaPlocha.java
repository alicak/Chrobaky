package sk.upjs.paz.chrobaky;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import sk.upjs.jpaz2.AudioClip;
import sk.upjs.jpaz2.ImageShape;
import sk.upjs.jpaz2.Pane;
import sk.upjs.jpaz2.Turtle;

public class HraciaPlocha extends Pane {

	/**
	 * Polomer plochy, po ktorej sa pohybuje dievcatko
	 */
	public static final double POLOMER_PLOCHY_DIEVCATKA = 250;

	/**
	 * Polomer plochy, po ktorej lezu chrobaci
	 */
	public static final double POLOMER_POCHODZKY = 300;

	/**
	 * Suradnice stredu plochy
	 */
	public static final double STRED_X = 300;
	public static final double STRED_Y = 300;

	/**
	 * Cislo, ktorym sa vynasobi polomer nafuknutej bubliny a v tomto okruhu
	 * budu chrobaky znicene
	 */
	private static final double NASOBOK_BUBLINY = 1.5;

	/**
	 * Pocet tikov, pocas ktorych bude zobrazeny mrtvy chrobak
	 */
	private static final int DLZKA_ZOBRAZENIA_MRTVEHO_CHROBAKA = 15;

	/**
	 * True ak hra bezi
	 */
	private boolean hraBezi;

	/**
	 * True ak je hra pozastavena
	 */
	private boolean jePauza;

	/**
	 * Dievcatko, ktore sa bude pohybovat v ploche
	 */
	private Dievcatko dievcatko;

	/**
	 * Zoznam vsetkych zivych chrobakov v ploche
	 */
	private List<Chrobak> ziveChrobaky;

	/**
	 * Zoznam mrtvych chrobakov, ktore este neboli odstranene z plochy
	 */
	private List<Chrobak> mrtveChrobaky;

	/**
	 * Maximalny pocet chrobakov, ktore sa v jednej chvili mozu nachadzat v
	 * hracej ploche
	 */
	private int maxPocetChrobakov;

	/**
	 * Skore v hre
	 */
	private int skore;

	/**
	 * Skore pri poslednom navyseni poctu chrobavko
	 */
	private int posledneSkorePredNavysenimMax;

	/**
	 * Pane ukazujuci aktualne skore
	 */
	private UkazovatelSkore ukazovatelSkore;

	/**
	 * Pane ukazujuci aktualny pocet zivotov
	 */
	private UkazovatelZivotov ukazovatelZivotov;

	/**
	 * Tlacidlo pre pozastavenie hry
	 */
	private TlacidloPauza tlacidloPauza;

	/**
	 * Tlacidlo pre navrat na uvodnu obrazovku
	 */
	private TlacidloSpat tlacidloSpat;

	/**
	 * Tlacidlo pre zapnutie/vypnutie hudby
	 */
	private TlacidloZvuk tlacidloZvuk;

	/**
	 * Plocha, ktora zobrazuje oznam o konci hry
	 */
	private KoniecHry koniecHry;

	/**
	 * Zvuk, ktory sa prehra pri prasknuti bubliny
	 */
	private AudioClip bublinaPraskla;

	/**
	 * True ak v predchadzajucom tiku dievcatko fukalo bublinu
	 */
	private boolean fukalaSaBublina;

	/**
	 * True ak plocha caka na vykreslenie pozadia po nakresleni koncovej bubliny
	 */
	private boolean cakamNaPozadie;

	/**
	 * Pocet tikov, ktore plocha caka na vykreslenie pozadia
	 */
	private int akoDlhoCakamNaPozadie;

	/**
	 * Pocet odstranenych chrobakov v poslednej bubline
	 */
	private int pocetOdstranenychChrobakov;

	/**
	 * Vytvori plochu aj s dievcatkom
	 * 
	 * @param velkost
	 *            strana stvorcovej hracej plochy
	 */
	public HraciaPlocha() {
		super(600, 600);
		vykresliPozadie();

		// Vytvori a umiestni tlacidlo spat
		tlacidloSpat = new TlacidloSpat(30, 80);
		add(tlacidloSpat);

		// Vytvori a umiestni ukazovatel skore
		ukazovatelSkore = new UkazovatelSkore();
		ukazovatelSkore.setPosition(480, 0);
		add(ukazovatelSkore);

		// Vytvori a umiestni tlacidlo zvuku
		tlacidloZvuk = new TlacidloZvuk();
		add(tlacidloZvuk);

		// Vytvori zvuk, ktory sa bude prehravat pri prasknuti bubliny
		bublinaPraskla = new AudioClip("audio", "bublina-praskla.wav");

		// Vytvori zoznamy chrobakov
		ziveChrobaky = new ArrayList<>();
		mrtveChrobaky = new ArrayList<>();

		// Vytvori a umiestni dievcatko
		dievcatko = new Dievcatko();
		add(dievcatko);

		// Vytvori a umiestni ukazovatel zivotov
		ukazovatelZivotov = new UkazovatelZivotov(dievcatko.getPocetZivotov());
		ukazovatelZivotov.setPosition(480, 40);
		add(ukazovatelZivotov);

		// Vytvori a umiestni tlacidlo pre pozastavenie hry
		tlacidloPauza = new TlacidloPauza();
		tlacidloPauza.setPosition(520, 90);
		add(tlacidloPauza);
	}

	/**
	 * Nastavi tlacidlo zvuku na aktualny stav
	 */
	public void pridajTlacidloZvuk() {
		if (HraChrobaky.hraHudba) {
			tlacidloZvuk.obrazokZapnuty();
		} else {
			tlacidloZvuk.obrazokVypnuty();
		}
	}

	/**
	 * Zacne novu hru
	 */
	public void zacniHru() {
		hraBezi = true;
		jePauza = false;

		// Vynuluje sa skore
		ukazovatelSkore.vynulujSa();
		skore = 0;

		// Pocet zivotov v ukazovateli zivotov sa nastavi na povodny
		ukazovatelZivotov.vynulujSa();

		// Dievcatko sa resetuje na hodnoty, ktore malo pri svojom vzniku a
		// umiestni sa do stredu plochy
		dievcatko.resetujSa();
		dievcatko.setPosition(STRED_X, STRED_Y);
		add(dievcatko);

		// Maximalny pocet chrobakov sa nastavi na povodny
		maxPocetChrobakov = 50;

		// Odstrani oznamenie o konci hry
		remove(koniecHry);

		// Vykresli sa pozadie hry (po skonceni predchadzajucej hry tam zostalo
		// pozadie Koniec hry)
		vykresliPozadie();

		// V ploche sa vygeneruje 15 chrobakov na nahodnej pozicii
		generujChrobakyNaZaciatku(15);

		// Nastavi periodu tikania plochy na 30 ms
		setTickPeriod(30);
	}

	/**
	 * Ukonci hru
	 */
	private void koniecHry() {
		hraBezi = false;
		jePauza = false;
		vyprazdniPlochu();
		remove(koniecHry);
		// Koniec hry a najvyssie skore sa nevypisu v pripade, ze uzivatel sa
		// predcasne vracia na uvodnu obrazovku
		if (dievcatko.getPocetZivotov() == 0) {
			nakresliKoniecHry();
			zapisNajvyssieSkore();
		}
		ziveChrobaky.clear();
		mrtveChrobaky.clear();
		setTickPeriod(0);
	}

	/**
	 * Pozastavi hru
	 */
	private void pause() {
		tlacidloPauza.pause();
		setTickPeriod(0);
		jePauza = !jePauza;
	}

	/**
	 * Znovu spusti hru
	 */
	private void play() {
		tlacidloPauza.play();
		setTickPeriod(30);
		jePauza = !jePauza;
	}

	/**
	 * Dievcatko sa hybe za mysou
	 */
	@Override
	protected void onMouseMoved(int x, int y, MouseEvent detail) {
		if (!jePauza && hraBezi) {
			Turtle pomocnicka = new Turtle(x, y);
			if (pomocnicka.distanceTo(STRED_X, STRED_Y) <= POLOMER_PLOCHY_DIEVCATKA - 10) {
				dievcatko.moveTo(x, y);
			}
		}
	}

	/**
	 * Pri kliknuti na tlacidloPauza sa hra pozastavi/znova spusti. Pri kliknuti
	 * na tlacidlo spat sa hra ukonci a zobrazi sa uvodna obrazovka
	 */
	@Override
	protected void onMouseClicked(int x, int y, MouseEvent detail) {
		if (tlacidloPauza.containsPoint(x, y) && hraBezi) {
			if (jePauza) {
				play();
			}

			else {
				pause();
			}

		}

		if (tlacidloSpat.klikloSaNaMna(x, y)) {
			if (jePauza) {
				play();
			}
			koniecHry();
			HraChrobaky.prepniNaUvodnuObrazovku();
		}
	}

	/**
	 * Pri stlaceni mysi sa zacne nafukovat bublina
	 */
	@Override
	protected void onMousePressed(int x, int y, MouseEvent detail) {
		// Bublina sa moze nafukovat iba ak dievcatko nema imunitu a hra nie je
		// pozastavena
		if (dievcatko.somTu(x, y) && !dievcatko.isJeImunne() && !jePauza
				&& hraBezi) {
			dievcatko.setFukaBublinu(true);
		}
	}

	/**
	 * Pri pusteni tlacitka mysi nafuknuta bublina praskne a znici chrobakov
	 */
	@Override
	protected void onMouseReleased(int x, int y, MouseEvent detail) {
		if (!jePauza && hraBezi) {
			dievcatko.setFukaBublinu(false);
		}
	}

	/**
	 * Pri stlaceni klavesu P sa hra pozastavi/znova spusti. Pri stlaceni
	 * klavesu M sa hra ukonci a vrati na uvodnu obrazovku
	 */
	@Override
	protected void onKeyPressed(KeyEvent e) {
		if ((e.getKeyCode() == KeyEvent.VK_P) && hraBezi) {
			if (jePauza) {
				play();
			}

			else {
				pause();
			}
		}

		if (e.getKeyCode() == KeyEvent.VK_U) {
			HraChrobaky.prepniNaUvodnuObrazovku();
		}
	}

	/**
	 * Vykresli bublinu o 1 pixel vacsiu ako predchadzajuca
	 */
	private void fukajBublinu() {
		if (!dievcatko.isJeImunne()
				&& ((dievcatko.getPolomerBubliny() + dievcatko.distanceTo(
						STRED_X, STRED_Y)) < POLOMER_PLOCHY_DIEVCATKA + 10)) {
			dievcatko.dot(dievcatko.getPolomerBubliny());
			dievcatko.setPolomerBubliny(dievcatko.getPolomerBubliny() + 1);
		} else {
			dievcatko.setFukaBublinu(false);
		}
	}

	/**
	 * Z prasknutej bubliny sa odstrania znicene chrobaky a navysi sa skore
	 */
	private void skonciFukanieBubliny() {
		if (HraChrobaky.hraHudba) {
			bublinaPraskla.playAsActionSound();
		}
		// Ak je dievcatko imunne, znamena to, ze bublina praskla kvoli
		// narazeniu do chrobaka a body sa neziskavaju
		if (!dievcatko.isJeImunne()) {
			koncovaBublina();
			odstranChrobakyVBubline();
			navysSkore(pocetOdstranenychChrobakov);
		}
		dievcatko.setPolomerBubliny(Dievcatko.POCIATOCNY_POLOMER_BUBLINY);
		pocetOdstranenychChrobakov = 0;
	}

	/**
	 * Nakresli koncovu bublinu, ktora oznacuje plochu, v ktorej boli znicene
	 * chrobaky
	 */
	private void koncovaBublina() {
		Color povodnaFarba = dievcatko.getFillColor();
		dievcatko.setFillColor(Color.red);
		dievcatko.dot(dievcatko.getPolomerBubliny() * NASOBOK_BUBLINY);
		dievcatko.setFillColor(povodnaFarba);
	}

	/**
	 * Navysi skore podla poctu odstranenych chrobakov
	 * 
	 * @param pocetOdstranenychChrobakov
	 *            pocet chrobakov, ktore boli odstranene pri prasknuti aktualnej
	 *            bubliny
	 */
	private void navysSkore(int pocetOdstranenychChrobakov) {
		skore = skore + pocetOdstranenychChrobakov * 10
				+ (pocetOdstranenychChrobakov / 3) * 10
				+ (pocetOdstranenychChrobakov / 5) * 20;
	}

	/**
	 * Odstrani vsetky zo zoznamu zive chrobaky v bubline a prida ich do zoznamu
	 * mrtvych chrobakov
	 */
	private void odstranChrobakyVBubline() {
		Iterator<Chrobak> it = ziveChrobaky.iterator();
		while (it.hasNext()) {
			Chrobak chrobak = it.next();
			if ((chrobak.distanceTo(dievcatko.getPosition()) < dievcatko
					.getPolomerBubliny() * NASOBOK_BUBLINY + 5)
					&& (chrobak.isVisible())) {
				pocetOdstranenychChrobakov++;
				chrobak.chrobakZomrel();
				mrtveChrobaky.add(chrobak);
				it.remove();
			}
		}
	}

	/**
	 * Odstrani zo zoznamu mrtych chrobakov vsetky, ktore boli zobrazene dlhsie
	 * ako DLZKA_ZOBRAZENIA_MRTVEHO_CHROBAKA. Zobrazenym chrobakom navysi
	 * premennu uchovavajucu dlzku zobrazenia
	 */

	private void odstranMrtveChrobaky() {
		Iterator<Chrobak> it = mrtveChrobaky.iterator();
		while (it.hasNext()) {
			Chrobak chrobak = it.next();
			if (chrobak.getAkoDlhoJeChrobakMrtvy() == DLZKA_ZOBRAZENIA_MRTVEHO_CHROBAKA) {
				remove(chrobak);
				it.remove();
			} else {
				chrobak.setAkoDlhoJeChrobakMrtvy(chrobak
						.getAkoDlhoJeChrobakMrtvy() + 1);
			}
		}
	}

	/**
	 * Vygeneruje noveho ziveho chrobaka a prida ho do zoznamu
	 * 
	 * @param jeVonku
	 *            urcuje, ci sa ma novy chrobak nachadzat mimo viditelnej plochy
	 */
	private void generujChrobaka(boolean jeVonku) {
		Chrobak novyChrobak = new Chrobak(jeVonku);
		ziveChrobaky.add(novyChrobak);
		add(novyChrobak);
	}

	/**
	 * Vygeneruje chrobaky, ktore sa budu na zaciatku hry nachadzat v ploche
	 * 
	 * @param pocet
	 *            pocet chrobakov, ktore sa maju vygenerovat
	 */
	private void generujChrobakyNaZaciatku(int pocet) {
		for (int i = 0; i < pocet; i++) {
			generujChrobaka(false);
		}
	}

	/**
	 * Vykresli pozadie plochy
	 */
	private void vykresliPozadie() {
		Turtle kreslic = new Turtle();
		kreslic.setShape(new ImageShape("images", "pozadieHracejPlochy.jpg"));
		add(kreslic);
		kreslic.center();
		kreslic.stamp();
		remove(kreslic);
	}

	/**
	 * Odstrani z plochy vsetky chrobaky a pripadnu bublinu
	 */
	private void vyprazdniPlochu() {
		for (Chrobak chrobak : ziveChrobaky) {
			remove(chrobak);
		}
		for (Chrobak chrobak : mrtveChrobaky) {
			remove(chrobak);
		}
		remove(dievcatko);
		vykresliPozadie();
	}

	/**
	 * Nakresli obrazok Koniec hry a pripadny oznam o najvyssom skore
	 */
	private void nakresliKoniecHry() {
		koniecHry = new KoniecHry(jeNajvyssieSkore());
		add(koniecHry);
		koniecHry.setPosition(150, 100);
	}

	/**
	 * Precita zo suboru najvyssie skore
	 * 
	 * @return najvyssie skore
	 */
	private int zistiNajvyssieSkore() {
		int najSkore = 0;
		Scanner citac = null;
		try {
			citac = new Scanner(new File("najSkore.txt"));
			if (citac.hasNextInt()) {
				najSkore = citac.nextInt();
			}
		} catch (FileNotFoundException e) {
			System.err
					.println("Subor s najvyssim skore sa nepodarilo otvorit.");
		} finally {
			if (citac != null)
				citac.close();
		}
		return najSkore;
	}

	/**
	 * Ak je skore najvyssie, zapise ho do suboru
	 */
	private void zapisNajvyssieSkore() {
		if (jeNajvyssieSkore()) {
			PrintWriter pw = null;
			try {
				pw = new PrintWriter(new File("najSkore.txt"));
				pw.print(skore);
			} catch (FileNotFoundException e) {
				System.err
						.println("Subor s najvyssim skore sa nepodarilo otvorit.");
			} finally {
				if (pw != null)
					pw.close();
			}
		}
	}

	/**
	 * True ak je aktualne skore vyssie ako najvyssie
	 * 
	 * @return
	 */
	private boolean jeNajvyssieSkore() {
		return zistiNajvyssieSkore() < skore;
	}

	/**
	 * Vykona sa pri kazdom tiku plochy
	 */
	protected void onTick() {

		// Fukanie alebo skoncenie fukania bubliny
		if (dievcatko.isFukaBublinu()) {
			fukajBublinu();
			fukalaSaBublina = true;
		} else if (!dievcatko.isFukaBublinu() && fukalaSaBublina) {
			skonciFukanieBubliny();
			ukazovatelSkore.nastavSkore(skore);
			cakamNaPozadie = true;
			akoDlhoCakamNaPozadie++;
			fukalaSaBublina = false;
		}

		// Vykreslenie pozadia po zobrazeni koncovej bubliny
		if (cakamNaPozadie) {
			if (akoDlhoCakamNaPozadie == 15) {
				vykresliPozadie();
				cakamNaPozadie = false;
				akoDlhoCakamNaPozadie = 0;
			} else {
				akoDlhoCakamNaPozadie++;
			}
		}

		// Odstranenie mrtvych chrobakov, ktore sa uz nezobrazuju
		odstranMrtveChrobaky();

		// Navysenie maximalneho poctu chrobakov v ploche, ak od posledneho
		// navysenia hrac ziskal viac ako 200 bodov
		if (skore - posledneSkorePredNavysenimMax > 200) {
			maxPocetChrobakov = maxPocetChrobakov + 8;
			posledneSkorePredNavysenimMax = skore;
		}

		// Vygenerovanie novych chrobakov, aby sa ich pocet rovnal maximalnemu
		while (ziveChrobaky.size() < maxPocetChrobakov) {
			generujChrobaka(true);
		}

		for (Chrobak chrobak : ziveChrobaky) {

			// Chrobak sa pohne
			chrobak.nahodnyKrok(STRED_X, STRED_Y, POLOMER_POCHODZKY);

			// Ak dievcatko nie je imunne, skontroluje, ci sa chrobak nedotkol
			// jej alebo bubliny
			if (!dievcatko.isJeImunne()) {
				chrobak.skontroluj(dievcatko, dievcatko.getPolomerBubliny());
			}

			// Ak je imunne, cas v imunnom stave sa navysi
			else if (dievcatko.getDlzkaImunity() < (50 * ziveChrobaky.size())) {
				dievcatko.setDlzkaImunity(dievcatko.getDlzkaImunity() + 1);
				// Imunne dievcatko blika
				if (Math.random() > 0.5) {
					dievcatko.setTransparency(0.3);
				} else {
					dievcatko.setTransparency(0.7);
				}
			}

			// Imunita sa skoncila
			else {
				dievcatko.setJeImunne(false);
				dievcatko.setDlzkaImunity(0);
				dievcatko.setTransparency(0);
			}
		}

		// Nastavi ukazovatel zivotov na aktualny pocet
		ukazovatelZivotov.nastavZivoty(dievcatko.getPocetZivotov());

		// Ak je pocet zivotov 0 a menej, hra sa ukonci
		if (dievcatko.getPocetZivotov() <= 0) {
			koniecHry();
		}
	}
}
