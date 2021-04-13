package com.app.fijirentalcars;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.andrewjapar.rangedatepicker.CalendarPicker;
import com.app.fijirentalcars.util.FijiRentalUtils;

import org.joda.time.LocalDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import kotlin.Pair;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function4;

public class RouteDatePicker extends AppCompatActivity {

    private Calendar startDate, endDate;
    CalendarPicker calendarPicker;
    String startSelectionDate, endSelectionDate;
    TextView saveBtn, resetBtn, startDateView, endDateView, startTimeview, endTimeView;
    private Pair<Date, Date> selectedDate;
    SeekBar startTimeBar, endTimeBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_date_picker);

        calendarPicker = findViewById(R.id.calendar_view);

        saveBtn = findViewById(R.id.tv_continue);
        resetBtn = findViewById(R.id.reset_btn);
        startDateView = findViewById(R.id.startDate);
        endDateView = findViewById(R.id.endDate);
        startTimeview = findViewById(R.id.startDatetime);
        endTimeView = findViewById(R.id.endDatetime);
        startTimeBar = findViewById(R.id.startTime);
        endTimeBar = findViewById(R.id.endTime);

        startSelectionDate = getIntent().getStringExtra("STARTDATE");
        endSelectionDate = getIntent().getStringExtra("ENDDATE");

        startDate = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        startDate.setTime(LocalDate.parse(startSelectionDate).toDate());

        endDate = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        endDate.setTime(LocalDate.parse(endSelectionDate).toDate());

        calendarPicker.showDayOfWeekTitle(false);
        calendarPicker.setMode(CalendarPicker.SelectionMode.RANGE);
        calendarPicker.setSelectionDate(startDate.getTime(), endDate.getTime());

        calendarPicker.setOnStartSelectedListener(new Function2<Date, String, Unit>() {
            @Override
            public Unit invoke(Date date, String s) {
                LocalDate localDate = LocalDate.fromDateFields(date);

                startDateView.setText(localDate.dayOfWeek().getAsShortText(Locale.getDefault()) + ", " + localDate.getDayOfMonth() + " " + localDate.monthOfYear().getAsShortText(Locale.getDefault()));
                endDateView.setText("End date");
                endTimeView.setText("");
                return null;
            }
        });

        calendarPicker.setOnRangeSelectedListener(new Function4<Date, Date, String, String, Unit>() {
            @Override
            public Unit invoke(Date date, Date date2, String s, String s2) {

                LocalDate localDate = LocalDate.fromDateFields(date);
                LocalDate localDate2 = LocalDate.fromDateFields(date2);

                startDateView.setText(localDate.dayOfWeek().getAsShortText(Locale.getDefault()) + ", " + localDate.getDayOfMonth() + " " + localDate.monthOfYear().getAsShortText(Locale.getDefault()));
                endDateView.setText(localDate2.dayOfWeek().getAsShortText(Locale.getDefault()) + ", " + localDate2.getDayOfMonth() + " " + localDate2.monthOfYear().getAsShortText(Locale.getDefault()));


                return null;
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDate = calendarPicker.getSelectedDate();
                FijiRentalUtils.Logger("TAGS", "fat " + selectedDate.getFirst());
                FijiRentalUtils.Logger("TAGS", "fat 2 " + selectedDate.getSecond());

                if (selectedDate.getSecond() == null) {
                    FijiRentalUtils.ShowValidation("Select return Date", RouteDatePicker.this, null);
                } else if (endTimeView.getText().toString().equalsIgnoreCase("")) {
                    FijiRentalUtils.ShowValidation("Select end time", RouteDatePicker.this, null);
                } else {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("start_date", LocalDate.fromDateFields(selectedDate.getFirst()).toString());
                    returnIntent.putExtra("end_date", LocalDate.fromDateFields(selectedDate.getSecond()).toString());
                    returnIntent.putExtra("start_date_time", startTimeview.getText().toString());
                    returnIntent.putExtra("end_date_time", endTimeView.getText().toString());
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }
        });

        startTimeBar.setMax(24 * 4);
        endTimeBar.setMax(24 * 4);


        startTimeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int hours = progress / 4;
                int minutes = (progress % 4) * 15;


                SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
                SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
                try {
                    Date _24HourDt = _24HourSDF.parse(hours + ":" + minutes);
                    startTimeview.setText(_12HourSDF.format(_24HourDt));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        endTimeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int hours = progress / 4;
                int minutes = (progress % 4) * 15;

                SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
                SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
                try {
                    Date _24HourDt = _24HourSDF.parse(hours + ":" + minutes);
                    endTimeView.setText(_12HourSDF.format(_24HourDt));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        startTimeBar.setProgress(40);
        endTimeBar.setProgress(40);

    }
}