package ua.edu.nau.dao.impl;

import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import ua.edu.nau.dao.GroupDAO;
import ua.edu.nau.hibernate.HibernateUtil;
import ua.edu.nau.model.UniversityStructure.Group;

import java.util.*;

public class GroupDAOImpl extends BasicDAOImpl<Group> implements GroupDAO {
    @Override
    public Group getById(Integer id) {
        Session session = HibernateUtil.getSession();

        session.beginTransaction();
        Group group = ((Group) session.get(Group.class, id));
        session.refresh(group);
        session.getTransaction().commit();

        return group;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ArrayList<Group> getAll() {
        Session session = HibernateUtil.getSession();
        ArrayList<Group> groups;

        session.beginTransaction();
        List<Group> groupSet = session.createCriteria(Group.class)
                .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
                .addOrder(Order.asc("name"))
                .list();

        groups = new ArrayList<Group>(groupSet);

        session.getTransaction().commit();

        return groups;
    }
}
