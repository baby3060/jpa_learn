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

public class IdentifyTest1 {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static EntityManagerFactory emf;

    @BeforeClass
    public static void setup() {
        emf = ManagerFactoryMaker.getFactoryInstance();
    }

    @Before
    public void initData() {
        deleteAll();
    }

    private void deleteAll() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            em.createNativeQuery("Delete From CHILDIDC").executeUpdate();
            em.createNativeQuery("Delete From PARENTIDC").executeUpdate();

            tx.commit();
        } catch(Exception e) {
            tx.rollback();
            logger.error(e.toString());
        } finally {
            em.close();
        }
    }

    @Test
    public void insertTest() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            ParentIdEntity parent = new ParentIdEntity();
            parent.setId1("id1");
            parent.setId2("id2");
            parent.setName("이름");

            em.persist(parent);

            Long count = em.createQuery("Select Count(i) From ParentIdEntity i Where i.id1 = :id1 And i.id2 = :id2", Long.class)
                           .setParameter("id1", "id1")
                           .setParameter("id2", "id2")
                           .getSingleResult();

            assertThat(count, is(1L));

            count = em.createQuery("Select Count(i) From ParentIdEntity i Where i.id1 = :id1 And i.id2 = :id2", Long.class)
                           .setParameter("id1", "idnoexist")
                           .setParameter("id2", "idnoexist")
                           .getSingleResult();

            assertThat(count, is(0L));

            ParentId parentId = new ParentId("id1", "id2");
            ParentIdEntity parent2 = em.find(ParentIdEntity.class, parentId);

            assertThat(parent, equalTo(parent2));

        } finally {
            tx.rollback();
            em.close();
        }
    }

    @Test
    public void insertChildTest() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            ParentIdEntity parent = new ParentIdEntity();
            parent.setId1("id1");
            parent.setId2("id2");
            parent.setName("이름");

            em.persist(parent);

            Long count = em.createQuery("Select Count(i) From ParentIdEntity i Where i.id1 = :id1 And i.id2 = :id2", Long.class)
                           .setParameter("id1", parent.getId1())
                           .setParameter("id2", parent.getId2())
                           .getSingleResult();

            assertThat(count, is(1L));

            ChildIdEntity child = new ChildIdEntity("1", parent);
            em.persist(child);

            ParentIdEntity parent2 = new ParentIdEntity();
            parent2.setId1("id2");
            parent2.setId2("id2");
            parent2.setName("이름2");

            ChildIdEntity child2 = new ChildIdEntity("2", parent2);
            // java.lang.IllegalStateException: org.hibernate.TransientPropertyValueException: object references an unsaved transient instance 발생
            // 테스트가 중지되지는 않음
            em.persist(child2);

            count = em.createQuery("Select Count(c) From ChildIdEntity c Where c.id = :id", Long.class)
                           .setParameter("id", child2.getId())
                           .getSingleResult();

            assertThat(count, is(0L));

            count = em.createQuery("Select Count(p) From ParentIdEntity p Where p.id1 = :id1 And p.id2 = :id2", Long.class)
                            .setParameter("id1", parent2.getId1())
                            .setParameter("id2", parent2.getId2())
                           .getSingleResult();

            assertThat(count, is(0L));

        } catch(Exception e) {
            logger.error(e.toString());
        } finally {
            tx.rollback();
            em.close();
        }
    }
}