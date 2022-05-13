package com.dmdev;

import com.dmdev.entity.Payment;
import com.dmdev.util.HibernateUtil;
import com.dmdev.util.TestDataImporter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class HibernateCallBack {

    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory
//                     .withOptions()
//                     .interceptor(new GlobalInterceptor())
                     .openSession()) {
            TestDataImporter.importData(sessionFactory);
            session.beginTransaction();

            var payment = session.find(Payment.class, 1L);
            payment.setAmount(payment.getAmount() + 10);

            session.getTransaction().commit();
        }
    }
}
