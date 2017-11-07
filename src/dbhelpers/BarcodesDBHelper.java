package dbhelpers;

import entity.BarcodesEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class BarcodesDBHelper extends AbstractDBHelper {

    public static List<BarcodesEntity> getBarcodesByItemId(SessionFactory sessFact, int itemId){
        Session session = sessFact.openSession();
        Transaction tr = session.beginTransaction();

        Query query = session.createQuery("FROM BarcodesEntity WHERE itemId =:itemId");
        query.setParameter("itemId", itemId);

        List<BarcodesEntity> barcodesList = query.list();

        tr.commit();
        session.close();

        return barcodesList;
    }
}
