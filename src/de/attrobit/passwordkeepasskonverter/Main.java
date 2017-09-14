package de.attrobit.passwordkeepasskonverter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import org.pwsafe.lib.exception.EndOfFileException;
import org.pwsafe.lib.exception.InvalidPassphraseException;
import org.pwsafe.lib.exception.UnsupportedFileVersionException;
import org.pwsafe.lib.file.PwsFile;
import org.pwsafe.lib.file.PwsFileFactory;

import de.slackspace.openkeepass.domain.Group;
import de.slackspace.openkeepass.domain.KeePassFile;

public class Main {

	/**
	 * @param args
	 * @throws UnsupportedFileVersionException
	 * @throws IOException
	 * @throws InvalidPassphraseException
	 * @throws EndOfFileException
	 * @throws NoSuchAlgorithmException
	 * @throws FileNotFoundException
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		String path = args[0];

		final JPasswordField pf = new JPasswordField();
		String passwd = JOptionPane.showConfirmDialog(null, pf, "Bitte Passwort eingeben.",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION
						? new String(pf.getPassword())
						: "";

		System.out.println("path: " + path);

		PwsFile pwsFile = PwsFileFactory.loadFile(path, new StringBuilder(passwd));

		Group convert = new Konverter().convert(pwsFile);

		// TODO: Safe file
	}

}
