package de.escoand.android.library;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

import de.escoand.android.library.R;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View parent = inflater.inflate(R.layout.calendar_month, container,
				false);

		/* set day names */
		SimpleDateFormat frmt = new SimpleDateFormat("E", Locale.getDefault());
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(GregorianCalendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
		for (int i = 0; i < 7; i++) {
			int id = getResources().getIdentifier("weekday" + (i + 1), "id",
					getActivity().getPackageName());
			TextView day = (TextView) parent.findViewById(id);
			if (day != null)
				day.setText(frmt.format(cal.getTime()));
			cal.add(GregorianCalendar.DATE, 1);
		}

		return parent;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		setSelection(adapter.WEEKS_BEFORE + 1);
		super.onActivityCreated(savedInstanceState);
	}

	public void setEvents(CalendarEvent[] events) {
		if (getView() == null)
			this.events = events;
		else
			adapter.setEvents(events);
	}
}
