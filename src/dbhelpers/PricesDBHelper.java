package dbhelpers;

import entity.PricesEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class PricesDBHelper extends AbstractDBHelper{

    public static PricesEntity getLastPriceByItemId(SessionFactory sessFact, int itemId){
        Session session = sessFact.openSession();
        Transaction tr = session.beginTransaction();

        Query query = session.createQuery("FROM PricesEntity WHERE itemId =:itemId ORDER BY lastcreated DESC");
        query.setParameter("itemId", itemId);
        query.setMaxResults(1);

        List<PricesEntity> pricesList = query.list();

        tr.commit();
        session.close();

        if(pricesList.size() > 0)
            return pricesList.get(0);

        return null;
    }

    public static void deletePricesByInvoiceNumber(SessionFactory sessFact, String invoiceNumber){
        Session session = sessFact.openSession();
        Transaction tr = session.beginTransaction();

        Query query = session.createQuery("DELETE FROM PricesEntity WHERE reason =:invoiceNumber");
        query.setParameter("invoiceNumber", invoiceNumber);
        query.executeUpdate();

        tr.commit();
        session.close();
    }
}
