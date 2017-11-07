package dbhelpers;

import entity.UnitsEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class UnitsDBHelper {

    public static List<UnitsEntity> getUnitsEntitiesList(SessionFactory sessFact){
        Session session = sessFact.openSession();
        Transaction tr = session.beginTransaction();

        List<UnitsEntity> unitsList = session.createQuery("FROM UnitsEntity").list();

        tr.commit();
        session.close();

        return unitsList;
    }

    public static void saveUnit(SessionFactory sessFact, UnitsEntity item){
        Session session = sessFact.openSession();
        Transaction tr = session.beginTransaction();

        session.save(item);

        tr.commit();
        session.close();
    }

    public static void updateUnit(SessionFactory sessFact, UnitsEntity item){
        Session session = sessFact.openSession();
        Transaction tr = session.beginTransaction();

        session.update(item);

        tr.commit();
        session.close();
    }

    public static void deleteUnit(SessionFactory sessFact, UnitsEntity item){
        Session session = sessFact.openSession();
        Transaction tr = session.beginTransaction();

        session.delete(item);

        tr.commit();
        session.close();
    }
}
