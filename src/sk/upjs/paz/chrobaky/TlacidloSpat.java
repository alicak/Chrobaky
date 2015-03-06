package sk.upjs.paz.chrobaky;

import sk.upjs.jpaz2.ImageShape;
import sk.upjs.jpaz2.Turtle;

public class TlacidloSpat extends Turtle {

	public TlacidloSpat(double x, double y) {
		setPosition(x, y);
		setShape(new ImageShape("images", "spat.png"));
	}

	/**
	 * Vrati true, ak sa bod so suradnicami x, y nachadza v tlacidle
	 */
	public boolean klikloSaNaMna(int x, int y) {
		return distanceTo(x, y) < 20;
	}

}
