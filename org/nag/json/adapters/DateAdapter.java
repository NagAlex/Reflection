package org.nag.json.adapters;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Converts object of type java.util.Date to String by using dd/MM/yyyy format
 */
public class DateAdapter implements JsonDataAdapter<Date> {
    @Override
    public Object toJson(Date date) {
        String[] months = {"01", "02", "03", "04", "05", "06",
                           "07", "08", "09", "10", "11", "12"};
        Calendar gc = new GregorianCalendar();
        gc.setTime(date);
        String dd = gc.get(Calendar.DAY_OF_MONTH) + "/";
        if(dd.length() == 2) dd = "0" + dd;

        return dd + months[gc.get(Calendar.MONTH)] + "/" + gc.get(Calendar.YEAR);
    }
}
