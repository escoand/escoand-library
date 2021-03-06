package de.escoand.android.library;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.TextView;

public class CalendarAdapter implements ListAdapter, OnClickListener {
	public GregorianCalendar DATE_FIRST;
	public GregorianCalendar DATE_LAST;
	public boolean WEEK_NUMBERS = true;
	public int EVENT_FOREGROUND = android.R.color.primary_text_dark;
	public int EVENT_BACKGROUND = android.R.color.background_dark;

	private CalendarEvent[] events = new CalendarEvent[0];
	private OnCalendarEventClickListener listener = null;

	public CalendarAdapter() {
		DATE_FIRST = new GregorianCalendar();
		DATE_FIRST.set(GregorianCalendar.DAY_OF_YEAR, 1);
		DATE_LAST = new GregorianCalendar();
		DATE_LAST.set(GregorianCalendar.DAY_OF_YEAR,
				DATE_LAST.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
	}

	public CalendarAdapter(GregorianCalendar first, GregorianCalendar last) {
		DATE_FIRST = first;
		DATE_LAST = last;
	}

	@Override
	public int getCount() {
		return (int) ((DATE_LAST.getTime().getTime() - DATE_FIRST.getTime()
				.getTime()) / (1000 * 60 * 60 * 24 * 7) + 1);
	}

	/* gui handling */

	@Override
	public Object getItem(int position) {
		GregorianCalendar begin = (GregorianCalendar) DATE_FIRST.clone();
		begin.add(GregorianCalendar.WEEK_OF_YEAR, position);
		begin.set(GregorianCalendar.DAY_OF_WEEK, begin.getFirstDayOfWeek());
		begin.set(GregorianCalendar.HOUR_OF_DAY, 0);
		begin.set(GregorianCalendar.MINUTE, 0);
		begin.set(GregorianCalendar.SECOND, 0);
		begin.set(GregorianCalendar.MILLISECOND, 0);

		GregorianCalendar end = (GregorianCalendar) begin.clone();
		end.add(GregorianCalendar.WEEK_OF_YEAR, 1);
		end.add(GregorianCalendar.MILLISECOND, -1);

		return new CalendarEvent(begin, end, null);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Context context = parent.getContext();
		CalendarEvent week = (CalendarEvent) getItem(position);
		LayoutInflater inflater = LayoutInflater.from(context);
		ViewHolder holder;

		/* get holder */
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.calendar_week, parent,
					false);
			holder = new ViewHolder();
			holder.month = (TextView) convertView.findViewById(R.id.monthName);
			holder.weekdays = (LinearLayout) convertView
					.findViewById(R.id.weekdays);
			holder.weekday1 = (TextView) convertView
					.findViewById(R.id.weekday1);
			holder.weekday2 = (TextView) convertView
					.findViewById(R.id.weekday2);
			holder.weekday3 = (TextView) convertView
					.findViewById(R.id.weekday3);
			holder.weekday4 = (TextView) convertView
					.findViewById(R.id.weekday4);
			holder.weekday5 = (TextView) convertView
					.findViewById(R.id.weekday5);
			holder.weekday6 = (TextView) convertView
					.findViewById(R.id.weekday6);
			holder.weekday7 = (TextView) convertView
					.findViewById(R.id.weekday7);
			holder.number = (TextView) convertView
					.findViewById(R.id.weekNumber);
			holder.day1 = (TextView) convertView.findViewById(R.id.day1);
			holder.day2 = (TextView) convertView.findViewById(R.id.day2);
			holder.day3 = (TextView) convertView.findViewById(R.id.day3);
			holder.day4 = (TextView) convertView.findViewById(R.id.day4);
			holder.day5 = (TextView) convertView.findViewById(R.id.day5);
			holder.day6 = (TextView) convertView.findViewById(R.id.day6);
			holder.day7 = (TextView) convertView.findViewById(R.id.day7);
			holder.rows = (LinearLayout) convertView
					.findViewById(R.id.weekRows);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		/* month name */
		if (week.end.get(GregorianCalendar.DAY_OF_MONTH) >= week.end
				.getMinimalDaysInFirstWeek()
				&& week.end.get(GregorianCalendar.DAY_OF_MONTH) < 7 + week.end
						.getMinimalDaysInFirstWeek()) {
			holder.month.setText(week.end.getDisplayName(
					GregorianCalendar.MONTH, GregorianCalendar.LONG,
					Locale.getDefault())
					+ " " + week.end.get(GregorianCalendar.YEAR));
			holder.month.setVisibility(View.VISIBLE);
			holder.weekdays.setVisibility(View.VISIBLE);
		} else {
			holder.month.setVisibility(View.GONE);
			holder.weekdays.setVisibility(View.GONE);
		}

		/* week number */
		if (WEEK_NUMBERS) {
			holder.number.setText(String.valueOf(week.begin
					.get(GregorianCalendar.WEEK_OF_YEAR)));
			holder.number.setVisibility(View.VISIBLE);
		} else {
			holder.number.setVisibility(View.GONE);
		}

		/* day numbers */
		SimpleDateFormat frmt = new SimpleDateFormat("E", Locale.getDefault());
		GregorianCalendar tmp = (GregorianCalendar) week.begin.clone();
		tmp.set(GregorianCalendar.DAY_OF_WEEK, tmp.getFirstDayOfWeek());
		holder.weekday1.setText(frmt.format(tmp.getTime()));
		holder.day1.setText(String.valueOf(tmp.get(GregorianCalendar.DATE)));
		tmp.add(GregorianCalendar.DATE, 1);
		holder.weekday2.setText(frmt.format(tmp.getTime()));
		holder.day2.setText(String.valueOf(tmp.get(GregorianCalendar.DATE)));
		tmp.add(GregorianCalendar.DATE, 1);
		holder.weekday3.setText(frmt.format(tmp.getTime()));
		holder.day3.setText(String.valueOf(tmp.get(GregorianCalendar.DATE)));
		tmp.add(GregorianCalendar.DATE, 1);
		holder.weekday4.setText(frmt.format(tmp.getTime()));
		holder.day4.setText(String.valueOf(tmp.get(GregorianCalendar.DATE)));
		tmp.add(GregorianCalendar.DATE, 1);
		holder.weekday5.setText(frmt.format(tmp.getTime()));
		holder.day5.setText(String.valueOf(tmp.get(GregorianCalendar.DATE)));
		tmp.add(GregorianCalendar.DATE, 1);
		holder.weekday6.setText(frmt.format(tmp.getTime()));
		holder.day6.setText(String.valueOf(tmp.get(GregorianCalendar.DATE)));
		tmp.add(GregorianCalendar.DATE, 1);
		holder.weekday7.setText(frmt.format(tmp.getTime()));
		holder.day7.setText(String.valueOf(tmp.get(GregorianCalendar.DATE)));

		/* events */
		holder.rows.removeAllViews();
		for (CalendarEvent event : events) {
			if (event.begin.after(week.end) || event.end.before(week.begin))
				continue;
			holder.rows = matchEventIntoWeek(context, holder.rows, event, this,
					EVENT_FOREGROUND, EVENT_BACKGROUND);
		}

		return convertView;
	}

	/* events handling */
	public static LinearLayout matchEventIntoWeek(Context context,
			LinearLayout rows, CalendarEvent event, OnClickListener listener,
			int foreground, int background) {
		LayoutInflater inflater = LayoutInflater.from(context);
		LinearLayout row = null;
		LayoutParams lo = null;
		int index = 0;

		GregorianCalendar week_begin = (GregorianCalendar) event.begin.clone();
		week_begin.set(GregorianCalendar.DAY_OF_WEEK,
				week_begin.getFirstDayOfWeek());
		week_begin.set(GregorianCalendar.HOUR_OF_DAY, 0);
		week_begin.set(GregorianCalendar.MINUTE, 0);
		week_begin.set(GregorianCalendar.SECOND, 0);
		week_begin.set(GregorianCalendar.MILLISECOND, 0);

		GregorianCalendar week_end = (GregorianCalendar) week_begin.clone();
		week_end.add(GregorianCalendar.WEEK_OF_YEAR, 1);
		week_end.add(GregorianCalendar.MILLISECOND, -1);

		/* real begin and end */
		GregorianCalendar begin = event.begin.after(week_begin) ? event.begin
				: week_begin;
		GregorianCalendar end = event.end.before(week_end) ? event.end
				: week_end;
		int offset = (int) (begin.getTime().getTime() - week_begin.getTime()
				.getTime()) / (1000 * 60 * 60 * 24);
		int days = (int) (end.getTime().getTime() - begin.getTime().getTime())
				/ (1000 * 60 * 60 * 24) + 1;

		/* search place in previous rows */
		loop: for (int i = 0; i < rows.getChildCount(); i++) {
			LinearLayout r = (LinearLayout) rows.getChildAt(i);
			float pos = 0;
			for (int j = 0; j < r.getChildCount(); j++) {
				lo = ((LayoutParams) r.getChildAt(j).getLayoutParams());

				/* insert into row */
				if (!(r.getChildAt(j) instanceof TextView) && pos <= offset
						&& pos + lo.weight >= offset + days
						&& lo.weight >= days) {
					row = r;
					lo.weight = pos + lo.weight - offset - days;
					if (lo.weight == 0)
						r.removeViewAt(j);
					index = j;
					offset -= pos;
					break loop;
				}

				pos += lo.weight;
			}

			/* append to row */
			if (pos <= offset) {
				row = r;
				index = row.getChildCount();
				offset -= pos;
				break;
			}
		}

		/* new row */
		if (row == null) {
			row = (LinearLayout) inflater.inflate(R.layout.calendar_eventrow,
					rows, false);
			rows.addView(row);
		}

		/* color */
		try {
			foreground = context.getResources().getColor(foreground);
		} catch (Exception e) {
		}
		try {
			background = context.getResources().getColor(background);
		} catch (Exception e) {
		}

		/* event */
		TextView tv = new TextView(context);
		tv.setTag(event);
		tv.setOnClickListener(listener);
		if (days > 2)
			tv.setText(event.name);
		else
			tv.setText(event.name_short);

		/* design */
		tv.setSingleLine();
		tv.setTextColor(foreground);
		tv.setBackgroundColor(background);
		tv.setTextAppearance(context, R.style.Calendar_Day_Event);

		/* layout */
		int pM = context.getResources().getDimensionPixelSize(
				R.dimen.Calendar_Event_Margin);
		int pH = context.getResources().getDimensionPixelSize(
				R.dimen.Calendar_Event_Padding_Horizontal);
		int pV = context.getResources().getDimensionPixelSize(
				R.dimen.Calendar_Event_Padding_Vertical);
		LayoutParams layout = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
		layout.setMargins(pM, pM, pM, pM);
		layout.weight = days;
		tv.setLayoutParams(layout);
		tv.setPadding(pH, pV, pH, pV);

		row.addView(tv, index);

		/* placeholder */
		if (offset >= 1) {
			View ph = new View(context);
			lo = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
			lo.weight = offset;
			ph.setLayoutParams(lo);
			row.addView(ph, index);
		}

		return rows;
	}

	public void setEvents(CalendarEvent[] events) {
		this.events = events;
	}

	public void zoomToEvents() {
		DATE_FIRST = new GregorianCalendar(3000, 0, 1);
		DATE_LAST = new GregorianCalendar(1000, 0, 1);
		for (CalendarEvent event : events) {
			if (event.begin.before(DATE_FIRST)) {
				DATE_FIRST = (GregorianCalendar) event.begin.clone();
				if (DATE_FIRST.get(GregorianCalendar.WEEK_OF_MONTH) == 0)
					DATE_FIRST.add(GregorianCalendar.DATE, -1);
				DATE_FIRST.set(GregorianCalendar.DAY_OF_WEEK,
						DATE_FIRST.getFirstDayOfWeek());
			}
			if (event.end.after(DATE_LAST)) {
				DATE_LAST = (GregorianCalendar) event.end.clone();
				DATE_LAST.set(GregorianCalendar.DAY_OF_WEEK,
						DATE_LAST.getFirstDayOfWeek());
				DATE_LAST.add(GregorianCalendar.WEEK_OF_YEAR, 1);
				DATE_LAST.add(GregorianCalendar.DAY_OF_WEEK, -1);
			}
		}
	}

	public void setOnCalendarEventClickedListener(OnCalendarEventClickListener l) {
		listener = l;
	}

	@Override
	public void onClick(View v) {
		if (listener != null)
			listener.onCalenderEventClick((CalendarEvent) v.getTag());
	}

	/* generic methods */

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public int getItemViewType(int position) {
		return 0;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		return true;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
	}

	/* view holder */
	private static class ViewHolder {
		TextView month;
		LinearLayout weekdays;
		TextView weekday1;
		TextView weekday2;
		TextView weekday3;
		TextView weekday4;
		TextView weekday5;
		TextView weekday6;
		TextView weekday7;
		TextView number;
		TextView day1;
		TextView day2;
		TextView day3;
		TextView day4;
		TextView day5;
		TextView day6;
		TextView day7;
		LinearLayout rows;
	}
}
