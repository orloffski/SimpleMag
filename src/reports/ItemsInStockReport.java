package reports;

import dbhelpers.*;
import entity.*;
import javafx.application.Platform;
import model.ItemsCount;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.hibernate.SessionFactory;
import utils.MessagesUtils;
import view.AbstractController;


import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.List;

public class ItemsInStockReport implements Runnable{

    private Thread t;
    private SessionFactory sessFact;
    private AbstractController controller;

    public ItemsInStockReport(SessionFactory sessFact, AbstractController controller) {
        this.controller = controller;
        this.sessFact = sessFact;
        t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        getItemsInStock(sessFact);
    }

    public void getItemsInStock(SessionFactory sessFact) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        List<CounterpartiesEntity> counterpartiesList = CounterpartiesDBHelper.getCounterpartiesEntitiesList(sessFact);

        for (CounterpartiesEntity counterpartiesEntity : counterpartiesList) {
            List<ItemsCount> items = getItemsByCounterparty(counterpartiesEntity.getId());

            if (items == null)
                continue;

            for (ItemsCount item : items) {
                getLastVendorPrice(sessFact, item);
                getItemName(sessFact, item);
            }

            addToExcellSheet(items, counterpartiesEntity.getName(), workbook);
        }

        saveToFile(workbook);
        saveToDB(sessFact);

        Platform.runLater(() ->{
            MessagesUtils.showShortInfo("Завершение работы отчета",
                    "Отчет \"Остатки на складе\" успешно завершен.");
            controller.updateForm();
        });
    }

    private List<ItemsCount> getItemsByCounterparty(int counterpartyId) {
        List<ItemsCount> items = ProductsInStockDBHelper.getStockItemsByCounterparty(counterpartyId);

        return items;
    }

    private void getItemName(SessionFactory sessFact, ItemsCount item){
        ItemsEntity itemsEntity = ItemsDBHelper.getItemsEntityById(sessFact, item.getItemId());

        item.setItemName(itemsEntity.getName());
    }

    private void getLastVendorPrice(SessionFactory sessFact, ItemsCount item) {
        List<InvoicesLinesEntity> unitsList = InvoicesLineDBHelper.getLinesByItemId(sessFact, item.getItemId());

        for (InvoicesLinesEntity unit : unitsList) {
            InvoicesHeadersEntity header = InvoicesHeaderDBHelper.getInvoiceHeaderEntityByNum(sessFact, unit.getInvoiceNumber());

            if (header.getStatus().equalsIgnoreCase("проведен")) {
                item.setVendor_price(unit.getVendorPrice() + unit.getSummVat()/unit.getCount());
                if(!Double.isNaN(item.getVendor_price()))
                    item.setVendor_price(new BigDecimal(item.getVendor_price()).setScale(2, RoundingMode.HALF_UP).doubleValue());
                else
                    item.setVendor_price(0d);
                break;
            }
        }
    }

    private void addToExcellSheet(List<ItemsCount> items, String counterpartiesName, HSSFWorkbook workbook) {
        HSSFSheet sheet = workbook.createSheet(counterpartiesName);
        int startRow = 0;
        int endRow = 0;

        int rownum = 0;
        Cell cell;
        Row row;
        //
        HSSFCellStyle style = createStyleForTitle(workbook);

        row = sheet.createRow(rownum);

        // head doc
        // item name
        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("Товар");
        cell.setCellStyle(style);
        // items count
        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("Количество");
        cell.setCellStyle(style);
        // vendor price
        cell = row.createCell(2, CellType.STRING);
        cell.setCellValue("цена поставщика с НДС");
        cell.setCellStyle(style);
        // line price
        cell = row.createCell(3, CellType.STRING);
        cell.setCellValue("Сумма строки");
        cell.setCellStyle(style);

        startRow = rownum + 3;
        rownum += 1;

        // body
        for(ItemsCount item : items){

            if(item.getItemCount() == 0.d)
                continue;

            rownum++;
            row = sheet.createRow(rownum);

            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue(item.getItemName());

            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue(item.getItemCount());

            cell = row.createCell(2, CellType.NUMERIC);
            cell.setCellValue(item.getVendor_price());

            cell = row.createCell(3, CellType.FORMULA);
            cell.setCellFormula("B" + (rownum + 1) + "*C" + (rownum + 1));

            endRow = rownum + 1;
        }

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);

        // add footer
        if(endRow == 0) // остатки в нуле
            return;

        rownum++;
        rownum++;
        row = sheet.createRow(rownum);

        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("Итого");
        cell.setCellStyle(style);

        cell = row.createCell(1, CellType.FORMULA);
        cell.setCellFormula("SUM(B" + startRow + ":B" + endRow + ")");
        cell.setCellStyle(style);

        cell = row.createCell(3, CellType.FORMULA);
        cell.setCellFormula("SUM(D" + startRow + ":D" + endRow + ")");
        cell.setCellStyle(style);
    }

    private String createFileName(){
        Calendar calendar = Calendar.getInstance();

        int Date = calendar.get(Calendar.DAY_OF_MONTH);
        int Month = calendar.get(Calendar.MONTH) + 1;
        int Year = calendar.get(Calendar.YEAR);

        StringBuilder createdfilename =
                new StringBuilder("../items_in_stock/");

        if(Date >= 10)
            createdfilename.append(Date);
        else
            createdfilename.append("0").append(Date);

        createdfilename.append(".");

        if(Month >= 10)
            createdfilename.append(Month);
        else
            createdfilename.append("0").append(Month);

        createdfilename.append(".").append(Year).append(".xls");

        return createdfilename.toString();
    }

    private HSSFCellStyle createStyleForTitle(HSSFWorkbook workbook) {
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        return style;
    }

    private void saveToFile(HSSFWorkbook workbook){
        String filename = createFileName();
        File file = new File(new File("").getAbsolutePath(), filename);

        if(file.exists())
            file.delete();

        try(FileOutputStream outFile = new FileOutputStream(file)) {
            workbook.write(outFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveToDB(SessionFactory sessFact){
        Calendar calendar = Calendar.getInstance();

        int Date = calendar.get(Calendar.DAY_OF_MONTH);
        int Month = calendar.get(Calendar.MONTH) + 1;
        int Year = calendar.get(Calendar.YEAR);

        StringBuilder filename = new StringBuilder("")
                        .append(Year).append(".")
                        .append(Month).append(".")
                        .append(Date);

        ItemsInStockDBHelper.saveOrUpdate(
                sessFact,
                new ItemsInStockEntity(filename.toString()));
    }
}
