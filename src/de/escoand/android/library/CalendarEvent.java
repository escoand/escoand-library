package de.escoand.android.library;

import java.util.GregorianCalendar;

public class CalendarEvent {
	GregorianCalendar begin;
	GregorianCalendar end;
	String name;

	public CalendarEvent(GregorianCalendar begin, GregorianCalendar end,
			String name) {
		super();
		this.begin = begin;
		this.end = end;
		this.name = name;
	}

	public int getDayRange() {
		return (int) ((end.getTime().getTime() - begin.getTime().getTime()) / (1000 * 60 * 60 * 24));
	}
}
