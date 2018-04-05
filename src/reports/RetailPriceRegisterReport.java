package reports;

import dbhelpers.InvoicesHeaderDBHelper;
import dbhelpers.InvoicesLineDBHelper;
import dbhelpers.ItemsDBHelper;
import dbhelpers.UnitsDBHelper;
import entity.InvoicesHeadersEntity;
import entity.InvoicesLinesEntity;
import entity.ItemsEntity;
import model.InvoiceHeader;
import model.InvoicesTypes;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.hibernate.SessionFactory;
import utils.RowCopy;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class RetailPriceRegisterReport extends AbstractReport implements Runnable{

    public static final String HEAD_1 = "Реестр розничных цен к накладной № ";
    public static final String HEAD_2 = "Белыничское_РАЙПО, г.Круглое ТЦ \"Днепр\"";

    public static final String TEMPLATE_FILE_PATH = "../report_templates/retail_price_register_template.xls";

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
        createTmpDoc(TEMPLATE_FILE_PATH);
    }

    @Override
    protected Workbook createTmpDocHeader(String template) {
        File file = new File(new File("").getAbsolutePath(), template);

        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        Sheet s = workbook.getSheetAt(0);

        Row row = s.getRow(1);
        Cell cell = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

        cell.setCellValue(HEAD_1 + " " + header.getTtnNo() + " от " + header.getTtnDate());

        row = s.getRow(4);
        cell = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

        cell.setCellValue(HEAD_2);

        return workbook;
    }

    protected void addTmpDocLines(Workbook workbook) {
        Sheet s = workbook.getSheetAt(0);

//        CellStyle cellStyle = getNumericCellStyle(workbook);

        int startRowNum = 10;
        int sourceRowNo = 9;
        int number = 1;

        double count = 0;
        double vendorPrice = 0;
        double summVat = 0;
        double retailSumm = 0;

        List<InvoicesLinesEntity> lines = InvoicesLineDBHelper.getLinesByInvoiceNumber(sessFact, header.getNumber());

        for(InvoicesLinesEntity docLine : lines){
            InvoicesLinesEntity line;
            int countItems;

            if(header.getType().equalsIgnoreCase(InvoicesTypes.RETURN.toString())){
                List<InvoicesHeadersEntity> headersEntities = InvoicesHeaderDBHelper.getHeadersByCounterpartyId(sessFact, header.getCounterpartyId());
                line = InvoicesLineDBHelper.getLastInvoiceLineByItemId(sessFact, docLine.getItemId(), headersEntities);
                countItems = docLine.getCount();
            }else{
                line = docLine;
                countItems = line.getCount();
            }

            RowCopy.copyRow(s, sourceRowNo, sourceRowNo + 1);

            Row row = s.getRow(sourceRowNo);

            // line number
            Cell cell = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellValue(number);

            // item name
            cell = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellValue(line.getItemName());

            // item article
            ItemsEntity itemsEntity = ItemsDBHelper.getItemsEntityById(sessFact, line.getItemId());
            cell = row.getCell(5, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellValue(itemsEntity.getVendorCode());

            // unit name
            cell = row.getCell(6, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellValue(
                    UnitsDBHelper.getUnitById(
                            sessFact,
                            ItemsDBHelper.getItemsEntityById(sessFact, line.getItemId()).getUnitId()
                    ));

            // items count
            cell = row.getCell(7, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(createCellStyle(workbook, line.getCount(), false, (short)9));
            cell.setCellValue(countItems);

            count += countItems;

            // vendor price
            cell = row.getCell(8, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(createCellStyle(workbook, line.getVendorPrice(), false, (short)9));
            cell.setCellValue(line.getVendorPrice());

            // vendor summ
            cell = row.getCell(9, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(createCellStyle(workbook, line.getVendorPrice() * line.getCount(), false, (short)9));
            cell.setCellValue(line.getVendorPrice() * countItems);

            vendorPrice += line.getVendorPrice() * countItems;

            // extra price
            cell = row.getCell(16, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellValue(line.getExtraPrice().toString() + "%");

            // vat
            cell = row.getCell(18, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellValue(line.getVat() + "%");

            // vat
            cell = row.getCell(19, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(createCellStyle(workbook, line.getSummVat(), false, (short)9));
            cell.setCellValue(line.getVendorPrice() * line.getVat() * countItems / 100);

            summVat += line.getVendorPrice() * line.getVat() * countItems / 100;

            // retail price
            cell = row.getCell(20, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(createCellStyle(workbook, line.getRetailPrice(), false, (short)9));
            cell.setCellValue(line.getRetailPrice());

            // retail summ
            cell = row.getCell(21, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(createCellStyle(workbook, line.getRetailPrice() * line.getCount(), false, (short)9));
            cell.setCellValue(line.getRetailPrice() * countItems);

            retailSumm += line.getRetailPrice() * countItems;

            sourceRowNo += 1;

            number++;
        }

        s.removeRow(s.getRow(sourceRowNo));

        sourceRowNo += 2;

        Row row = s.getRow(sourceRowNo);

        // full count summ
        Cell cell = row.getCell(7, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        cell.setCellStyle(createCellStyle(workbook, count, true, (short)11));
        cell.setCellFormula(String.valueOf(count));

        // full vendor summ
        cell = row.getCell(9, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        cell.setCellStyle(createCellStyle(workbook, vendorPrice, true, (short)11));
        cell.setCellFormula(String.valueOf(vendorPrice));

        // full retail vat summ
        cell = row.getCell(19, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        cell.setCellStyle(createCellStyle(workbook, summVat, true, (short)11));
        cell.setCellFormula(String.valueOf(summVat));

        // full retail summs
        cell = row.getCell(21, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        cell.setCellStyle(createCellStyle(workbook, retailSumm, true, (short)11));
        cell.setCellFormula(String.valueOf(retailSumm));

        s.setFitToPage(true);

    }

    private CellStyle createCellStyle(Workbook workbook, double number, boolean bold, short size){
        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();

        if(!checkSumm(number)){
            // double
            cellStyle.setDataFormat(
                    workbook.getCreationHelper().createDataFormat().getFormat("#.##"));
        }

        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);

        if(bold){
            font.setBold(true);

            cellStyle.setFont(font);
        }

        font.setFontHeightInPoints(size);

        return cellStyle;
    }

    private boolean checkSumm(double summ){
        if ((summ == Math.floor(summ)) && !Double.isInfinite(summ)) {
            // integer type
            return true;
        }

        // double type
        return false;
    }
}
