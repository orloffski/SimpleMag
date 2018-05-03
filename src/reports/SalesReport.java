package reports;

import dbhelpers.CounterpartiesDBHelper;
import dbhelpers.SalesHeaderDBHelper;
import dbhelpers.SalesLinesDBHelper;
import dbhelpers.SalesReportsDBHelper;
import entity.CounterpartiesEntity;
import entity.SalesHeaderEntity;
import entity.SalesLineEntity;
import entity.SalesReportsEntity;
import javafx.application.Platform;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.hibernate.SessionFactory;
import utils.MessagesUtils;
import view.AbstractController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SalesReport implements Runnable {

    private Thread t;
    private SessionFactory sessFact;
    private AbstractController controller;

    private LocalDate dateFrom;
    private LocalDate dateTo;

    private List<SalesHeaderEntity> salesHeaders;
    private List<SalesLineEntity> salesLines;
    private List<CounterpartiesEntity> counterpartiesEntities;

    String reportFileName;

    public SalesReport(SessionFactory sessFact, AbstractController controller, LocalDate dateFrom, LocalDate dateTo) {
        this.controller = controller;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.sessFact = sessFact;
        t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        salesHeaders = SalesHeaderDBHelper.getSalesFromDateToDate(sessFact, dateFrom, dateTo);
        List<String> headersList = createHeadersList(salesHeaders);

        salesLines = SalesLinesDBHelper.getLinesByHeadersList(sessFact, headersList);
        counterpartiesEntities = CounterpartiesDBHelper.getCounterpartiesEntitiesList(sessFact);

        HSSFWorkbook workbook = createXLS();
        saveToFile(workbook);
        saveToDB();

        Platform.runLater(() ->{
            MessagesUtils.showShortInfo("Завершение работы отчета",
                    "Отчет \"Отчет о продажах\" успешно завершен.");
            controller.updateForm();
        });
    }

    private HSSFWorkbook createXLS(){
        HSSFWorkbook workbook = new HSSFWorkbook();
        Sheet sheet = null;
        int counterpartyNum = 0;
        int startRowNum = 0;
        Row row = null;
        Cell cell;

        for(SalesLineEntity salesLineEntity : salesLines){
            if(counterpartyNum != salesLineEntity.getCounterpartyId()){
                String counterpartyName = CounterpartiesDBHelper.getCounterpartyBiId(sessFact, salesLineEntity.getCounterpartyId()).getName();
                sheet = createSheet(workbook, counterpartyName);
                counterpartyNum = salesLineEntity.getCounterpartyId();
                startRowNum = 0;

                row = sheet.createRow(startRowNum);

                cell = row.createCell(0, CellType.STRING);
                cell.setCellValue("Товар");
                // items count
                cell = row.createCell(1, CellType.STRING);
                cell.setCellValue("Количество");

                startRowNum += 2;
            }

            row = sheet.createRow(startRowNum);

            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue(salesLineEntity.getItemName());

            cell = row.createCell(1, CellType.NUMERIC);
            cell.setCellValue(salesLineEntity.getCount());

            startRowNum +=1;
        }

        return workbook;
    }

    private Sheet createSheet(Workbook workbook, String sheetName){
        return workbook.createSheet(sheetName);
    }

    private void saveToFile(HSSFWorkbook workbook){
        reportFileName = createFileName();
        File file = new File(new File("").getAbsolutePath(), reportFileName);

        if(file.exists())
            file.delete();

        try(FileOutputStream outFile = new FileOutputStream(file)) {
            workbook.write(outFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveToDB(){
        StringBuilder interval = new StringBuilder();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

        if(dateFrom.isEqual(dateTo))
            interval.append(formatter.format(java.sql.Date.valueOf(dateTo)));
        else
            interval.append(formatter.format(java.sql.Date.valueOf(dateFrom)) + "-" + formatter.format(java.sql.Date.valueOf(dateTo)));

        SalesReportsDBHelper.saveOrUpdate(sessFact, new SalesReportsEntity(interval.toString()));
    }

    private List<String> createHeadersList(List<SalesHeaderEntity> sales){
        List<String> headers = new ArrayList<>();

        for(SalesHeaderEntity sale : sales){
            headers.add(sale.getSalesNumber());
        }

        return headers;
    }

    private String createFileName(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

        StringBuilder createdfilename =
                new StringBuilder("../sales_reports/");

        if(dateFrom.isEqual(dateTo))
            createdfilename.append(formatter.format(java.sql.Date.valueOf(dateTo))).append(".xls");
        else
            createdfilename.append(formatter.format(java.sql.Date.valueOf(dateFrom)) + "-" + formatter.format(java.sql.Date.valueOf(dateTo))).append(".xls");

        return createdfilename.toString();
    }
}
