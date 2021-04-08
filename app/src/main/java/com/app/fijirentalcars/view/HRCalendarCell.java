package com.app.fijirentalcars.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.util.AttributeSet;
import android.view.View;


import androidx.appcompat.widget.AppCompatTextView;

import com.app.fijirentalcars.Model.CarModel;
import com.app.fijirentalcars.Model.DBModel;
import com.app.fijirentalcars.Model.Unavailability;
import com.app.fijirentalcars.R;
import com.app.fijirentalcars.SQLDB.HRDatabase;
import com.app.fijirentalcars.util.FijiRentalUtils;
import com.app.fijirentalcars.util.HRDateUtil;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.paperdb.Paper;

import static com.app.fijirentalcars.util.HRDateUtil.isUnavailableDate;

public class HRCalendarCell extends AppCompatTextView {
    Context context;

    public HRCalendarCell(Context context) {
        super(context);
        this.context = context;
    }

    public HRCalendarCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public HRCalendarCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()+(getMeasuredWidth()/3));
    }

    public void populate(Calendar calendar, LocalDate startDate, LocalDate endDate) {
        HRDatabase HRDatabase = new HRDatabase(context);
        LocalDate today = HRDateUtil.getTodaysDate();
        LocalDate calendarDate = HRDateUtil.getConvertedLocalDate(calendar);
        setTag(calendarDate);
        setVisibility(View.VISIBLE);
        setBackground(getResources().getDrawable(R.drawable.background_drawable));




        List<DBModel> model = new ArrayList<>();
        model.addAll(HRDatabase.viewNotes(calendarDate.toString()));

//        LocalDate currentDate = LocalDate.parse(model.get(i).getDate());
        if (calendarDate.isBefore(today)) {
            FijiRentalUtils.Logger("TAGS","calender date "+calendarDate);
            setText("");

        } else {


            if (model.size() > 0) {
                for (int i = 0; i < model.size(); i++) {

                    if (String.valueOf(calendarDate).equals(model.get(i).getDate())) {



                        String value;
                        if(!isUnavailableDate(calendarDate)) {
                            value = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)).concat("\n\n").concat(model.get(i).getTitle()).concat(" $");
                            setTextColor(Color.RED);
                        }else {
                            value = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
                            setTextColor(Color.BLACK);
                        }
                        SpannableString styledResultText = new SpannableString(value);
                        styledResultText.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER)
                                , String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)).length()
                                , value.length()
                                , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        setText(styledResultText);
//                    setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)).concat("\n").concat(model.get(i).getTitle()).concat(" $"));

                        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "OpenSans-SemiBold.ttf");
                        setTypeface(typeface, Typeface.NORMAL);
                    } else {

                        String value;
                        if(!isUnavailableDate(calendarDate)) {
                            value = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)).concat("\n\n").concat(String.valueOf(HRDateUtil.DailyPrice)).concat(" $");
                        }else {
                            value = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
                        }
                        SpannableString styledResultText = new SpannableString(value);
                        styledResultText.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER)
                                , String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)).length()
                                , value.length()
                                , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        setText(styledResultText);
//                    setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)).concat("\n").concat("50").concat(" $"));
                        setTextColor(Color.BLACK);
                        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "OpenSans-Regular.ttf");
                        setTypeface(typeface, Typeface.NORMAL);
                    }
                }
            } else {

                String value;
                if(!isUnavailableDate(calendarDate)) {
                    value = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)).concat("\n\n").concat(String.valueOf(HRDateUtil.DailyPrice)).concat(" $");
                }else {
                    value = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
                }
                SpannableString styledResultText = new SpannableString(value);
                styledResultText.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER)
                        , String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)).length()
                        , value.length()
                        , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                setText(styledResultText);
//            setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)).concat("\n").concat("50").concat(" $"));
                setTextColor(Color.BLACK);
                Typeface typeface = Typeface.createFromAsset(context.getAssets(), "OpenSans-Regular.ttf");
                setTypeface(typeface, Typeface.NORMAL);

            }
        }
        setEnabled(!(calendarDate.isBefore(today) || calendarDate.isAfter(HRDateUtil.getMaxShowDate())));
        setActivated(today.equals(calendarDate));
        updateSelection(startDate, endDate);
    }



    public void updateSelection(LocalDate startDate, LocalDate endDate) {

        LocalDate calendarDate = (LocalDate) getTag();


        if(isUnavailableDate(calendarDate)){
            updateCell(R.drawable.calendar_item_gray, Typeface.BOLD, true);
        }else {

            if (HRDateUtil.localDateEquals(calendarDate, startDate) && HRDateUtil.localDateEquals(calendarDate, endDate)) {
                updateCell(R.drawable.bg_selected_date_background, Typeface.BOLD, true);
//            updateCell(R.mipmap.calendar_selector_same_day, Typeface.BOLD, true);
            } else if (HRDateUtil.localDateEquals(calendarDate, startDate)) {
                int resId = endDate == null ?
                        R.drawable.bg_selected_date_background : R.drawable.right;
//                    R.drawable.bg_selected_date_background : R.mipmap.calendar_selector_green_grey;
                updateCell(resId, Typeface.BOLD, true);
                setTextColor(Color.WHITE);
            } else if (HRDateUtil.localDateEquals(calendarDate, endDate)) {
                int resId = startDate == null ?
                        R.drawable.bg_selected_date_background : R.drawable.left;
//                    R.mipmap.calendar_selector_blue : R.mipmap.calendar_selector_blue_grey;
                updateCell(resId, Typeface.BOLD, true);
            } else if (startDate != null &&
                    endDate != null &&
                    calendarDate.isAfter(new LocalDate(startDate)) &&
                    calendarDate.isBefore(new LocalDate(endDate))) {
                updateCell(R.drawable.bg_selected_date_background, Typeface.NORMAL, false);
            } else if (startDate != null &&
                    endDate != null &&
                    calendarDate.isBefore(new LocalDate(startDate)) &&
                    calendarDate.isAfter(new LocalDate(endDate))) {
                updateCell(R.drawable.bg_selected_date_background, Typeface.NORMAL, false);
            } else {
                updateCell(R.drawable.background_drawable, HRDateUtil.getTodaysDate().equals(calendarDate) ? Typeface.BOLD : Typeface.NORMAL, false);
            }
        }
    }

    private void updateCell(int resId, int textStyle, boolean selected) {
        setBackgroundResource(resId);
        setTypeface(null, textStyle);
        setSelected(selected);
    }


}
