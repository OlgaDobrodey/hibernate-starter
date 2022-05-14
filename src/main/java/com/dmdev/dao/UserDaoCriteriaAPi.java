package com.dmdev.dao;

import com.dmdev.entity.Payment;
import com.dmdev.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDaoCriteriaAPi {

    private static final UserDaoCriteriaAPi INSTANCE = new UserDaoCriteriaAPi();

    /**
     * All users
     */
    public List<User> findAll(Session session) {
        var criteriaBuilder = session.getCriteriaBuilder();
        var criteria = criteriaBuilder.createQuery(User.class);
        var user = criteria.from(User.class);
        criteria.select(user);

        return session.createQuery(criteria).getResultList();
    }

    /**
     * all users with same name
     */
    public List<User> findAllByFirstName(Session session, String firstName) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(User.class);
        var user = criteria.from(User.class);

        criteria.select(user).where(
                cb.equal(user.get("personalInfo").get("firstname"), firstName));
//        criteria.select(user).where(
//                cb.equal(user.get(User_.personalInfo).get(PersonalInfo_.firstname), firstName));

        return session.createQuery(criteria).getResultList();
    }

    /**
     * all users {limit}, sort for date asd
     */
    public List<User> findLimitedUsersOrderedByBirthday(Session session, int limit) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(User.class);
        var user = criteria.from(User.class);

//        criteria.select(user).orderBy(cb.asc(user.get(User_.personalInfo)
//                .get(PersonalInfo_.birthDate)));

        return session.createQuery(criteria)
                .setMaxResults(limit)
                .getResultList();
    }


    /**
     * all users for same name company
     */
    public List<User> findAllByCompanyName(Session session, String companyName) {
        return session.createQuery("select u from User u where u.company.name =:company", User.class)
                .setParameter("company", companyName)
                .getResultList();
    }

    /**
     * all payment for users with same name,
     * sort by name user and amount
     */
    public List<Payment> findAllPaymentsByCompanyName(Session session, String companyName) {
        return session.createQuery("select p from Payment p " +
                        "join p.receiver u " +
                        "join u.company c " +
                        "where c.name = :companyName " +
                        "order by u.personalInfo.firstname, p.amount", Payment.class)
                .setParameter("companyName", companyName)
                .list();
    }

    /**
     * Returns the average salary of an employee with the given first and last name
     */
    public Optional<Double> findAveragePaymentAmountByFirstAndLastNames(Session session, String firstName, String lastName) {
        return session.createQuery("select avg(p.amount) from Payment p " +
                        "join p.receiver u " +
                        "where u.personalInfo.firstname = :firstName " +
                        "   and u.personalInfo.lastname = :lastName", Double.class)
                .setParameter("firstName", firstName)
                .setParameter("lastName", lastName)
                .uniqueResultOptional();
//        session.createQuery("select avg(p.amount) from Payment p " +
//                        "where p.receiver.personalInfo.firstname = :firstName and " +
//                        "p.receiver.personalInfo.lastname = :lastName", Double.class)
//                .setParameter("firstName",firstName)
//                .setParameter("lastName", lastName)
//                .uniqueResultOptional();
    }

    /**
     * Returns for each company: name, average salary of all its employees. Companies are ordered by name.
     */
    public List<Object[]> findCompanyNamesWithAvgUserPaymentsOrderedByCompanyName(Session session) {
        return session.createQuery("select c.name, avg(p.amount) from Company c " +
                        "join c.users u " +
                        "join u.payments p " +
                        "group by c.name " +
                        "order by c.name", Object[].class)
                .list();
    }

    /**
     * Returns a list: employee (User object), average payout, but only for those employees whose average payout is
     * more than the average salary of all employees
     * Sort by employee name
     */
    public List<Object[]> isItPossible(Session session) {

        return session.createQuery("select u, avg(p.amount) from User u " +
                        "join u.payments p " +
                        "group by u " +
                        "having avg(p.amount) > (select avg(pa.amount) from Payment pa) " +
                        "order by u.personalInfo.firstname", Object[].class)
                .list();
    }

    public static UserDaoCriteriaAPi getInstance() {
        return INSTANCE;
    }
}
