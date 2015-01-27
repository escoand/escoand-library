package de.escoand.android.library;

import android.app.ListFragment;
import android.os.Bundle;

public class CalendarFragment extends ListFragment {
	CalendarAdapter adapter = new CalendarAdapter();
	CalendarEvent[] events = new CalendarEvent[0];

	@Override
	public void onCreate(Bundle savedInstanceState) {
		adapter.setEvents(events);
		setListAdapter(adapter);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public void setEvents(CalendarEvent[] events) {
		if (getView() == null)
			this.events = events;
		else
			adapter.setEvents(events);
	}
}
