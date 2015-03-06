package sk.upjs.paz.chrobaky;

import java.awt.event.MouseEvent;

import sk.upjs.jpaz2.ImageShape;
import sk.upjs.jpaz2.Pane;
import sk.upjs.jpaz2.Turtle;

public class TlacidloZvuk extends Pane {

	/**
	 * Korytnacka, ktora vykresluje obrazok tlacidla podla toho, ci hudba hra
	 * alebo nie
	 */
	private Turtle obrazok;

	public TlacidloZvuk() {
		super(40, 40);

		setBorderWidth(0);
		setTransparentBackground(true);
		setPosition(10, 10);

		obrazok = new Turtle();
		obrazok.setPosition(20, 20);
		add(obrazok);

		// Obrazok sa nastavi podla toho, ci hudba hra alebo nie
		if (HraChrobaky.hraHudba) {
			obrazokZapnuty();
		} else {
			obrazokVypnuty();
		}
	}

	/**
	 * Nastavi obrazok zapnuteho zvuku
	 */
	public void obrazokZapnuty() {
		obrazok.setShape(new ImageShape("images", "sound-on.png"));
		obrazok.stamp();
	}

	/**
	 * Nastavi obrazok vypnuteho zvuku
	 */
	public void obrazokVypnuty() {
		obrazok.setShape(new ImageShape("images", "sound-off.png"));
		obrazok.stamp();
	}

	/**
	 * Zapne hudbu
	 */
	public void zapniHudbu() {
		HraChrobaky.hraHudba = true;
		HraChrobaky.hudba.playInLoop();
	}

	/**
	 * Vypne hudbu
	 */
	public void vypniHudbu() {
		HraChrobaky.hraHudba = false;
		HraChrobaky.hudba.stop();
	}

	/**
	 * Vrati true, ak sa bod so suradnicami x,y nachadza v tlacidle
	 */
	public boolean klikloSaNaMna(int x, int y) {
		return obrazok.distanceTo(x, y) < 20;
	}

	@Override
	protected void onMouseClicked(int x, int y, MouseEvent detail) {
		// Ak sa klikne na tlacidlo, hudba sa zapne/vypne
		if (klikloSaNaMna(x, y)) {
			if (HraChrobaky.hraHudba) {
				vypniHudbu();
				obrazokVypnuty();
			} else {
				zapniHudbu();
				obrazokZapnuty();
			}
		}
	}

}
