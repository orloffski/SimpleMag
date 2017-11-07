package dbhelpers;

import entity.CounterpartiesEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class CounterpartiesDBHelper extends AbstractDBHelper{
    public static List<CounterpartiesEntity> getCounterpartiesEntitiesList(SessionFactory sessFact){
        Session session = sessFact.openSession();
        Transaction tr = session.beginTransaction();

        List<CounterpartiesEntity> counterpartiesList = session.createQuery("FROM CounterpartiesEntity ").list();

        tr.commit();
        session.close();

        return counterpartiesList;
    }
}
