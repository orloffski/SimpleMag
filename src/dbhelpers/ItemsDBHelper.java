package dbhelpers;

import entity.ItemsEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class ItemsDBHelper extends AbstractDBHelper {

    public static List<ItemsEntity> getUnitsEntitiesList(SessionFactory sessFact){
        Session session = sessFact.openSession();
        Transaction tr = session.beginTransaction();

        List<ItemsEntity> unitsList = session.createQuery("FROM ItemsEntity ").list();

        tr.commit();
        session.close();

        return unitsList;
    }
}
