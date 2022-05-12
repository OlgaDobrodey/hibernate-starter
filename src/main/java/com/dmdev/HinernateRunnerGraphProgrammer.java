package com.dmdev;

import com.dmdev.entity.User;
import com.dmdev.entity.UserChat;
import com.dmdev.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.graph.GraphSemantic;

import java.sql.SQLException;
import java.util.Map;

//without annotation in User.class
//@NamedEntityGraph(
//        name = "WithCompanyAndChat",
//        attributeNodes = {
//                @NamedAttributeNode("company"),
//                @NamedAttributeNode(value = "userChats", subgraph = "chats")
//        },
//        subgraphs = {
//                @NamedSubgraph(name = "chats", attributeNodes = @NamedAttributeNode("chat"))
//        }
//)
public class HinernateRunnerGraphProgrammer {
    public static void main(String[] args) throws SQLException {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            var userGraph = session.createEntityGraph(User.class);
            userGraph.addAttributeNodes("company", "userChats");
            var userChatsSubgraph = userGraph.addSubgraph("userChats", UserChat.class);
            userChatsSubgraph.addAttributeNodes("chat");

            Map<String, Object> properties =
                    Map.of(GraphSemantic.LOAD.getJpaHintName(), userGraph);

            var user = session.find(User.class, 1L, properties);
            System.out.println(user.getCompany().getName());
            System.out.println(user.getUserChats().size());

            var users = session.createQuery(
                            "select u from User u " +
                                    "where 1 = 1", User.class)
                    .setHint(GraphSemantic.LOAD.getJpaHintName(), userGraph)
                    .list();
            users.forEach(it -> System.out.println(it.getUserChats().size()));
            users.forEach(it -> System.out.println(it.getCompany().getName()));

            session.getTransaction().commit();
        }
    }
}
