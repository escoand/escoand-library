package de.escoand.android.library;

import java.util.GregorianCalendar;
import java.util.Locale;

import de.escoand.android.library.R;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.TextView;

public class CalendarAdapter extends Object implements ListAdapter {
	public int WEEKS_BEFORE = 1000;
	public int WEEKS_AFTER = 1000;
	public int DEFAUL_EVENT_COLOR = 0xff219bd2;

	private CalendarEvent[] events = new CalendarEvent[0];

	@Override
	public int getCount() {
		return WEEKS_BEFORE + 1 + WEEKS_AFTER;
	}

	@Override
	public Object getItem(int position) {
		GregorianCalendar begin = new GregorianCalendar();
		begin.add(GregorianCalendar.WEEK_OF_YEAR, position - WEEKS_BEFORE);
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
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Context context = parent.getContext();
		CalendarEvent week = (CalendarEvent) getItem(position);
		LayoutInflater inflater = LayoutInflater.from(context);
		ViewHolder holder;

		/* default layout */
		int px = context.getResources().getDimensionPixelSize(
				R.dimen.calendar_event_margin);
		LayoutParams layout = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
		layout.setMargins(px, px, px, px);

		/* get holder */
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.calendar_week, parent,
					false);
			holder = new ViewHolder();
			holder.month = (TextView) convertView.findViewById(R.id.monthName);
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
		} else {
			holder.month.setVisibility(View.GONE);
		}

		/* day numbers */
		GregorianCalendar tmp = (GregorianCalendar) week.begin.clone();
		holder.number.setText(String.valueOf(tmp
				.get(GregorianCalendar.WEEK_OF_YEAR)));
		tmp.set(GregorianCalendar.DAY_OF_WEEK, tmp.getFirstDayOfWeek());
		holder.day1.setText(String.valueOf(tmp.get(GregorianCalendar.DATE)));
		tmp.add(GregorianCalendar.DATE, 1);
		holder.day2.setText(String.valueOf(tmp.get(GregorianCalendar.DATE)));
		tmp.add(GregorianCalendar.DATE, 1);
		holder.day3.setText(String.valueOf(tmp.get(GregorianCalendar.DATE)));
		tmp.add(GregorianCalendar.DATE, 1);
		holder.day4.setText(String.valueOf(tmp.get(GregorianCalendar.DATE)));
		tmp.add(GregorianCalendar.DATE, 1);
		holder.day5.setText(String.valueOf(tmp.get(GregorianCalendar.DATE)));
		tmp.add(GregorianCalendar.DATE, 1);
		holder.day6.setText(String.valueOf(tmp.get(GregorianCalendar.DATE)));
		tmp.add(GregorianCalendar.DATE, 1);
		holder.day7.setText(String.valueOf(tmp.get(GregorianCalendar.DATE)));

		/* events */
		holder.rows.removeAllViews();
		for (CalendarEvent event : events) {
			if (event.begin.after(week.end) || event.end.before(week.begin))
				continue;

			LinearLayout row = null;
			LayoutParams lo = null;
			int index = 0;

			/* real begin and end */
			GregorianCalendar begin = event.begin.after(week.begin) ? event.begin
					: week.begin;
			GregorianCalendar end = event.end.before(week.end) ? event.end
					: week.end;
			int offset = (int) (begin.getTime().getTime() - week.begin
					.getTime().getTime()) / (1000 * 60 * 60 * 24);
			int days = (int) (end.getTime().getTime() - begin.getTime()
					.getTime()) / (1000 * 60 * 60 * 24) + 1;

			/* search place in previous rows */
			loop: for (int i = 0; i < holder.rows.getChildCount(); i++) {
				LinearLayout r = (LinearLayout) holder.rows.getChildAt(i);
				float k = 0;
				for (int j = 0; j < r.getChildCount(); j++) {
					lo = ((LayoutParams) r.getChildAt(j).getLayoutParams());

					/* insert into row */
					if (!(r.getChildAt(j) instanceof TextView) && k <= offset
							&& k + lo.weight >= offset + days
							&& lo.weight >= days) {
						row = r;
						lo.weight = k + lo.weight - offset - days;
						if (lo.weight == 0)
							r.removeViewAt(j);
						index = j;
						offset -= k;
						break loop;
					}

					k += lo.weight;
				}

				/* append to row */
				if (k <= offset) {
					row = r;
					index = row.getChildCount();
					offset -= k;
					break;
				}
			}

			/* new row */
			if (row == null) {
				row = (LinearLayout) inflater.inflate(
						R.layout.calendar_eventrow, holder.rows, false);
				holder.rows.addView(row);
			}

			/* event */
			TextView tv = new TextView(new ContextThemeWrapper(context,
					R.style.Calendar_Event));
			tv.setBackgroundColor(DEFAUL_EVENT_COLOR);
			tv.setText(event.name);
			lo = new LayoutParams((ViewGroup.LayoutParams) layout);
			lo.weight = days;
			tv.setLayoutParams(lo);
			row.addView(tv, index);

			/* placeholder */
			if (offset >= 1) {
				View ph = new View(context);
				lo = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
				lo.weight = offset;
				ph.setLayoutParams(lo);
				row.addView(ph, index);
			}
		}

		return convertView;
	}

	@Override
	public int getItemViewType(int position) {
		return 0;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	public void setEvents(CalendarEvent[] events) {
		this.events = events;
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
	public void registerDataSetObserver(DataSetObserver observer) {
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	@Override
	public boolean isEnabled(int position) {
		return true;
	}

	private static class ViewHolder {
		TextView month;
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
