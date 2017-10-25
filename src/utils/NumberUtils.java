package utils;

import application.DBClass;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NumberUtils {

    public static final String getNextDocNumber(String newType){
        String number = "";
        int numberInt;
        String SQL = "SELECT * FROM invoices_headers WHERE number LIKE '" + newType + "%' ORDER BY id DESC LIMIT 1";

        Connection connection = null;
        try {
            connection = new DBClass().getConnection();
            ResultSet rs = connection.createStatement().executeQuery(SQL);

            if(rs.next()){
                number = rs.getString("number");
                number = number.substring(3, number.length());
            }else{
                number = "0";
            }

            numberInt = Integer.parseInt(number);
            numberInt++;
            number = String.format("%06d", numberInt);
            number = newType + number;

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }


        return number;
    }

    // REC = RECEIPT
    // RET = RETURN
    // DEL = DELIVERY
    // INI = INITIAL
    public static final String getDocSuffix(String docType){
        String newType = "";

        switch (docType){
            case "Поступление":
                newType = "REC";
                break;
            case "Возврат":
                newType = "RET";
                break;
            case "Перемещение":
                newType = "DEL";
                break;
            case "Ввод начальных остатков":
                newType = "INI";
                break;
        }

        return newType;
    }

    public static final String getNextCheckNumber(String newType){
        String number = "";
        int numberInt;
        String SQL = "SELECT * FROM sales_header WHERE sales_number LIKE '" + newType + "%' ORDER BY id DESC LIMIT 1";

        Connection connection = null;
        try {
            connection = new DBClass().getConnection();
            ResultSet rs = connection.createStatement().executeQuery(SQL);

            if(rs.next()){
                number = rs.getString("sales_number");
                number = number.substring(3, number.length());
            }else{
                number = "0";
            }

            numberInt = Integer.parseInt(number);
            numberInt++;
            number = String.format("%06d", numberInt);
            number = newType + number;

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }


        return number;
    }

    public static final String getCheckSuffix(String docType){
        String newType = "";

        switch (docType){
            case "покупка":
                newType = "SAL";
                break;
            case "возврат":
                newType = "RET";
                break;
        }

        return newType;
    }

    public static final Double round(Double forRound){
        Double number = Double.parseDouble(String.format( "%.2f", forRound).replace(",","."));

        return number;
    }
}
