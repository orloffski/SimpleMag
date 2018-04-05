package reports;

import dbhelpers.BarcodesDBHelper;
import dbhelpers.InvoicesLineDBHelper;
import dbhelpers.ItemsDBHelper;
import dbhelpers.PricesDBHelper;
import entity.InvoicesLinesEntity;
import entity.ItemsEntity;
import model.InvoiceHeader;
import model.Items;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.hibernate.SessionFactory;
import utils.RowCopy;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PriceListReport extends AbstractReport implements Runnable{

    public static final String HEADER_ONE = "Белыничское_РАЙПО";

    public static final String TEMPLATE_FILE_PATH = "../report_templates/prices_list_template.xls";

    private Thread t;
    private SessionFactory sessFact;
    private List<ItemsEntity> items;
    private InvoiceHeader header;

    // print from InvoiceHeader
    public PriceListReport(SessionFactory sessFact, InvoiceHeader header) {
        this.sessFact = sessFact;
        this.header = header;

        List<InvoicesLinesEntity> lines = InvoicesLineDBHelper.getLinesByInvoiceNumber(sessFact, header.getNumber());

        this.items = new ArrayList<>();

        for(InvoicesLinesEntity line : lines){
            this.items.add(ItemsDBHelper.getItemsEntityById(sessFact, line.getItemId()));
        }

        this.t = new Thread(this);
        this.t.start();
    }

    // print from ItemsList
    public PriceListReport(SessionFactory sessFact, List<Items> itemsList) {
        this.sessFact = sessFact;

        this.items = new ArrayList<>();

        for(Items item : itemsList){
            this.items.add(ItemsDBHelper.getItemsEntityById(sessFact, item.getId()));
        }

        this.t = new Thread(this);
        this.t.start();
    }

    @Override
    public void run() {
        createTmpDoc(TEMPLATE_FILE_PATH);
    }

    protected void addTmpDocLines(Workbook workbook) {
        Sheet s = workbook.getSheetAt(0);

        int counter = 0;
        int rowNum = 1;
        int startRowNum = 1;

        for(ItemsEntity item : items){
            counter++;

            if(counter%8 == 0)
                s.setRowBreak(rowNum + 12);

            if(counter%2 != 0){
                if(rowNum != 1) {
                    // create new prices row 1-13
                    for (int i = 0; i < 13; i++)
                        RowCopy.copyRow(s, startRowNum + i, rowNum + i);

                    try {
                        addPicture(workbook, rowNum, 1);
                        addPicture(workbook, rowNum, 7);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                createPrice(counter, rowNum, item, s);
            }else{
                createPrice(counter, rowNum, item, s);

                rowNum += 13;
                s.createRow(rowNum + 1);
                rowNum += 1;
            }
        }
    }

    private String getPriceString(String price){
        StringBuilder fullPrice = new StringBuilder();
        String[] prices = new String[]{};

        if(price.contains("."))
            prices = price.split("\\.");
        else if(price.contains(","))
            prices = price.split(",");

        if(prices.length > 0)
            fullPrice.append(prices[0]).append(" р. ");
        else
            fullPrice.append(price).append(" р. ");

        if(prices.length > 1)
            fullPrice.append(prices[1]).append(" коп.");
        else
            fullPrice.append("00 коп.");

        return fullPrice.toString();
    }

    private void createPrice(int counter, int rowNum, ItemsEntity item, Sheet s){
        Cell cell;
        Row row;

        if(counter%2 != 0){
            row = s.getRow(rowNum);
            cell = row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellValue(HEADER_ONE);

            row = s.getRow(rowNum + 1);
            cell = row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellValue(item.getName() + "\n" + item.getVendorCode());

            row = s.getRow(rowNum + 3);
            cell = row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellValue(
                    getPriceString(PricesDBHelper.getLastPriceByItemId(sessFact, item.getId()).getPrice())
            );

            row = s.getRow(rowNum + 9);
            cell = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellValue(header.getTtnNo() + " от " + header.getTtnDate());

            row = s.getRow(rowNum + 9);
            cell = row.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellValue(
                    BarcodesDBHelper.getBarcodesByItemId(sessFact, item.getId()).get(0).getBarcode()
            );

            row = s.getRow(rowNum + 12);
            cell = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellValue(item.getVendorCountry());
        }else{
            row = s.getRow(rowNum);
            cell = row.getCell(8, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellValue(HEADER_ONE);

            row = s.getRow(rowNum + 1);
            cell = row.getCell(8, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellValue(item.getName() + "\n" + item.getVendorCode());

            row = s.getRow(rowNum + 3);
            cell = row.getCell(9, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellValue(
                    getPriceString(PricesDBHelper.getLastPriceByItemId(sessFact, item.getId()).getPrice())
            );

            row = s.getRow(rowNum + 9);
            cell = row.getCell(7, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellValue(header.getTtnNo() + " от " + header.getTtnDate());

            row = s.getRow(rowNum + 9);
            cell = row.getCell(10, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellValue(
                    BarcodesDBHelper.getBarcodesByItemId(sessFact, item.getId()).get(0).getBarcode()
            );

            row = s.getRow(rowNum + 12);
            cell = row.getCell(7, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellValue(item.getVendorCountry());
        }
    }

    private void addPicture(Workbook workbook, int startRow, int startCol) throws IOException {
        Sheet sheet = workbook.getSheetAt(0);
        FileInputStream stream = new FileInputStream("./resources/images/logo.png");
        CreationHelper helper = workbook.getCreationHelper();
        Drawing drawing = sheet.createDrawingPatriarch();

        ClientAnchor anchor = helper.createClientAnchor();
        anchor.setAnchorType( ClientAnchor.AnchorType.MOVE_AND_RESIZE );


        int pictureIndex = workbook.addPicture(IOUtils.toByteArray(stream), Workbook.PICTURE_TYPE_PNG);

        anchor.setCol1( startCol );
        anchor.setRow1( startRow );
        anchor.setRow2( startRow + 2 );
        anchor.setCol2( startCol );
        final Picture pict = drawing.createPicture( anchor, pictureIndex );
        pict.resize();
    }
}
