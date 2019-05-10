package com.jpale;

import javax.persistence.Query;

import org.junit.Test;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import com.jpale.entity.*;
import com.jpale.common.*;

import javax.persistence.*;

import java.util.*;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class MemberJpaTest {
    private static EntityManagerFactory emf;

    private Logger logger = LoggerFactory.getLogger(MemberJpaTest.class);

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

        String sql = "Delete FROM Member";
        
        Query query = em.createQuery(sql);

        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            query.executeUpdate();

            tx.commit();
        } catch(Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
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
        } finally {
            tx.rollback();
            em.close();
        }
    }

    @Test
    public void listTest() {
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

            String sql = "SELECT m FROM Member m Order By m.id";
            
            Query q = em.createQuery(sql);
            
            List<Member> result = q.getResultList();

            assertThat(result.size(), is(3));

            Member m1 = em.find(Member.class, "1");
            Member m2 = em.find(Member.class, "2");
            Member m3 = em.find(Member.class, "3");

            assertMember(member1, m1);
            assertMember(member2, m2);
            assertMember(member3, m3);

        } finally {
            tx.rollback();
            em.close();
        }
    }

    private void assertMember(Member m1, Member m2) {
        assertThat(m1.getUserId(), is(m2.getUserId()));
        assertThat(m1.getUserName(), is(m2.getUserName()));
        assertThat(m1.getPassword(), is(m2.getPassword()));
    }

    @AfterClass
    public static void tearDown() {
        emf.close();
    }
}