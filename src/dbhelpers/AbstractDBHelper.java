package dbhelpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public abstract class AbstractDBHelper {

    public static void saveEntity(SessionFactory sessFact, Object item){
        Session session = sessFact.openSession();
        Transaction tr = session.beginTransaction();

        session.save(item);

        tr.commit();
        session.close();
    }

    public static void updateEntity(SessionFactory sessFact, Object item){
        Session session = sessFact.openSession();
        Transaction tr = session.beginTransaction();

        session.update(item);

        tr.commit();
        session.close();
    }

    public static void deleteEntity(SessionFactory sessFact, Object item){
        Session session = sessFact.openSession();
        Transaction tr = session.beginTransaction();

        session.delete(item);

        tr.commit();
        session.close();
    }

    public static void saveOrUpdate(SessionFactory sessFact, Object item){
        Session session = sessFact.openSession();
        Transaction tr = session.beginTransaction();

        session.saveOrUpdate(item);

        tr.commit();
        session.close();
    }
}
