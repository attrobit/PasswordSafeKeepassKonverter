package eu.philipplang.passwordkeepasskonverter;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import org.pwsafe.lib.file.PwsFileFactory;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String path = args[0];

		final JPasswordField pf = new JPasswordField();
		String passwd = JOptionPane.showConfirmDialog(null, pf,
				"Bitte Passwort eingeben.", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION ? new String(
				pf.getPassword()) : "";

		System.out.println("path: " + path);
		System.out.println("password: " + passwd);
		System.out.println(PwsFileFactory.class);
	}

}
