package de.attrobit.passwordkeepasskonverter;

import java.util.Iterator;
import java.util.Optional;

import org.pwsafe.lib.file.PwsField;
import org.pwsafe.lib.file.PwsFieldTypeV3;
import org.pwsafe.lib.file.PwsFile;
import org.pwsafe.lib.file.PwsRecord;

import de.slackspace.openkeepass.domain.Entry;
import de.slackspace.openkeepass.domain.EntryBuilder;
import de.slackspace.openkeepass.domain.Group;
import de.slackspace.openkeepass.domain.GroupBuilder;

public class Konverter {

	Group convert(PwsFile pwsFile) {
		GroupBuilder groupBuilder = new GroupBuilder();

		Iterator<? extends PwsRecord> records = pwsFile.getRecords();
		while (records.hasNext()) {
			PwsRecord pwsRecord = records.next();

			PwsField field = pwsRecord.getField(PwsFieldTypeV3.GROUP);
			String fieldString = new String(field.getBytes());

			// verify group
			Optional<Group> foundGroup = groupBuilder.getGroups().stream()
					.filter(it -> it.getName().equalsIgnoreCase(fieldString)).findFirst();

			Group keePassGroup = foundGroup.orElse(new GroupBuilder(fieldString).build());
			if (!foundGroup.isPresent()) {
				groupBuilder.addGroup(keePassGroup);
			}
			keePassGroup.getEntries().add(convertRecord(pwsRecord));
		}

		return groupBuilder.build();
	}

	private Entry convertRecord(PwsRecord record) {
		String title = (String) record.getField(PwsFieldTypeV3.TITLE).getValue();

		return new EntryBuilder(title).build();
	}
}
