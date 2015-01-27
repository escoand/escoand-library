package de.escoand.android.library;

import java.util.GregorianCalendar;

public class CalendarEvent {
	GregorianCalendar begin;
	GregorianCalendar end;
	String name;
	String name_short;

	public CalendarEvent(GregorianCalendar begin, GregorianCalendar end,
			String name) {
		super();
		this.begin = begin;
		this.end = end;
		this.name = name;
		this.name_short = name;
	}

	public CalendarEvent(GregorianCalendar begin, GregorianCalendar end,
			String name, String name_short) {
		super();
		this.begin = begin;
		this.end = end;
		this.name = name;
		this.name_short = name_short;
	}

	public int getDayRange() {
		return (int) ((end.getTime().getTime() - begin.getTime().getTime()) / (1000 * 60 * 60 * 24));
	}
}
