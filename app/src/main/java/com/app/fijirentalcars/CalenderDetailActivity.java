package com.app.fijirentalcars;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.fijirentalcars.Model.CarModel;
import com.app.fijirentalcars.Model.Unavailability;
import com.app.fijirentalcars.util.FijiRentalUtils;
import com.app.fijirentalcars.util.HRDateUtil;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class CalenderDetailActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvDate, tv_day, happingHeader, happeningName, happeningTime;
    RelativeLayout addUnavail, dailyPrice;
    CarModel carModel;
    ImageView leftSwipe, rightSwipe;
    String currentDate;
    TextView month_year;
    RelativeLayout swipeRelative, happingView;
    ImageView closeBtn;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender_detail);
        Window window = this.getWindow();
        Log.e("CalenderDetailActivity", "CalenderDetailActivity");
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.my_statusbar_color));

        tvDate = findViewById(R.id.tv_date);
        tv_day = findViewById(R.id.tv_day);
        addUnavail = findViewById(R.id.add_unavail);
        dailyPrice = findViewById(R.id.rl_daily_price);
        happingHeader = findViewById(R.id.whatHappening);
        happingView = findViewById(R.id.viewhappen);
        month_year = findViewById(R.id.month_year);
        closeBtn = findViewById(R.id.iv_back);
        leftSwipe = findViewById(R.id.leftSwipe);
        rightSwipe = findViewById(R.id.rightSwipe);
        happeningName = findViewById(R.id.tv_name);
        happeningTime = findViewById(R.id.date_happening);

        leftSwipe.setOnClickListener(this);
        rightSwipe.setOnClickListener(this);
        happingView.setOnClickListener(this);
        swipeRelative = findViewById(R.id.swipe_date_view);
        swipeRelative.setOnTouchListener(new OnSwipeTouchListener(this));

        if (getIntent().hasExtra(FijiRentalUtils.CarDetailIntent)) {
            carModel = (CarModel) getIntent().getSerializableExtra(FijiRentalUtils.CarDetailIntent);

            currentDate = getIntent().getStringExtra("CURRENT_DATE");


            LocalDate localDate = LocalDate.parse(currentDate);
            month_year.setText(localDate.monthOfYear().getAsText(Locale.getDefault()) + " " + localDate.getYear());   //.substring(0, 1).toUpperCase() + localDate.dayOfMonth().toString().substring(1).toLowerCase()
            tvDate.setText(String.valueOf(localDate.getDayOfMonth()));

            DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("EEEE");
            updateView(localDate);


//
            tv_day.setText(String.valueOf(localDate.dayOfWeek().getAsText(Locale.getDefault())));
//                FijiRentalUtils.Logger("TAGS", "date  " + localDate.getYear() + "+ month " + localDate.monthOfYear().getAsText(Locale.getDefault()) + " date " + localDate.getDayOfMonth() + " day " +localDate.dayOfWeek().getAsText(Locale.getDefault()));
//
//            } else {
//            LocalDate.parse(currentDate);
//                try {
//                    Date currentdate;
//                    currentdate = new SimpleDateFormat("yyyy-MM-dd").parse(currentDate);
//                    month_year.setText(currentdate.getMonth() + " " + currentdate.getYear());
//                    tvDate.setText(String.valueOf(currentdate.getDay()));
//                    tv_day.setText(String.valueOf(currentdate.getDate()));
//
//                } catch (ParseException e) {
//                    FijiRentalUtils.Logger("TAGS", "mess " + e.getMessage());
//                    e.printStackTrace();
//                }

//            }


        }

        tvDate.setOnClickListener(this);
        addUnavail.setOnClickListener(this);
        dailyPrice.setOnClickListener(this);
        closeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.tv_date:
//                Intent dateIntent = new Intent(this, ActivityVerticalCalendar.class);
//                startActivity(dateIntent);
//                break;

            case R.id.add_unavail:
                Intent unavailIntent = new Intent(this, Add_Unavailability.class);
                unavailIntent.putExtra("CURRENT_DATE", currentDate);
                startActivity(unavailIntent);
                break;

            case R.id.rl_daily_price:
                Intent dailyPrice = new Intent(this, DailyPrice.class);
                dailyPrice.putExtra(FijiRentalUtils.CarDetailIntent, carModel);
                dailyPrice.putExtra("CURRENT_DATE", currentDate);
                startActivity(dailyPrice);
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.leftSwipe:
                LocalDate localDate = LocalDate.parse(currentDate).minusDays(1);
                currentDate = localDate.toString();
                month_year.setText(localDate.monthOfYear().getAsText(Locale.getDefault()) + " " + localDate.getYear());
                tvDate.setText(String.valueOf(localDate.getDayOfMonth()));
                tv_day.setText(String.valueOf(localDate.dayOfWeek().getAsText(Locale.getDefault())));
                updateView(localDate);
                break;
            case R.id.rightSwipe:
                LocalDate localDate2 = LocalDate.parse(currentDate).plusDays(1);
                currentDate = localDate2.toString();
                month_year.setText(localDate2.monthOfYear().getAsText(Locale.getDefault()) + " " + localDate2.getYear());
                tvDate.setText(String.valueOf(localDate2.getDayOfMonth()));
                tv_day.setText(String.valueOf(localDate2.dayOfWeek().getAsText(Locale.getDefault())));
                updateView(localDate2);
                break;
            case R.id.viewhappen:
                Intent i=new Intent(CalenderDetailActivity.this,Remove_Unavailability.class);
                startActivity(i);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalDate localDate = LocalDate.parse(currentDate);
        month_year.setText(localDate.monthOfYear().getAsText(Locale.getDefault()) + " " + localDate.getYear());   //.substring(0, 1).toUpperCase() + localDate.dayOfMonth().toString().substring(1).toLowerCase()
        tvDate.setText(String.valueOf(localDate.getDayOfMonth()));

        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("EEEE");
        updateView(localDate);


//
        tv_day.setText(String.valueOf(localDate.dayOfWeek().getAsText(Locale.getDefault())));
    }

    private void updateView(LocalDate localDate) {
        if (HRDateUtil.isUnavailableDate(localDate)) {
            happingView.setVisibility(View.VISIBLE);
            happingHeader.setVisibility(View.VISIBLE);

            CarModel carModel = Paper.book().read(FijiRentalUtils.carModel);

            List<Unavailability> unavailabilities = carModel.getUnavailability();

            Unavailability unavailability = unavailabilities.get(HRDateUtil.UnavailPos);

            happeningName.setText("Unavailable");


            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);

            try {
                Date t = ft.parse(unavailability.getToDateTime());
                Date t2 = ft.parse(unavailability.getFromDateTime());
                ft.applyPattern("dd MMM, hh:mm a");
//                FijiRentalUtils.Logger("TAGS","time "+ft.format(t));
                happeningTime.setText(ft.format(t) + "-" + ft.format(t2));
            } catch (ParseException e) {
                e.printStackTrace();
            }

//            String unavailableText=;
//            happeningTime.setText(unavailableText);


        } else {
            happingView.setVisibility(View.GONE);
            happingHeader.setVisibility(View.GONE);
        }

        FijiRentalUtils.Logger("TAGS", "pos " + HRDateUtil.UnavailPos);
    }

    public class OnSwipeTouchListener implements View.OnTouchListener {

        private final GestureDetector gestureDetector;

        public OnSwipeTouchListener(Context ctx) {
            gestureDetector = new GestureDetector(ctx, new GestureListener());
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }

        private class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                boolean result = false;
                try {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
//                                Toast.makeText(CalenderDetailActivity.this, "right", Toast.LENGTH_SHORT).show();

                                LocalDate localDate = LocalDate.parse(currentDate).minusDays(1);
                                currentDate = localDate.toString();
                                month_year.setText(localDate.monthOfYear().getAsText(Locale.getDefault()) + " " + localDate.getYear());   //.substring(0, 1).toUpperCase() + localDate.dayOfMonth().toString().substring(1).toLowerCase()
                                tvDate.setText(String.valueOf(localDate.getDayOfMonth()));
                                tv_day.setText(String.valueOf(localDate.dayOfWeek().getAsText(Locale.getDefault())));
                                updateView(localDate);

//                                TranslateAnimation animate = new TranslateAnimation(0,swipeRelative.getWidth(),0,0);
//                                animate.setDuration(500);
//                                animate.setFillAfter(true);
//                                swipeRelative.startAnimation(animate);
                            } else {

                                LocalDate localDate = LocalDate.parse(currentDate).plusDays(1);
                                currentDate = localDate.toString();
                                month_year.setText(localDate.monthOfYear().getAsText(Locale.getDefault()) + " " + localDate.getYear());   //.substring(0, 1).toUpperCase() + localDate.dayOfMonth().toString().substring(1).toLowerCase()
                                tvDate.setText(String.valueOf(localDate.getDayOfMonth()));
                                tv_day.setText(String.valueOf(localDate.dayOfWeek().getAsText(Locale.getDefault())));
                                updateView(localDate);
//                                Toast.makeText(CalenderDetailActivity.this, "Left", Toast.LENGTH_SHORT).show();
//                                TranslateAnimation animate = new TranslateAnimation(0,-swipeRelative.getWidth(),0,0);
//                                animate.setDuration(500);
//                                animate.setFillAfter(true);
//                                swipeRelative.startAnimation(animate);
                            }
                            result = true;
                        }
                    }
//                    else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
//                        if (diffY > 0) {
//                            onSwipeBottom();
//                        } else {
//                            onSwipeTop();
//                        }
//                        result = true;
//                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return result;
            }
        }
    }
}