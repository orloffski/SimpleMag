package reports;

import model.InvoiceHeader;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.hibernate.SessionFactory;
import utils.RowCopy;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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

    private void addTmpDocLines(Workbook workbook){
        Sheet s = workbook.getSheetAt(0);

        int sourceRowNo = 9;

        for(int i = 0; i < 55; i++){
            RowCopy.copyRow(s, sourceRowNo, sourceRowNo + 1);
            sourceRowNo += 1;
        }
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
