package com.jpale;

import java.math.BigInteger;

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

public class MemberJpaTest {
    private static EntityManagerFactory emf;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @BeforeClass
    public static void setup() {
        emf = ManagerFactoryMaker.getFactoryInstance();
    }

    @Before
    public void init() {
        deleteAll();
    }

    @After
    public void close() {
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
            String alterOrderItem = "alter table OrderItem auto_increment = 1";

            Query query = em.createQuery(deleteAllOrderItem);
            query.executeUpdate();

            query = em.createQuery(deleteAllOrder);
            query.executeUpdate();

            query = em.createQuery(deleteAllItem);
            query.executeUpdate();

            query = em.createQuery(deleteAllBoard);
            query.executeUpdate();

            query = em.createQuery(deleteAllMember);
            query.executeUpdate();

            query = em.createQuery(deleteAllTeam);
            query.executeUpdate();

            query = em.createNativeQuery(alterOrder);
            query.executeUpdate();

            query = em.createNativeQuery(alterItem);
            query.executeUpdate();

            query = em.createNativeQuery(alterBoard);
            query.executeUpdate();

            query = em.createNativeQuery(alterOrderItem);
            query.executeUpdate();

            tx.commit();
        } catch(Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    public void updateTest() {
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

            Member m1 = em.find(Member.class, "1");
            Member m2 = em.find(Member.class, "2");
            Member m3 = em.find(Member.class, "3");

            assertMember(member1, m1);
            assertMember(member2, m2);
            assertMember(member3, m3);

            m1.setUserName("테스트1(수정)");

            em.flush();

            Member mUpdate1 = em.find(Member.class, "1");

            assertThat(mUpdate1.getUserName(), is(m1.getUserName()));
            // member1의 값은 바꾸지 않았는데 member1도 '테스트1(수정)'로 바뀌었다(캐시).
            // Fail
            // assertThat(member1.getUserName(), is(not(mUpdate1.getUserName())));
            assertThat(member1.getUserName(), is(mUpdate1.getUserName()));

            member1.setUserName("테스트1");

            // find로 불러온 데이터가 아니어도 해당 데이터를 수정하면, find로 불러온 데이터가 수정됨
            assertThat(member1.getUserName(), is(mUpdate1.getUserName()));
        } finally {
            tx.rollback();
            em.close();
        }
    }

    @Test
    public void deleteTest() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        Member member1 = new Member("1", "테스트1", 1, "패스워드1");
        Member member2 = new Member("2", "테스트2", 2, "패스워드2");
        Member member3 = new Member("3", "테스트3", 3, "패스워드3");

        Address address = new Address("시", "길", "우편번호");

        try {
            tx.begin();

            member1.setAddress(address);

            em.persist(member1);
            em.persist(member2);
            em.persist(member3);

            em.flush();

            Member m1 = em.find(Member.class, "1");
            Member m2 = em.find(Member.class, "2");
            Member m3 = em.find(Member.class, "3");

            assertMember(member1, m1);
            assertMember(member2, m2);
            assertMember(member3, m3);

            TypedQuery<Long> query = em.createQuery("SELECT COUNT(m) FROM Member m Where m.id = :id", Long.class);

            query.setParameter("id", "1");

            long count = query.getSingleResult();

            assertThat(count, is(1L));

            List<Board> boardList = em.createQuery("Select b From Board b Where b.member.userId = :userId").setParameter("userId", m1.getUserId()).getResultList();

            if( boardList.size() > 0 ) {
                for(Board board : boardList) {
                    em.remove(board);
                }
            }

            em.remove(m1);

            count = query.getSingleResult();

            assertThat(count, is(0L));
        } finally {
            tx.rollback();
            em.close();
        }
    }

    @Test
    public void countTest() {
        EntityManager em = emf.createEntityManager();

        TypedQuery<Long> query = em.createQuery("SELECT COUNT(m) FROM Member m", Long.class);
        long count = query.getSingleResult();
        
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

    @Test
    public void collectionTest() {
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();

        Member member = new Member("1", "테스트1", 1, "패스워드1");
        
        try {
            tx.begin();

            member.getInterestSubject().add("Shopping");
            member.getInterestSubject().add("Webtoon");

            em.persist(member);

            Query query = em.createNativeQuery("Select Count(*) As cnt From interest_subject Where member_id = :member_id").setParameter("member_id", "1");

            Long count = ((BigInteger)query.getSingleResult()).longValue();
            assertThat(count, is(2L));
            
            assertThat(member.getInterestSubject().size(), is(2));

            member.getInterestSubject().remove("Webtoon");

            assertThat(member.getInterestSubject().size(), is(1));
            count = ((BigInteger)query.getSingleResult()).longValue();
            assertThat(count, is(1L));

            member.getInterestSubject().add("Catoon");

            count = ((BigInteger)query.getSingleResult()).longValue();
            assertThat(count, is(2L));

        } catch(Exception e) {
            logger.error("Err Msg : " + e.toString());
        } finally {
            tx.rollback();
            em.close();
        }
    }
    
    @Test
    public void pagingTest() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            for( int i = 1; i < 14; i++ ) {
                Member member = new Member(String.valueOf(i), "테스트" + i, i, "패스워드" + i);
                em.persist(member);
            }

            TypedQuery<Long> query = em.createQuery("Select Count(m) From Member m", Long.class);
            long count = query.getSingleResult();

            assertThat(count, is(13L));

            // userId가 문자열이므로, 일반적으로 Order By 할 시 원하는 결과가 안 나옴
            TypedQuery<Member> listQuery = em.createQuery("Select m From Member m Order By m.userId * 1", Member.class);
            listQuery.setFirstResult(0);
            listQuery.setMaxResults(10);

            List<Member> resultList = listQuery.getResultList();

            assertThat(resultList.size(), is(10));
            
            for( int i = 0; i < resultList.size(); i++ ) {
                assertThat(resultList.get(i).getUserId(), is(String.valueOf((i + 1))));
            }
            
            listQuery.setFirstResult(10);
            resultList = listQuery.getResultList();
            assertThat(resultList.size(), is(3));
            for( int i = 0; i < resultList.size(); i++ ) {
                assertThat(resultList.get(i).getUserId(), is(String.valueOf((10 + i + 1))));
            }

        } catch(Exception e) {
            logger.error("Err Msg : " + e.toString());
        } finally {
            tx.rollback();
            em.close();
        } 
    }

    
}