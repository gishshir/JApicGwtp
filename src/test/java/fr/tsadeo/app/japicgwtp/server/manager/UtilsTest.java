package fr.tsadeo.app.japicgwtp.server.manager;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoField;
import java.util.Date;

import org.junit.Test;

import fr.tsadeo.app.japicgwtp.server.util.CollectUtils;

public class UtilsTest {

	@Test
	public void testDateTimeFormatter() {

		LocalDate date = LocalDate.now();
		System.out.println("HOUR_OF_DAY: " + date.isSupported(ChronoField.HOUR_OF_DAY));
		String text = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
		System.out.println("date: " + text);

		Instant now = Clock.systemUTC().instant();
		System.out.println("INSTANT HOUR_OF_DAY: " + now.isSupported(ChronoField.HOUR_OF_DAY));
		System.out.println("INSTANT YEAR: " + now.isSupported(ChronoField.YEAR));
		System.out.println("Instant: " + DateTimeFormatter.ISO_INSTANT.format(now));

		ZonedDateTime dt = ZonedDateTime.ofInstant(now, ZoneId.systemDefault());
		System.out.println("ZonedDateTime YEAR: " + dt.isSupported(ChronoField.YEAR));
		System.out
				.println("ZonedDateTime date: " + DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).format(dt));
		//
		// for (String zoneId : ZoneId.getAvailableZoneIds()) {
		// System.out.println("zoneId: " + zoneId);
		// }
		ZonedDateTime dtChine = ZonedDateTime.ofInstant(now, ZoneId.of("US/Pacific"));
		System.out.println("ZonedDateTime YEAR: " + dtChine.isSupported(ChronoField.YEAR));
		System.out.println("ZonedDateTime date: " + DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(dtChine));

		long timestamp = now.toEpochMilli();
		System.out.println("INSTANT DATE: " + new Date(timestamp));

	}

	@Test
	public void testSplit() throws Exception{

		String value = "totoValuetotoBonjourxxx";
		System.out.println("split toto: " +  CollectUtils.tabToString(value.split("toto"), '|'));
		System.out.println("split tot: " + CollectUtils.tabToString(value.split("xxx"), '|'));
		System.out.println("split titi: " + CollectUtils.tabToString(value.split("titi"), '|'));
		
		String[] result = value.split("totoValuetotoBonjourtot");
		
		System.out.println("split totoValuetotoBonjourxxx: " + CollectUtils.tabToString(result, '|') );
		System.out.println("size: " + result.length);
	}

}
