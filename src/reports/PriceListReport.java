package reports;

import dbhelpers.InvoicesLineDBHelper;
import dbhelpers.ItemsDBHelper;
import entity.InvoicesLinesEntity;
import entity.ItemsEntity;
import model.InvoiceHeader;
import model.Items;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.SessionFactory;

import java.util.List;

public class PriceListReport extends AbstractReport implements Runnable{

    public static final String HEADER_ONE = "Белыничское_РАЙПО";

    public static final String TEMPLATE_FILE_PATH = "../report_templates/prices_list_template.xls";

    private Thread t;
    private SessionFactory sessFact;
    private List<ItemsEntity> items;

    // print from InvoiceHeader
    public PriceListReport(SessionFactory sessFact, InvoiceHeader header) {
        this.sessFact = sessFact;

        List<InvoicesLinesEntity> lines = InvoicesLineDBHelper.getLinesByInvoiceNumber(sessFact, header.getNumber());

        this.items.clear();

        for(InvoicesLinesEntity line : lines){
            this.items.add(ItemsDBHelper.getItemsEntityById(sessFact, line.getItemId()));
        }

        this.t = new Thread(this);
        this.t.start();
    }

    // print from ItemsList
    public PriceListReport(SessionFactory sessFact, List<Items> itemsList) {
        this.sessFact = sessFact;

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

    }
}
