package com.app.fijirentalcars;

import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.app.fijirentalcars.Adapter.HRCalendarListAdapter;
import com.app.fijirentalcars.Model.CarModel;
import com.app.fijirentalcars.Model.DBModel;
import com.app.fijirentalcars.SQLDB.HRDatabase;
import com.app.fijirentalcars.util.FijiRentalUtils;
import com.app.fijirentalcars.util.HRDateUtil;

import org.joda.time.LocalDate;

import io.paperdb.Paper;


public class ActivityVerticalCalendar extends AppCompatActivity {
    //  ActivityVerticalCalendarBinding binding;

    private String date, month;
    private HRCalendarListAdapter HRCalendarListAdapter;
    private HRDatabase HRDatabase;
    private Context context;
    CarModel carModel;
    ListView calendarList;
    ImageView closeBtn;
    TextView saveBtn, heading;
    String currentDate = null;
    boolean isDateGet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_calendar);

        Window window = this.getWindow();
// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.my_statusbar_color));
        }

        carModel = Paper.book().read(FijiRentalUtils.carModel);

        if (carModel.getCarprice() != null) {
            if (!carModel.getCarprice().equals("null") && !TextUtils.isEmpty(carModel.getCarprice())) {
                HRDateUtil.DailyPrice = Integer.parseInt(String.valueOf((int) Double.parseDouble(carModel.getCarprice())));
            }
        }


        if (getIntent().hasExtra("DATEGET")) {
            isDateGet = getIntent().getBooleanExtra("DATEGET", false);
        }

        if (getIntent().hasExtra("DATEVALUE")) {
            currentDate = getIntent().getStringExtra("DATEVALUE");
        }


        //   binding = DataBindingUtil.setContentView(this, R.layout.activity_vertical_calendar);
        context = this;
        calendarList = (ListView) findViewById(R.id.calendar_list);
        closeBtn = findViewById(R.id.cancel_button);
        saveBtn = findViewById(R.id.save_btn);
        heading = findViewById(R.id.heading);


        if (isDateGet) {
            saveBtn.setVisibility(View.VISIBLE);
            heading.setVisibility(View.GONE);
            HRCalendarListAdapter = new HRCalendarListAdapter(this, listener, null, null);

        } else {
            saveBtn.setVisibility(View.GONE);
            heading.setVisibility(View.VISIBLE);
            HRCalendarListAdapter = new HRCalendarListAdapter(this, listener, HRDateUtil.getTodaysDate(), null);
        }

        if (currentDate != null) {
            if (currentDate.contains(HRDateUtil.DataSeprator)) {
                String date1 = currentDate.split(HRDateUtil.DataSeprator)[0];
                String date2 = currentDate.split(HRDateUtil.DataSeprator)[1];

                HRCalendarListAdapter.updateSelection(LocalDate.parse(date1), LocalDate.parse(date2));
            } else {
                HRCalendarListAdapter.updateSelection(LocalDate.parse(currentDate), LocalDate.parse(currentDate));
            }
        }


        HRDatabase = new HRDatabase(this);
//        HRDatabase.deleteAllNotes();
        calendarList.setAdapter(HRCalendarListAdapter);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(HRCalendarListAdapter.getSelectedDate())) {
                    finish();
                } else {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result", "success");
                    returnIntent.putExtra("DATEVALUE", HRCalendarListAdapter.getSelectedDate());
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }
        });
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            LocalDate selectedDate = (LocalDate) view.getTag();
            date = selectedDate.toString();
            if (isDateGet) {
                HRCalendarListAdapter.rangeSelection(selectedDate);
            } else {
                HRCalendarListAdapter.updateSelection(selectedDate, null);

                if (currentDate != null && !TextUtils.isEmpty(currentDate)) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result", "success");
                    returnIntent.putExtra("SELECTEDDATE", HRCalendarListAdapter.getSelectedDate());
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                } else {
                    Intent i = new Intent(ActivityVerticalCalendar.this, CalenderDetailActivity.class);
                    i.putExtra(FijiRentalUtils.CarDetailIntent, carModel);
                    i.putExtra("CURRENT_DATE", date);
                    startActivity(i);
                }


//            popup();

            }

        }
    };

    @Override
    protected void onDestroy() {
        HRDateUtil.DailyPrice = 30;
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (FijiRentalUtils.isDateUpdate) {
            HRCalendarListAdapter.update();
            FijiRentalUtils.isDateUpdate = false;
        }
    }

    private void popup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        TextView ok = view.findViewById(R.id.okTv);
        TextView cancel = view.findViewById(R.id.cancelTv);
        final EditText fare = view.findViewById(R.id.editText);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fare.getText().toString() != null && fare.getText().toString().length() > 0 && !fare.getText().toString().equals("") && !fare.getText().toString().equals("null")) {
                    DBModel model = new DBModel(fare.getText().toString(), date);
                    HRDatabase.insertTitle(model);
                    HRCalendarListAdapter.update();
                    alertDialog.dismiss();
                } else {
                    fare.setError("Enter Event");
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }

}
