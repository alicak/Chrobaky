package sk.upjs.paz.chrobaky;

import java.awt.event.MouseEvent;

import sk.upjs.jpaz2.ImageShape;
import sk.upjs.jpaz2.Pane;
import sk.upjs.jpaz2.Turtle;

public class UvodnaObrazovka extends Pane {

	private TlacidloZvuk tlacidloZvuk;

	public UvodnaObrazovka() {
		super(600, 600);
		setBorderWidth(0);
		vykresliPozadie();

		// Vytvori a umiestni tlacidlo zvuku
		tlacidloZvuk = new TlacidloZvuk();
		add(tlacidloZvuk);
	}

	@Override
	protected void onMouseClicked(int x, int y, MouseEvent detail) {
		// Kliklo sa na tlacitko Hrat hru
		if ((x > 75) && (x < 225) && (y > 175) && (y < 205)) {
			HraChrobaky.prepniNaHraciuPlochu();
		}
		// Kliklo sa na tlacitko Pravidla
		else if ((x > 365) && (x < 510) && (y > 175) && (y < 205)) {
			HraChrobaky.prepniNaPravidla();
		}
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
	 * Vykresli pozadie plochy
	 */
	private void vykresliPozadie() {
		Turtle kreslic = new Turtle();
		kreslic.setShape(new ImageShape("images", "uvod.png"));
		add(kreslic);
		kreslic.center();
		kreslic.stamp();
		remove(kreslic);
	}
}
