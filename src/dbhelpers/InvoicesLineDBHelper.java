package dbhelpers;

import entity.InvoicesHeadersEntity;
import entity.InvoicesLinesEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
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

    public static List<InvoicesLinesEntity> getLinesByItemId(SessionFactory sessFact, int itemId){
        Session session = sessFact.openSession();
        Transaction tr = session.beginTransaction();

        Query query = session.createQuery("FROM InvoicesLinesEntity WHERE itemId =:itemId ORDER BY id DESC");
        query.setParameter("itemId", itemId);
        List<InvoicesLinesEntity> unitsList = query.list();

        tr.commit();
        session.close();

        return unitsList;
    }

    public static InvoicesLinesEntity getLastInvoiceLineByItemId(SessionFactory sessFact, int itemId, List<InvoicesHeadersEntity> headers){
        Session session = sessFact.openSession();
        Transaction tr = session.beginTransaction();

        List<InvoicesLinesEntity> unitsList = new ArrayList<>();

        for(InvoicesHeadersEntity header : headers){
            Query query = session.createQuery("FROM InvoicesLinesEntity " +
                    "WHERE itemId =:itemId AND invoiceNumber =:invoiceNumber ORDER BY id DESC");
            query.setParameter("itemId", itemId);
            query.setParameter("invoiceNumber", header.getNumber());
            query.setMaxResults(1);

            unitsList = query.list();

            if(unitsList.size() > 0)
                break;
        }

        tr.commit();
        session.close();

        if(unitsList.size() > 0)
            return unitsList.get(0);

        return null;
    }
}
