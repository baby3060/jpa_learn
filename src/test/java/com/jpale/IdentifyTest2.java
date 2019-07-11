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

public class IdentifyTest2 {
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

            em.createQuery("Delete From GrandChildIdent").executeUpdate();
            em.createQuery("Delete From ChildItent").executeUpdate();
            em.createQuery("Delete From ParentIdent").executeUpdate();
            em.createQuery("Delete From GrandChildEmbedIdentify").executeUpdate();
            em.createQuery("Delete From ChildEmbedIdentify").executeUpdate();
            em.createQuery("Delete From ParentEmbedIdetify").executeUpdate();

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
        ParentIdent parent = new ParentIdent("1", "parent");
        ChildItent child = new ChildItent(parent, "1_1", "child");
        GrandChildIdent grandChild = new GrandChildIdent(child, "1_1_1", "grandchild");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            em.persist(parent);
            em.persist(child);
            em.persist(grandChild);
            
            ChildIdentId childId = new ChildIdentId(parent.getId(), child.getChildId());
            
            em.flush();

            ChildItent childGet = em.find(ChildItent.class, childId);

            assertThat(child, equalTo(childGet));

            GrandChildIdentId grandChildId = new GrandChildIdentId(childId, grandChild.getId());

            GrandChildIdent grandChildGet = em.find(GrandChildIdent.class, grandChildId);

            assertThat(grandChild, equalTo(grandChildGet));
            assertThat(grandChildGet.getChild(), equalTo(child));
            assertThat(grandChildGet.getChild().getParent(), equalTo(parent));
            assertThat(grandChildGet.getChild().getParent().getId(), is(parent.getId()));
            assertThat(grandChildGet.getChild().getParent().getId(), not(is("3")));
        } catch(Exception e) {
            logger.error(e.toString());
        } finally {
            tx.rollback();
            em.close();
        }
    }

    @Test
    public void embedIdentiTest() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        ParentEmbedIdetify parent = new ParentEmbedIdetify("1", "parent");
        ChildEmbedIdentify child = new ChildEmbedIdentify();
        ChildEmbeddedId childId = new ChildEmbeddedId(parent.getId(), "1_1");
        child.setId(childId);
        child.setName("child");

        GrandChildEmbeddedId grandChildId = new GrandChildEmbeddedId();
        grandChildId.setChildId(childId);
        grandChildId.setId("1_1_1");
        
        GrandChildEmbedIdentify grandChild = new GrandChildEmbedIdentify();
        grandChild.setId(grandChildId);
        grandChild.setName("grandchild");

        try {
            tx.begin();

            em.persist(parent);
            em.persist(child);
            em.persist(grandChild);

            ChildEmbedIdentify getChild = em.find(ChildEmbedIdentify.class, childId);
            GrandChildEmbedIdentify getGrandChild = em.find(GrandChildEmbedIdentify.class, grandChildId);

            assertThat(getChild.getParent(), equalTo(parent));
            assertThat(getGrandChild.getChild(), equalTo(getChild));

        } catch(Exception e) {
            logger.error(e.toString());
        } finally {
            tx.rollback();
            em.close();
        }
    }

}