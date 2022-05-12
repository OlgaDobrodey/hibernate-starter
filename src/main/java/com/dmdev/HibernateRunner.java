package com.dmdev;

import com.dmdev.entity.User;
import com.dmdev.util.HibernateUtil;
import com.dmdev.util.TestDataImporter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

@Slf4j
public class HibernateRunner {

    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
//            TestDataImporter.importData(sessionFactory);
            session.beginTransaction();
            session.enableFetchProfile("withCompanyAndPayment");

//            var user = session.get(User.class, 1L);
//            System.out.println(user.getPayments().size());
//            System.out.println(user.getCompany().getName());
//            var users = session.createQuery("select u from User u", User.class)
//                    .list();
//            users.forEach(user -> System.out.println(user.getPayments().size()));
//            var users = session.createQuery(
//                            "select u from User u " +
//                                    "join fetch u.payments " +
//                                    "join fetch u.company " +
//                                    "where 1 = 1", User.class)
//                    .list();

            var users = session.createQuery(
                            "select u from User u " +
                                    "where 1 = 1", User.class)
                    .list();
            users.forEach(user -> System.out.println(user.getPayments().size()));
            users.forEach(user -> System.out.println(user.getCompany().getName()));

            session.getTransaction().commit();
        }
    }
}
