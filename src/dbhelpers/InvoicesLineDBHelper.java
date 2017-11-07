package dbhelpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class InvoicesLineDBHelper extends AbstractDBHelper {

    public static void deleteLinesByInvoiceNumber(SessionFactory sessFact, String invoiceNumber){
        Session session = sessFact.openSession();
        Transaction tr = session.beginTransaction();

        Query query = session.createQuery("DELETE FROM InvoicesLinesEntity WHERE invoiceNumber =:invoiceNumber");
        query.setParameter("invoiceNumber", invoiceNumber);
        query.executeUpdate();

        tr.commit();
        session.close();
    }
}
