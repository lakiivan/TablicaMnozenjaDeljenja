package main;

import javax.swing.JOptionPane;

import logic.VezbanjeDeljenja;
import logic.VezbanjeMnozenja;

public class MainApp {

	public static void main(String[] args) {
		//izbor sta da se vezba
		int n = jOptionChoice();
		System.out.println("You choosed " + n);
		//pozvati izabrani mod odnosno mnozenje ili deljenje
		if (n == 0) {
			VezbanjeMnozenja vezbanje = new VezbanjeMnozenja();
			vezbanje.setVisible(true);
		} else {
			VezbanjeDeljenja vezbanje = new VezbanjeDeljenja();
			vezbanje.setVisible(true);
		}
	}

	private static int jOptionChoice() {
		Object[] options = { "Mnozenje", "Deljenje" };
		int n = JOptionPane.showOptionDialog(null, "Da li zelite da vezbamo mnozenje ili deljenje?", "Vezbanje Matematike",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, // do not use a custom Icon
				options, // the titles of buttons
				options[0]); // default button title
		return n;
	}

}
