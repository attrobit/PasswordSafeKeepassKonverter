package de.attrobit.passwordkeepasskonverter;

import org.junit.Test;
import org.pwsafe.lib.file.PwsFile;
import org.pwsafe.lib.file.PwsFileFactory;

import de.slackspace.openkeepass.domain.Entry;
import de.slackspace.openkeepass.domain.Group;

public class KonverterTest {

	private Konverter instance = new Konverter();

	@Test
	public void test() throws Exception {
		PwsFile pwsFile = PwsFileFactory.loadFile("test/de/attrobit/passwordkeepasskonverter/TestSafe.psafe3",
				new StringBuilder("test"));

		Group convertedKpf = instance.convert(pwsFile);

		System.out.println("Entries.size: " + convertedKpf.getEntries().size());
		System.out.println("Groups.size: " + convertedKpf.getGroups().size());

		for (Group group : convertedKpf.getGroups()) {
			System.out.println(group);
			for (Entry entry : group.getEntries()) {
				System.out.println(entry);
			}
		}
	}

}
