package reports;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class TTNOneListReport extends AbstractReport {
    @Override
    protected void addTmpDocLines(Workbook workbook) {
        Sheet s = workbook.getSheetAt(0);
    }
}
