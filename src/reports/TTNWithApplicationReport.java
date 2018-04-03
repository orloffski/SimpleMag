package reports;

import model.InvoiceHeader;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.SessionFactory;

public class TTNWithApplicationReport extends AbstractReport implements Runnable{

    private Thread t;
    private SessionFactory sessFact;
    private InvoiceHeader invoice;

    public static final String TEMPLATE_FILE_PATH = "../report_templates/ttn_with_application_template.xls";

    public TTNWithApplicationReport(SessionFactory sessFact, InvoiceHeader invoice) {
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

    }
}
