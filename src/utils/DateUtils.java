package utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static final String transformDateFromDB(String dateToTransform){
        StringBuilder newDate = new StringBuilder();

        String[] tmpString = dateToTransform.split(" ")[0].split("-");

        newDate.append(tmpString[2]).append(".")
                .append(tmpString[1]).append(".")
                .append(tmpString[0]);

        return newDate.toString();
    }

    public static final String getDate(){
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        Date date = new Date();

        return dateFormat.format(date);
    }
}
