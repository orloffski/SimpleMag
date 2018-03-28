package dbhelpers;

import entity.ItemsInStockEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class ItemsInStockDBHelper extends AbstractDBHelper  {
    public static List<ItemsInStockEntity> getAll(SessionFactory sessFact){
        Session session = sessFact.openSession();
        Transaction tr = session.beginTransaction();

        Query query = session.createQuery("FROM ItemsInStockEntity ORDER BY date DESC");

        List<ItemsInStockEntity> datesList = query.list();

        tr.commit();
        session.close();

        return datesList;
    }


}
