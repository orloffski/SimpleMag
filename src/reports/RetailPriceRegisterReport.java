package reports;

import dbhelpers.InvoicesLineDBHelper;
import dbhelpers.ItemsDBHelper;
import dbhelpers.UnitsDBHelper;
import entity.InvoicesLinesEntity;
import model.InvoiceHeader;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.hibernate.SessionFactory;
import utils.RowCopy;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class RetailPriceRegisterReport implements Runnable{

    public static final String HEAD_1 = "Реестр розничных цен к накладной № ";
    public static final String HEAD_2 = "Белыничское_РАЙПО, г.Круглое ТЦ \"Днепр\"";

    public static final String TEMPLATE_FILE_PATH = "../report_templates/retail_price_register_template.xls";
    public static final String TMP_FILE_PATH = "../tmp/retail_price_register.xls";

    private Thread t;
    private SessionFactory sessFact;
    private InvoiceHeader header;

    public RetailPriceRegisterReport(SessionFactory sessFact, InvoiceHeader header) {
        this.sessFact = sessFact;
        this.header = header;
        t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        createTmpDoc();
    }

    private void createTmpDoc(){
        Workbook workbook;

        try {
            workbook = createTmpDocHeader();
            addTmpDocLines(workbook);
            saveTmpDoc(workbook);
            openTmpDoc();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
    }

    private Workbook createTmpDocHeader() throws IOException, InvalidFormatException {
        File file = new File(new File("").getAbsolutePath(), TEMPLATE_FILE_PATH);

        Workbook workbook = WorkbookFactory.create(file);
        Sheet s = workbook.getSheetAt(0);

        Row row = s.getRow(1);
        Cell cell = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

        cell.setCellValue(HEAD_1 + " " + header.getTtnNo() + " от " + header.getTtnDate());

        row = s.getRow(4);
        cell = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

        cell.setCellValue(HEAD_2);

        return workbook;
    }

    private void addTmpDocLines(Workbook workbook) {
        Sheet s = workbook.getSheetAt(0);

        CellStyle cellStyle = getNumericCellStyle(workbook);

        int startRowNum = 10;
        int sourceRowNo = 9;
        int number = 1;

        List<InvoicesLinesEntity> lines = InvoicesLineDBHelper.getLinesByInvoiceNumber(sessFact, header.getNumber());

        for(InvoicesLinesEntity line : lines){
            RowCopy.copyRow(s, sourceRowNo, sourceRowNo + 1);

            Row row = s.getRow(sourceRowNo);

            // line number
            Cell cell = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellValue(number);

            // item name
            cell = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellValue(line.getItemName());

            // unit name
            cell = row.getCell(6, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellValue(
                    UnitsDBHelper.getUnitById(
                            sessFact,
                            ItemsDBHelper.getItemsEntityById(sessFact, line.getItemId()).getUnitId()
                    ));

            // items count
            cell = row.getCell(7, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(line.getCount());

            // vendor price
            cell = row.getCell(8, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(cellStyle);;
            cell.setCellValue(line.getVendorPrice());

            // vendor summ
            cell = row.getCell(9, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(line.getVendorPrice() * line.getCount());

            // extra price
            cell = row.getCell(16, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellValue(line.getExtraPrice().toString() + "%");

            // vat
            cell = row.getCell(18, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellValue(line.getVat() + "%");

            // vat
            cell = row.getCell(19, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(line.getSummVat());

            // retail price
            cell = row.getCell(20, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(line.getRetailPrice());

            // retail summ
            cell = row.getCell(21, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(line.getRetailPrice() * line.getCount());

            sourceRowNo += 1;

            number++;
        }

        s.removeRow(s.getRow(sourceRowNo));

        sourceRowNo += 2;
        int endRowNum = sourceRowNo -2;

        Row row = s.getRow(sourceRowNo);

        StringBuilder summCount = new StringBuilder();
        StringBuilder vendorSumm = new StringBuilder();
        StringBuilder retailVat = new StringBuilder();
        StringBuilder retailSumm = new StringBuilder();
        for(int i = startRowNum; i <= endRowNum; i++){
            summCount.append("H").append(i);
            vendorSumm.append("J").append(i);
            retailVat.append("T").append(i);
            retailSumm.append("V").append(i);
            if(i != endRowNum) {
                summCount.append("+");
                vendorSumm.append("+");
                retailVat.append("+");
                retailSumm.append("+");
            }
        }

        // count summ formula
        Cell cell = row.getCell(7, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        cell.setCellStyle(cellStyle);
        cell.setCellFormula(summCount.toString());

        // vendor summ formula
        cell = row.getCell(9, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        cell.setCellStyle(cellStyle);
        cell.setCellFormula(vendorSumm.toString());

        // retail vat summ formula
        cell = row.getCell(19, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        cell.setCellStyle(cellStyle);
        cell.setCellFormula(retailVat.toString());

        // retail summs formula
        cell = row.getCell(21, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        cell.setCellStyle(cellStyle);
        cell.setCellFormula(retailSumm.toString());

    }

    private CellStyle getNumericCellStyle(Workbook workbook){
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(
                workbook.getCreationHelper().createDataFormat().getFormat("#.##"));
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);

        return cellStyle;
    }

    private void saveTmpDoc(Workbook workbook){
        File file = new File(new File("").getAbsolutePath(), TMP_FILE_PATH);

        if(file.exists())
            file.delete();

        try(FileOutputStream outFile = new FileOutputStream(file)) {
            workbook.write(outFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openTmpDoc() throws IOException {
        Desktop.getDesktop().open(new File(TMP_FILE_PATH));
    }
}
