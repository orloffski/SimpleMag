package reports;

import model.InvoiceHeader;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.SessionFactory;

public class TTNWithApplicationReport extends AbstractTTNReport implements Runnable{

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
        // check summ rows height in point or twips
        // if height after copy new row bigger then 297 - set rowbreak
        // 1 point = 0.352mm
        // 1 point = 20 twips
        // 1 twip = 0.0176mm

    }
}
