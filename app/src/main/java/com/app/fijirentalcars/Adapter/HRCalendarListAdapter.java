package com.app.fijirentalcars.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.app.fijirentalcars.R;
import com.app.fijirentalcars.util.FijiRentalUtils;
import com.app.fijirentalcars.util.HRDateUtil;
import com.app.fijirentalcars.view.HRCalendarCard;

import org.joda.time.Days;
import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.joda.time.Months;

import java.util.Calendar;


public class HRCalendarListAdapter extends BaseAdapter {

    private final Context context;
    private final View.OnClickListener listener;
    private LocalDate departDate;
    private LocalDate returnDate;

    public HRCalendarListAdapter(Context context, View.OnClickListener listener, LocalDate departDate, LocalDate returnDate) {
        this.context = context;
        this.listener = listener;
        this.departDate = departDate;
        this.returnDate = returnDate;
    }

    @Override
    public int getCount() {
        return Months.monthsBetween(HRDateUtil.getTodaysDate(), HRDateUtil.getMaxShowDate()).getMonths() + 1;
    }

    @Override
    public Calendar getItem(int position) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, position);
        return calendar;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        HRCalendarCard calendarView;
        if (convertView == null) {
            calendarView = (HRCalendarCard) LayoutInflater.from(context).inflate(R.layout.calendar_card_view, viewGroup, false);
            calendarView.setOnClickListener(listener);
        } else {
            calendarView = (HRCalendarCard) convertView;
        }

        if(departDate!=null && returnDate!=null) {
            if (departDate.isAfter(returnDate)) {
                calendarView.populate(getItem(position), returnDate, departDate);
            } else {
                calendarView.populate(getItem(position), departDate, returnDate);
            }
        }else {
            calendarView.populate(getItem(position), departDate, returnDate);
        }

        return calendarView;
    }

    public void updateSelection(LocalDate departDate, LocalDate returnDate) {
        this.departDate = departDate;
        this.returnDate = returnDate;
        this.notifyDataSetChanged();
    }

    public void rangeSelection(LocalDate departDate) {




        if(this.departDate==null){
            if(!HRDateUtil.isUnavailableDate(departDate)){
                this.departDate=departDate;
            }

        }else if(returnDate==null){
            int DataDiff=0;

            if(this.departDate.isBefore(departDate)){
                DataDiff=Days.daysBetween(this.departDate,departDate).getDays();
            }else {
                DataDiff=Days.daysBetween(departDate,this.departDate).getDays();
            }

            if(DataDiff>15){
                if(HRDateUtil.isUnavailableDate(departDate)){
                    this.departDate=null;
                }else {
                    this.departDate=departDate;
                }

            }else {
                if(HRDateUtil.isBetweenUnavailableDate(this.departDate,departDate)){
                    this.departDate = departDate;
                }else {
                    this.returnDate = departDate;
                }

            }
//
        }else {
            if(HRDateUtil.isUnavailableDate(departDate)){
                this.departDate = null;
                this.returnDate = null;
            }else {
                this.departDate = departDate;
                this.returnDate = null;
            }
        }



        this.notifyDataSetChanged();
    }

    public void update() {
        this.notifyDataSetChanged();
    }

    public String getSelectedDate() {
        String date="";
        if(departDate!=null) {
            if (this.returnDate == null) {
                date = this.departDate.toString();
            } else {

                if (this.departDate.isBefore(this.returnDate)) {
                    date = this.departDate.toString() + HRDateUtil.DataSeprator + this.returnDate.toString();
                } else {
                    date = this.returnDate.toString() + HRDateUtil.DataSeprator + this.departDate.toString();
                }
            }
        }
        return date;
    }
}
