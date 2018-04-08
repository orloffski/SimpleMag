package dbhelpers;

import entity.CounterpartiesEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

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

    public static CounterpartiesEntity getCounterpartyBiId(SessionFactory sessFact, int id){
        Session session = sessFact.openSession();
        Transaction tr = session.beginTransaction();

        Query query = session.createQuery("FROM CounterpartiesEntity " +
                "WHERE id =:id");
        query.setParameter("id", id);

        List<CounterpartiesEntity> counterpartiesList = query.list();

        tr.commit();
        session.close();

        if(counterpartiesList.size() > 0)
            return counterpartiesList.get(0);

        return null;
    }
}
