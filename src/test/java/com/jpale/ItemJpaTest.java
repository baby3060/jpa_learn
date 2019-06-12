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

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.*;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class ItemJpaTest {
    private final Logger logger = LoggerFactory.getLogger(ItemJpaTest.class);

    private static EntityManagerFactory emf;

    @BeforeClass
    public static void setup() {
        emf = ManagerFactoryMaker.getFactoryInstance();
    }

    @Before
    public void init() {
        deleteAll();
    }

    private void deleteAll() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        String deleteAllMember = "Delete From Member";
        String deleteAllItem = "Delete From Item";
        String deleteAllCate = "Delete From ItemCategory";

        try {
            tx.begin();

            Query query = em.createQuery(deleteAllItem);

            query.executeUpdate();

            query = em.createQuery(deleteAllMember);

            query.executeUpdate();

            query = em.createQuery(deleteAllCate);

            query.executeUpdate();

            em.createNativeQuery("ALTER TABLE ITEM AUTO_INCREMENT = 1").executeUpdate();

            tx.commit();
        } catch(Exception e) {
            tx.rollback();
            logger.error(e.toString());
        } finally {
            em.close();
        }
    }

    @Test
    public void itemInsertTest() {
        DateFormat format = new SimpleDateFormat("yyyyMMdd");

        Date today = new Date();

        String todayStr = format.format(today);

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Item item = new Item();
            item.setItemName("아이템1");
            item.setPrice(5000);
            item.setStockQt(100);

            em.persist(item);
            em.flush();

            long itemId = item.getItemId();

            assertThat(itemId, is(1L));

            assertThat(format.format(item.getRegistDate()), is(todayStr));

        } finally {
            tx.rollback();
            em.close();
        }
    }

    @Test
    public void insertCategory() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            ItemCategory iCategory = new ItemCategory();

            iCategory.setCategory(Category.ALBUM);

            em.persist(iCategory);

            assertThat(iCategory.getCategory().getCategory(), is(Category.ALBUM.getCategory()));

        } catch(Exception e) {
            logger.error(e.toString());
        } finally {
            tx.rollback();
            em.close();
        }
    }

    @Test
    public void itemCategorySet() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            ItemCategory iCategory = new ItemCategory();

            iCategory.setCategory(Category.ETC);

            em.persist(iCategory);

            Item item = new Item();
            item.setItemName("아이템1");
            item.setPrice(5000);
            item.setStockQt(100);
            item.setCategory(iCategory);

            em.persist(item);

            item = new Item();
            item.setItemName("아이템2");
            item.setPrice(1000);
            item.setStockQt(50);
            item.setCategory(iCategory);

            em.persist(item);

            ItemCategory findCategory = em.find(ItemCategory.class, Category.ETC);

            List<Item> etcItem = findCategory.getItemList();

            assertThat(etcItem.size(), is(2));

            etcItem.stream()
                .forEach(item2 -> {
                    logger.info(item2.getItemId() + "] " + item2.getItemName());
                });

        } catch(Exception e) {
            logger.error(e.toString());
        } finally {
            tx.rollback();
            em.close();
        }
    }
}