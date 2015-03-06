package sk.upjs.paz.chrobaky;

import java.awt.event.MouseEvent;

import sk.upjs.jpaz2.ImageShape;
import sk.upjs.jpaz2.Pane;
import sk.upjs.jpaz2.Turtle;

public class Pravidla extends Pane {

	TlacidloSpat tlacidloSpat;
	TlacidloZvuk tlacidloZvuk;

	public Pravidla() {
		super(600, 600);
		setBorderWidth(0);
		vykresliPozadie();

		// Vytvori a umiestni tlacidlo spat
		tlacidloSpat = new TlacidloSpat(30, 80);
		add(tlacidloSpat);

		// Vytvori a umiestni tlacidlo zvuku
		tlacidloZvuk = new TlacidloZvuk();
		add(tlacidloZvuk);
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
		kreslic.setShape(new ImageShape("images", "pravidla.png"));
		add(kreslic);
		kreslic.center();
		kreslic.stamp();
		remove(kreslic);
	}

	@Override
	protected void onMouseClicked(int x, int y, MouseEvent detail) {
		// Kliklo sa na tlacidlo spat
		if (tlacidloSpat.klikloSaNaMna(x, y))
			HraChrobaky.prepniNaUvodnuObrazovku();
	}
}
