package com.jpale;

import org.junit.Test;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import com.jpale.entity.*;
import com.jpale.common.*;

import javax.persistence.*;

import java.util.*;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class OrderJpaTest {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private static EntityManagerFactory emf;

    @BeforeClass
    public static void setUp() {
        emf = ManagerFactoryMaker.getFactoryInstance();
    }

    @Before
    public void init() {
        deleteAll();
    }

    private void deleteAll() {
        
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String deleteAllMember = "Delete From Member";
            String deleteAllBoard = "Delete From Board";
            String deleteAllItem = "Delete From Item";
            String deleteAllOrder = "Delete From Order";
            String deleteAllTeam = "Delete From Team";
            String alterOrder = "alter table Orders auto_increment = 1";
            String alterItem = "alter table Item auto_increment=1";
            String alterBoard = "alter table Board auto_increment=1";

            Query query = em.createQuery(deleteAllMember);
            query.executeUpdate();
            query = em.createQuery(deleteAllItem);
            query.executeUpdate();
            query = em.createQuery(deleteAllTeam);
            query.executeUpdate();
            query = em.createQuery(deleteAllOrder);
            query.executeUpdate();
            query = em.createQuery(deleteAllBoard);
            query.executeUpdate();
            query = em.createNativeQuery(alterOrder);
            query.executeUpdate();
            query = em.createNativeQuery(alterItem);
            query.executeUpdate();
            query = em.createNativeQuery(alterBoard);
            query.executeUpdate();

            tx.commit();
        } catch(Exception e) {
            tx.rollback();
            logger.error(e.toString());
        } finally {
            em.close();
        }
    }

    @Test
    public void insertOrder() {
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            Member member = new Member("1", "테스트1", 1, "패스워드1");

            Team team = new Team("1111", "팀1");

            Item item = new Item();
            item.setItemName("아이템1");
            item.setPrice(5000);
            item.setStockQt(100);

            em.persist(item);
            em.persist(team);
            member.setTeam(team);
            em.persist(member);

            Order order = new Order();
            order.setOrderMember(member);

            em.persist(order);

            assertThat(order.getOrderId(), is(1L));
            assertThat(member.getOrderList().size(), is(1));

            member.setTeam(null);

            assertThat(member.getTeam(), is(nullValue()));
            assertThat(team.getMemberList().size(), is(0));

        } catch(Exception e) {
            logger.error(e.toString());
        } finally {
            tx.rollback();
            em.close();
        }
    }
}