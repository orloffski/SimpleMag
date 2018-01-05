package dbhelpers;

import entity.InvoicesHeadersEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class InvoicesHeaderDBHelper extends AbstractDBHelper {

    public static  InvoicesHeadersEntity getInvoiceHeaderEntityById(SessionFactory sessFact, int headerId){
        Session session = sessFact.openSession();
        Transaction tr = session.beginTransaction();

        Query query = session.createQuery("FROM InvoicesHeadersEntity WHERE id =:headerId");
        query.setParameter("headerId", headerId);
        query.setMaxResults(1);

        List<InvoicesHeadersEntity> invoicesList = query.list();

        tr.commit();
        session.close();

        if(invoicesList.size() > 0)
            return invoicesList.get(0);

        return null;
    }

    public static List<InvoicesHeadersEntity> getInvoicesHeadersEntitiesList(SessionFactory sessFact){
        Session session = sessFact.openSession();
        Transaction tr = session.beginTransaction();

        List<InvoicesHeadersEntity> invoicesHeadersList = session.createQuery("FROM InvoicesHeadersEntity ").list();

        tr.commit();
        session.close();

        return invoicesHeadersList;
    }

    public static void deleteHeaderById(SessionFactory sessFact, int headerId){
        Session session = sessFact.openSession();
        Transaction tr = session.beginTransaction();

        Query query = session.createQuery("DELETE FROM InvoicesHeadersEntity WHERE id =:headerId");
        query.setParameter("headerId", headerId);
        query.executeUpdate();

        tr.commit();
        session.close();
    }
}
