package dbhelpers;

import entity.UnitsEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

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

    public static String getUnitById(SessionFactory sessFact, int unitId){
        Session session = sessFact.openSession();
        Transaction tr = session.beginTransaction();

        Query query = session.createQuery("FROM UnitsEntity WHERE id =:unitId");
        query.setParameter("unitId", unitId);

        List<UnitsEntity> unitsList = query.list();

        tr.commit();
        session.close();

        if(unitsList.size() > 0)
            return unitsList.get(0).getUnit();

        return null;
    }
}
