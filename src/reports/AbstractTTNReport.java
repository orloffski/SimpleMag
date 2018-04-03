package reports;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;

public abstract class AbstractTTNReport extends AbstractReport {
    @Override
    protected Workbook createTmpDocHeader(String template) {
        // set first list
        File file = new File(new File("").getAbsolutePath(), template);
        Sheet s;

        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(file);
            s = workbook.getSheetAt(0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }

        return workbook;
    }
}
