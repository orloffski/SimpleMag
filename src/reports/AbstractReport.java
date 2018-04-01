package reports;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public abstract class AbstractReport {

    public static final String TMP_FILE_PATH = "../tmp/tmp_xls.xls";

    protected abstract void addTmpDocLines(Workbook workbook);

    protected void createTmpDoc(String template){
        Workbook workbook;

        try {
            workbook = createTmpDocHeader(template);
            addTmpDocLines(workbook);
            saveTmpDoc(workbook);
            openTmpDoc();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected Workbook createTmpDocHeader(String template){
        File file = new File(new File("").getAbsolutePath(), template);

        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }

        return workbook;
    }

    void saveTmpDoc(Workbook workbook){
        File file = new File(new File("").getAbsolutePath(), TMP_FILE_PATH);

        if(file.exists())
            file.delete();

        try(FileOutputStream outFile = new FileOutputStream(file)) {
            workbook.write(outFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void openTmpDoc() throws IOException {
        Desktop.getDesktop().open(new File(TMP_FILE_PATH));
    }
}
