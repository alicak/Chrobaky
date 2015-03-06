package sk.upjs.paz.chrobaky;

import sk.upjs.jpaz2.AudioClip;
import sk.upjs.jpaz2.ImageShape;
import sk.upjs.jpaz2.JPAZWindow;
import sk.upjs.jpaz2.TransitionEffect;

public class HraChrobaky {
	
	/**
	 * Okno, v ktorom bude cela hra prebiehat
	 */
	private static JPAZWindow okno;
	
	/**
	 * Uvodna obrazovka
	 */
	private static UvodnaObrazovka uvodnaObrazovka;
	
	/**
	 * Hracia plocha
	 */
	private static HraciaPlocha hraciaPlocha;
	
	/**
	 * Obrazovka s pravidlami hry
	 */
	private static Pravidla pravidla;
	
	/**
	 * Hudba, ktora bude hrat na pozadi
	 */
	public static AudioClip hudba;
	
	/**
	 * True, ak hudba hra
	 */
	public static boolean hraHudba;

	public static void main(String[] args) {
		// Vytvori a spusti hudbu
		hudba = new AudioClip("audio", "hudba.wav");
		hraHudba = true;
		hudba.playInLoop();
		
		// Vytvori uvodnu obrazovku, hraciu plochy aj obrazovku s pravidlami
		uvodnaObrazovka = new UvodnaObrazovka();
		hraciaPlocha = new HraciaPlocha();
		pravidla = new Pravidla();
		
		// Vytvori okno hry zobrazujuce uvodnu obrazovku
		okno = new JPAZWindow(uvodnaObrazovka);
		okno.setTitle("Chrobáky");
		okno.setIconImage(new ImageShape("images", "chrobak-ikonka.png"));
		okno.setResizable(false);
		
	}

	/**
	 * Prepne obrazovku na hraciu plochu
	 */
	public static void prepniNaHraciuPlochu() {	
		hraciaPlocha.pridajTlacidloZvuk();
		hraciaPlocha.zacniHru();
		okno.rebindWithEffect(hraciaPlocha, TransitionEffect.FADE_OUT_FADE_IN, 1500);
	}
	
	/**
	 * Prepne obrazovku na plochu s pravidlami
	 */
	public static void prepniNaPravidla()
	{
		pravidla.pridajTlacidloZvuk();
		okno.rebindWithEffect(pravidla, TransitionEffect.FADE_OUT_FADE_IN, 1500);
	}
	
	/**
	 * Prepne obrazovku na uvodnu obrazovku
	 */
	public static void prepniNaUvodnuObrazovku()
	{
		uvodnaObrazovka.pridajTlacidloZvuk();
		okno.rebindWithEffect(uvodnaObrazovka, TransitionEffect.FADE_OUT_FADE_IN, 1500);
	}
}
