package de.escoand.android.library;

import android.app.ListFragment;
import android.os.Bundle;

public class CalendarFragment extends ListFragment {
	private CalendarAdapter adapter;

	public CalendarFragment() {
		adapter = new CalendarAdapter();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setListAdapter(adapter);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		getListView().setDivider(null);
		super.onActivityCreated(savedInstanceState);
	}

	public void setEvents(CalendarEvent[] events) {
		adapter.setEvents(events);
	}

	public void setOnCalendarEventClickedListener(OnCalendarEventClickListener l) {
		adapter.setOnCalendarEventClickedListener(l);
	}
}
