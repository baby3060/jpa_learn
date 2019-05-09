package com.jpale;

import javax.persistence.Query;

import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import com.jpale.entity.*;

import javax.persistence.*;

public class MemberJpaTest {
    private static EntityManagerFactory emf;

    @BeforeClass
    public static void setup() {
        emf = Persistence.createEntityManagerFactory("learningPersistance");
    }

    @Test
    public void countTest() {
        EntityManager em = emf.createEntityManager();

        String sql = "SELECT COUNT(m.userId) FROM Member m";
        
        Query q = em.createQuery(sql);
        
        long count = (long)q.getSingleResult();

        assertThat(count, is(0L));
        
        em.close();
    }

    @Test
    public void insertTest() {

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();

        Member member1 = new Member("1", "테스트1", 1, "패스워드1");
        Member member2 = new Member("2", "테스트2", 2, "패스워드2");
        Member member3 = new Member("3", "테스트3", 3, "패스워드3");
        
        try {
            tx.begin();

            em.persist(member1);
            em.persist(member2);
            em.persist(member3);

            em.flush();

            String sql = "SELECT COUNT(m.userId) FROM Member m";
            
            Query q = em.createQuery(sql);
            
            long count = (long)q.getSingleResult();

            assertThat(count, is(3L));

            tx.commit();
        } catch(Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
    }

    @AfterClass
    public static void tearDown() {
        emf.close();
    }
}