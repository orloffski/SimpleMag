package dbhelpers;

import entity.ProductsInStockEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class ProductsInStockDBHelper extends AbstractDBHelper {

    public static void deleteByInvoiceNumber(SessionFactory sessFact, String invoiceNumber){
        Session session = sessFact.openSession();
        Transaction tr = session.beginTransaction();

        Query query = session.createQuery("DELETE FROM ProductsInStockEntity WHERE invoiceNumber =:invoiceNumber");
        query.setParameter("invoiceNumber", invoiceNumber);
        query.executeUpdate();

        tr.commit();
        session.close();
    }

    public static ProductsInStockEntity fullFindLines(SessionFactory sessFact, int itemId, int counerpartyId, String expireDate){
        Session session = sessFact.openSession();
        Transaction tr = session.beginTransaction();

        Query query;

        if(!expireDate.equalsIgnoreCase("")) {
            query = session.createQuery("FROM ProductsInStockEntity WHERE itemId =:itemId AND counterpartyId =:counerpartyId AND expireDate =:expireDate ORDER BY id DESC");
            query.setParameter("itemId", itemId);
            query.setParameter("counerpartyId", counerpartyId);
            query.setParameter("expireDate", expireDate);
        }else{
            query = session.createQuery("FROM ProductsInStockEntity WHERE itemId =:itemId AND counterpartyId =:counerpartyId");
            query.setParameter("itemId", itemId);
            query.setParameter("counerpartyId", counerpartyId);
        }

        List<ProductsInStockEntity> list = query.list();

        tr.commit();
        session.close();

        if(list.size() > 0)
            return list.get(0);

        return null;
    }
}
