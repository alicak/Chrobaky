package sk.upjs.paz.chrobaky;

import sk.upjs.jpaz2.ImageShape;
import sk.upjs.jpaz2.Pane;
import sk.upjs.jpaz2.Turtle;

public class KoniecHry extends Pane {
	
	public KoniecHry(boolean jeNajvyssieSkore)
	{
		super(300, 180);
		setTransparentBackground(true);
		setBorderWidth(0);
		ukazKoniecHry();
		// Ak je nahrate skore vyssie ako doteraz najvyssie, ukaze o tom oznam
		if(jeNajvyssieSkore)
		{
			ukazNajSkore();
		}
	}
	
	/**
	 * Nakresli oznam o najvyssom skore
	 */
	private void ukazNajSkore()
	{
		Turtle kreslic = new Turtle(150, 150);
		kreslic.setShape(new ImageShape("images", "nahrane-naj-skore.png"));
		add(kreslic);
		kreslic.stamp();
		remove(kreslic);
	}
	
	/**
	 * Nakresli koniec hry
	 */
	private void ukazKoniecHry()
	{
		Turtle kreslic = new Turtle(150, 60);
		kreslic.setShape(new ImageShape("images", "koniec-hry.png"));
		add(kreslic);
		kreslic.stamp();
		remove(kreslic);
	}

}
