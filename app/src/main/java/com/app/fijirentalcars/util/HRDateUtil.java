package com.app.fijirentalcars.util;

import com.app.fijirentalcars.Model.CarModel;
import com.app.fijirentalcars.Model.Unavailability;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

import java.util.Calendar;
import java.util.List;

import io.paperdb.Paper;

public class HRDateUtil {

    public static int DailyPrice = 30;
    public static int UnavailPos = 0;
    public static String DataSeprator = "MM";

    /**
     * This static method is used to get the restriction for the max show date*
     *
     * @return
     */


    public static LocalDate getMaxShowDate() {
        return getTodaysDate().plusMonths(12);
    }

    public static LocalDate getTodaysDate() {
        return LocalDate.now();
    }

    public static LocalDate getConvertedLocalDate(Calendar calendar) {
        DateTimeZone dateTimeZone = DateTimeZone.forID(calendar.getTimeZone().getID());
        return new DateTime(calendar.getTimeInMillis(), dateTimeZone).toLocalDate();
    }

    public static boolean localDateEquals(LocalDate calendar, LocalDate localDate) {
        return !(calendar == null || localDate == null) &&
                calendar.toString().equals(localDate.toString());
    }

    public static boolean isUnavailableDate(LocalDate calendarDate) {
        CarModel carModel = Paper.book().read(FijiRentalUtils.carModel);

        List<Unavailability> unavailabilities = carModel.getUnavailability();

        for (int i = 0; i < unavailabilities.size(); i++) {

            LocalDate startDate = LocalDate.parse(unavailabilities.get(i).getToDateTime().split("\\s+")[0]);
            LocalDate endDate = LocalDate.parse(unavailabilities.get(i).getFromDateTime().split("\\s+")[0]);

//            FijiRentalUtils.Logger("TAGS","date "+calendarDate+" start "+startDate+" end "+endDate+" val "+( calendarDate.isAfter(startDate) && calendarDate.isBefore(endDate))+" vale "+(calendarDate.isEqual(calendarDate) || calendarDate.isEqual(endDate) ));

            if (calendarDate.isEqual(startDate) || calendarDate.isEqual(endDate)) {
                UnavailPos = i;
                return true;
            } else if (calendarDate.isAfter(startDate) && calendarDate.isBefore(endDate)) {
                UnavailPos = i;
                return true;
            }


        }
        UnavailPos = 0;

        return false;


    }

    public static boolean isBetweenUnavailableDate(LocalDate startDaTe, LocalDate endDaTe) {
        CarModel carModel = Paper.book().read(FijiRentalUtils.carModel);

        List<Unavailability> unavailabilities = carModel.getUnavailability();

        for (int i = 0; i < unavailabilities.size(); i++) {

            LocalDate startDate = LocalDate.parse(unavailabilities.get(i).getToDateTime().split("\\s+")[0]);
            LocalDate endDate = LocalDate.parse(unavailabilities.get(i).getFromDateTime().split("\\s+")[0]);

            if(endDaTe!=null){
                if (startDaTe.isAfter(startDate) && endDaTe.isBefore(endDate)) {
                    return true;
                }
                if (startDate.isAfter(startDaTe) && endDate.isBefore(endDaTe)) {
                    return true;
                }
                if (endDaTe.isEqual(startDate) || endDaTe.isEqual(endDate)) {
                    return true;
                }

            }


            if (startDaTe.isEqual(startDate) || startDaTe.isEqual(endDate)) {
                return true;
            }



        }
        UnavailPos = 0;

        return false;


    }
}
