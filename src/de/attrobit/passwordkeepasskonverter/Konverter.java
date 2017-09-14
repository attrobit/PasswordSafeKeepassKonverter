package de.attrobit.passwordkeepasskonverter;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import de.slackspace.openkeepass.domain.TimesBuilder;

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
		EntryBuilder entryBuilder = new EntryBuilder();
		entryBuilder.times(new TimesBuilder().build());

		Iterator<Integer> fields = record.getFields();
		while (fields.hasNext()) {
			Integer fieldValue = fields.next();
			mapField(record.getField(fieldValue), PwsFieldTypeV3.valueOf(fieldValue), entryBuilder);
		}

		return entryBuilder.build();
	}

	private void mapField(PwsField field, PwsFieldTypeV3 fieldType, EntryBuilder entryBuilder) {
		switch (fieldType) {
		case TITLE:
			entryBuilder.title(field.toString());
			break;
		case GROUP:
			break;
		case NOTES:
			entryBuilder.notes(field.toString());
			break;
		case PASSWORD:
			entryBuilder.password(field.toString());
			break;
		case USERNAME:
			entryBuilder.username(field.toString());
			break;
		case URL:
			entryBuilder.url(field.toString());
			break;
		case UUID:
			entryBuilder.uuid(java.util.UUID.nameUUIDFromBytes(field.getBytes()));
			break;
		case AUTOTYPE:
		case CREATION_TIME:
			entryBuilder.times(new TimesBuilder(entryBuilder.getTimes()).creationTime(toCalendar(field)).build());
			break;
		case LAST_MOD_TIME:
			entryBuilder
					.times(new TimesBuilder(entryBuilder.getTimes()).lastModificationTime(toCalendar(field)).build());
			break;
		case LAST_ACCESS_TIME:
			entryBuilder.times(new TimesBuilder(entryBuilder.getTimes()).lastAccessTime(toCalendar(field)).build());
			break;
		case PASSWORD_MOD_TIME:
			// ignoriere password mod time
			break;
		case END_OF_RECORD:
		case PASSWORD_EXPIRY_INTERVAL:
		case PASSWORD_HISTORY:
		case PASSWORD_LIFETIME:
		case PASSWORD_POLICY:
		case PASSWORD_POLICY_DEPRECATED:
		case V3_ID_STRING:
		default:
			throw new RuntimeException("Field not mapped: " + fieldType);
		}
	}

	private static Calendar toCalendar(PwsField field) {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime((Date) field.getValue());
		return calendar;
	}
}
