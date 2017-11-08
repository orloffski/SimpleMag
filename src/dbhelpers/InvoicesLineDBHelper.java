package dbhelpers;

import entity.InvoicesLinesEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

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

    public static void deleteLinesById(SessionFactory sessFact, int id){
        Session session = sessFact.openSession();
        Transaction tr = session.beginTransaction();

        Query query = session.createQuery("DELETE FROM InvoicesLinesEntity WHERE id =:id");
        query.setParameter("id", id);
        query.executeUpdate();

        tr.commit();
        session.close();
    }

    public static List<InvoicesLinesEntity> getLinesByInvoiceNumber(SessionFactory sessFact, String invoiceNumber){
        Session session = sessFact.openSession();
        Transaction tr = session.beginTransaction();

        Query query = session.createQuery("FROM InvoicesLinesEntity WHERE invoiceNumber =:invoiceNumber ORDER BY id");
        query.setParameter("invoiceNumber", invoiceNumber);
        List<InvoicesLinesEntity> unitsList = query.list();

        tr.commit();
        session.close();

        return unitsList;
    }
}
