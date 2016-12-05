package ua.edu.nau.dao.impl;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import ua.edu.nau.dao.TestSessionDAO;
import ua.edu.nau.hibernate.HibernateUtil;
import ua.edu.nau.model.TestSession;

import java.util.ArrayList;

public class TestSessionDAOImpl extends BasicDAOImpl<TestSession> implements TestSessionDAO {
    @Override
    public ArrayList<TestSession> getAll() {
        return null;
    }

    @Override
    public TestSession get(Integer id) {
        return null;
    }

    @Override
    public TestSession getById(Integer id) {
        Session session = HibernateUtil.getSession();

        session.beginTransaction();
        TestSession testSession = ((TestSession) session.get(TestSession.class, id));
        session.refresh(testSession);
        session.getTransaction().commit();

        return testSession;
    }

   /* @Override
    public Integer insert(TestSession model) {
        Session session = HibernateUtil.getSession();

        Integer id;

        session.beginTransaction();
        id = ((Integer) session.save(model));
        session.getTransaction().commit();

        return id;
    }*/

    @Override
    @SuppressWarnings("unchecked")
    public ArrayList<TestSession> getUserSessions(Integer userId) {
        Session session = HibernateUtil.getSession();

        session.beginTransaction();

        ArrayList<TestSession> testSessions = new ArrayList<TestSession>(session.createCriteria(TestSession.class)
                .add(Restrictions.eqProperty("user_id", String.valueOf(userId)))
                .list());

        testSessions.forEach(session::refresh);

        session.getTransaction().commit();

        return testSessions;
    }

    @Override
    public void update(TestSession model) {
        Session session = HibernateUtil.getSession();

        session.beginTransaction();
        session.update(model);
        session.getTransaction().commit();
    }

    @Override
    public void delete(TestSession model) {

    }
}