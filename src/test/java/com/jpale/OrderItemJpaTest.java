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

import java.text.SimpleDateFormat;
import java.text.DateFormat;

public class OrderItemJpaTest {
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
            String deleteAllOrderItem = "Delete From OrderItem";
            String alterOrder = "alter table orders auto_increment = 1";
            String alterItem = "alter table Item auto_increment = 1";
            String alterBoard = "alter table Board auto_increment = 1";


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
            query = em.createQuery(deleteAllOrderItem);
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
    public void insertOrderItem() {
        
        DateFormat format = new SimpleDateFormat("yyyyMMdd");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        Member member = new Member("1", "테스트1", 1, "패스워드1");
        Team team = new Team("1111", "팀1");

        Date today = new Date();

        String todayStr = format.format(today);

        logger.info(todayStr);

        try {
            tx.begin();

            member.setTeam(team);

            em.persist(team);
            em.persist(member);
            
            Item item = new Item();
            item.setItemName("아이템1");
            item.setPrice(5000);
            item.setStockQt(100);
            
            em.persist(item);

            assertThat(format.format(item.getRegistDate()), is(todayStr));

            Order order = new Order();
            order.setOrderMember(member);
            
            assertThat(order.getOrderItemList(), is(not(nullValue())));

            em.persist(order);

            OrderItem orderItem = new OrderItem(order, item);
            orderItem.setOrderCount(1);
            orderItem.setOrderPrice(10000);

            em.persist(orderItem);

            Item item2 = new Item();
            item2.setItemName("아이템2");
            item2.setPrice(12000);
            item2.setStockQt(200);
            
            em.persist(item2);

            OrderItem orderItem2 = new OrderItem(order, item2);
            orderItem2.setOrderCount(2);
            orderItem2.setOrderPrice(20000);

            em.persist(orderItem2);

            List<Order> orderList = member.getOrderList();

            orderList.stream()
                         .forEach(orderIn -> {
                             logger.info(orderIn.getOrderMember().getUserId() + ", " + format.format(orderIn.getOrderDate()) + " : " + orderIn.getOrderStatus().toString());
                             List<OrderItem> orderItemList = orderIn.getOrderItemList();
                             logger.info(orderItemList.size() + "");
                         });
            
            orderItem2.setOrder(null);
            orderItem2.setItem(null);

            em.remove(orderItem2);
            
            List<OrderItem> orderItemList2 = order.getOrderItemList();
            logger.info(orderItemList2.size() + "");
            
        } catch(Exception e) {
            
            logger.error(e.toString());
        } finally {
            tx.rollback();
            em.close();
        }
    }
}