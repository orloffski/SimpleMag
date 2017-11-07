package dbhelpers;

import entity.UnitsEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class UnitsDBHelper extends AbstractDBHelper{

    public static List<UnitsEntity> getUnitsEntitiesList(SessionFactory sessFact){
        Session session = sessFact.openSession();
        Transaction tr = session.beginTransaction();

        List<UnitsEntity> unitsList = session.createQuery("FROM UnitsEntity").list();

        tr.commit();
        session.close();

        return unitsList;
    }
}
