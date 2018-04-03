package reports;

import model.InvoiceHeader;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.SessionFactory;
import utils.RowCopy;

public class TTNOneListReport extends AbstractReport implements Runnable{

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

       double height = 0;

//        for (int i = 0; i < 5; i++) {
//            RowCopy.copyRow(s, i, i+1);
//            height += s.getRow(i).getHeight();
//        }
    }
}
