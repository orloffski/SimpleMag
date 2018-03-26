package utils;

public class DateUtils {
    public static final String transformDateFromDB(String dateToTransform){
        StringBuilder newDate = new StringBuilder();

        String[] tmpString = dateToTransform.split(" ")[0].split("-");

        newDate.append(tmpString[2]).append(".")
                .append(tmpString[1]).append(".")
                .append(tmpString[0]);

        return newDate.toString();
    }
}
