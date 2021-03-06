package reports;

import dbhelpers.InvoicesHeaderDBHelper;
import dbhelpers.InvoicesLineDBHelper;
import entity.InvoicesHeadersEntity;
import entity.InvoicesLinesEntity;
import model.InvoiceHeader;
import model.types.InvoicesTypes;
import org.apache.poi.ss.usermodel.*;
import org.hibernate.SessionFactory;
import utils.DateUtils;
import utils.MoneyInWords;
import utils.RowCopy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class TTNOneListReport extends AbstractTTNReport implements Runnable{

    private Thread t;
    private SessionFactory sessFact;
    private InvoiceHeader invoice;

    public static final String TEMPLATE_FILE_PATH = "../report_templates/ttn_one_list_template.xls";

    public TTNOneListReport(SessionFactory sessFact, InvoiceHeader invoice) {
        this.sessFact = sessFact;
        this.invoice = invoice;
        this.t = new Thread(this);
        this.t.start();
    }

    @Override
    public void run() {
        createTmpDoc(TEMPLATE_FILE_PATH);
    }

    @Override
    protected void addTmpDocLines(Workbook workbook) {
        Sheet s = workbook.getSheetAt(0);

        String tmp = "Белыничское_РАЙПОг.Белыничи адрес 213051 Могилевская обл.г.п.Белыничи ул.Ленинская 34";
        String unp = "700267438";

        int rowNum = 42;
        int counter = 0;
        double summVat = 0;
        double summRetailPrices =0;
        double summIncludeVat =0;
        Row row;
        Cell cell;
        List<InvoicesLinesEntity> lines = InvoicesLineDBHelper.getLinesByInvoiceNumber(sessFact, invoice.getNumber());

        // print header
        row = s.getRow(7);
        cell = row.getCell(11, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        cell.setCellValue(unp);

        addAlert(workbook, 7, 15);

        row = s.getRow(7);
        cell = row.getCell(19, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        cell.setCellValue(unp);

        row = s.getRow(22);
        cell = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        cell.setCellValue(DateUtils.getDate());

        addAlert(workbook, 24, 3);
        addAlert(workbook, 24, 31);
        addAlert(workbook, 26, 5);
        addAlert(workbook, 26, 23);

        row = s.getRow(28);
        cell = row.getCell(10, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        cell.setCellValue(tmp);

        addAlert(workbook, 30, 4);
        addAlert(workbook, 32, 4);
        addAlert(workbook, 34, 4);
        addAlert(workbook, 34, 17);
        addAlert(workbook, 34, 28);
        addAlert(workbook, 36, 3);

        for(InvoicesLinesEntity line : lines){
            if(rowNum != 42){
                RowCopy.copyRow(s, 42, rowNum);
                counter++;
            }

            row = s.getRow(rowNum);
            cell = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellValue(line.getItemName());

            cell = row.getCell(8, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellValue(line.getCount());

            if(invoice.getType().equalsIgnoreCase(InvoicesTypes.DELIVERY.toString())) {
                cell = row.getCell(11, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                cell.setCellValue(line.getRetailPrice());

                cell = row.getCell(14, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                cell.setCellValue(line.getRetailPrice() * line.getCount());

                summRetailPrices += line.getRetailPrice() * line.getCount();

//                cell = row.getCell(31, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
//                cell.setCellValue("Цена поставщика: " + line.getVendorPrice() + "\n" +
//                "торговая надбавка: " +
//                        new BigDecimal(
//                                (line.getRetailPrice() - line.getVendorPrice() - line.getSummVat()/line.getCount())
//                        ).setScale(2, RoundingMode.HALF_UP).doubleValue()
//                );

                row.setHeightInPoints((2*s.getDefaultRowHeightInPoints()));

                cell = row.getCell(19, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
//                cell.setCellValue(line.getSummVat());
                double vatSum = new BigDecimal(line.getRetailPrice() * line.getCount() * line.getVat() / (100 + line.getVat())).setScale(2, RoundingMode.HALF_UP).doubleValue();
                cell.setCellValue(vatSum);

                cell = row.getCell(22, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                cell.setCellValue(line.getRetailPrice() * line.getCount());

                summVat += vatSum;

            }else if(invoice.getType().equalsIgnoreCase(InvoicesTypes.RETURN.toString())){
                // get last vendor price
                List<InvoicesHeadersEntity> headersEntities = InvoicesHeaderDBHelper.getHeadersByCounterpartyId(sessFact, invoice.getCounterpartyId());
                InvoicesLinesEntity linesEntity = InvoicesLineDBHelper.getLastInvoiceLineByItemId(sessFact, line.getItemId(), headersEntities);

                cell = row.getCell(11, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                cell.setCellValue(linesEntity.getVendorPrice());

                cell = row.getCell(14, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                cell.setCellValue(linesEntity.getVendorPrice() * line.getCount());

                summRetailPrices += linesEntity.getVendorPrice() * line.getCount();

                double vatFromVendor = (linesEntity.getVendorPrice() * (line.getVat())/100) * line.getCount();
                vatFromVendor = new BigDecimal(vatFromVendor).setScale(2, RoundingMode.HALF_UP).doubleValue();

                cell = row.getCell(19, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                cell.setCellValue(vatFromVendor);

                cell = row.getCell(22, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                cell.setCellValue(linesEntity.getVendorPrice() * line.getCount() + vatFromVendor);

                summIncludeVat += linesEntity.getVendorPrice() * line.getCount() + vatFromVendor;
                summVat += vatFromVendor;
            }

            cell = row.getCell(17, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellValue(line.getVat());

            cell = row.getCell(25, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellValue(line.getCount());

            rowNum++;
        }

        // print summs
        row = s.getRow(43 + counter);
        cell = row.getCell(8, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        cell.setCellValue(invoice.getCount());

        row = s.getRow(43 + counter);
        cell = row.getCell(14, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        cell.setCellValue(summRetailPrices);

        row = s.getRow(43 + counter);
        cell = row.getCell(19, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        cell.setCellValue(summVat);

        row = s.getRow(43 + counter);
        cell = row.getCell(22, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        if(invoice.getType().equalsIgnoreCase(InvoicesTypes.DELIVERY.toString())) {
            cell.setCellValue(invoice.getFullSumm());
        }else if(invoice.getType().equalsIgnoreCase(InvoicesTypes.RETURN.toString())){
            cell.setCellValue(summIncludeVat);
        }

        row = s.getRow(43 + counter);
        cell = row.getCell(25, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        cell.setCellValue(invoice.getCount());

        // print footer
        if(invoice.getType().equalsIgnoreCase(InvoicesTypes.DELIVERY.toString())) {
            row = s.getRow(46 + counter);
            cell = row.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellValue(MoneyInWords.inwords(summVat));

            row = s.getRow(48 + counter);
            cell = row.getCell(5, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellValue(MoneyInWords.inwords(invoice.getFullSumm()));
        }else if(invoice.getType().equalsIgnoreCase(InvoicesTypes.RETURN.toString())){
            row = s.getRow(46 + counter);
            cell = row.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellValue(MoneyInWords.inwords(summVat));

            row = s.getRow(48 + counter);
            cell = row.getCell(5, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellValue(MoneyInWords.inwords(summIncludeVat));
        }

        row = s.getRow(50 + counter);
        cell = row.getCell(22, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        cell.setCellValue(
                MoneyInWords.inwords(
                        (double)invoice.getCount()
                ).split(" белорусс")[0]);

        addAlert(workbook, 52 + counter, 4);
        addAlert(workbook, 54 + counter, 5);
    }

    private CellStyle getAlertStyle(Workbook workbook){
        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();

        font.setBold(true);
        font.setColor((short) 10);

        cellStyle.setFont(font);

        return cellStyle;
    }

    private void addAlert(Workbook workbook, int rowNum, int cellNum){
        Sheet s = workbook.getSheetAt(0);
        Row row = s.getRow(rowNum);
        Cell cell = row.getCell(cellNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        cell.setCellStyle(getAlertStyle(workbook));
        cell.setCellValue("ЗАПОЛНИТЬ");
    }
}
