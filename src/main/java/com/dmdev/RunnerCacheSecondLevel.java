package com.dmdev;

import com.dmdev.entity.User;
import com.dmdev.util.HibernateUtil;
import com.dmdev.util.TestDataImporter;
import org.hibernate.SessionFactory;

import javax.transaction.Transactional;
import java.sql.SQLException;

public class RunnerCacheSecondLevel {

    @Transactional
    public static void main(String[] args) throws SQLException {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            TestDataImporter.importData(sessionFactory);
            User user = null;
            try (var session = sessionFactory.openSession()) {
                session.beginTransaction();

                user = session.find(User.class, 1L);
                user.getCompany().getName();
                user.getUserChats().size();
                var user1 = session.find(User.class, 1L);

                System.out.println(sessionFactory.getStatistics().getCacheRegionStatistics("Users"));
                session.getTransaction().commit();
            }
            try (var session = sessionFactory.openSession()) {
                session.beginTransaction();

                var user2 = session.find(User.class, 1L);
                user2.getCompany().getName();
                user2.getUserChats().size();
                System.out.println(sessionFactory.getStatistics().getCacheRegionStatistics("Users"));
                session.getTransaction().commit();
            }
        }
    }
}
