package dbhelpers;

import entity.ItemsEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class ItemsDBHelper extends AbstractDBHelper {

    public static List<ItemsEntity> getItemsEntitiesList(SessionFactory sessFact){
        Session session = sessFact.openSession();
        Transaction tr = session.beginTransaction();

        List<ItemsEntity> itemsList = session.createQuery("FROM ItemsEntity ").list();

        tr.commit();
        session.close();

        return itemsList;
    }

    public static ItemsEntity getItemsEntityById(SessionFactory sessFact, int id){
        Session session = sessFact.openSession();
        Transaction tr = session.beginTransaction();

        Query query = session.createQuery("FROM ItemsEntity WHERE id =:id");
        query.setParameter("id", id);
        List<ItemsEntity> itemsList = query.list();

        tr.commit();
        session.close();

        if(itemsList.size() > 0)
            return itemsList.get(0);

        return null;
    }
}
