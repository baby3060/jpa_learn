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

    @Test
    public void groupingTest() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        
        try {

            tx.begin();

            Team teamA = new Team("A", "Team A");
            Team teamB = new Team("B", "Team B");

            em.persist(teamA);
            em.persist(teamB);

            Member member1 = new Member("1", "테스트1", 1, "패스워드1");
            Member member2 = new Member("2", "테스트2", 2, "패스워드2");
            Member member3 = new Member("3", "테스트3", 3, "패스워드3");
            Member member4 = new Member("4", "테스트4", 4, "패스워드4");
            Member member5 = new Member("5", "테스트5", 5, "패스워드5");
            Member member6 = new Member("6", "테스트6", 6, "패스워드6");
            Member member7 = new Member("7", "테스트7", 7, "패스워드7");
            Member member8 = new Member("8", "테스트8", 8, "패스워드8");
            Member member9 = new Member("9", "테스트9", 9, "패스워드9");

            
            member1.setTeam(teamA);
            member4.setTeam(teamA);
            member7.setTeam(teamA);
            member8.setTeam(teamA);
            
            member2.setTeam(teamB);
            member3.setTeam(teamB);
            member5.setTeam(teamB);
            member6.setTeam(teamB);
            member9.setTeam(teamB);
            

            em.persist(member1);
            em.persist(member2);
            em.persist(member3);
            em.persist(member4);
            em.persist(member5);
            em.persist(member6);
            em.persist(member7);
            em.persist(member8);
            em.persist(member9);


            TypedQuery<Long> queryCount = em.createQuery("Select Count(m) From Member m", Long.class);
            Long countTotal = queryCount.getSingleResult();
            
            assertThat(countTotal, is(9L));

            
            queryCount = em.createQuery("Select Count(m) From Member m Where m.team = :teamId", Long.class).setParameter("teamId", teamA);
            Long countA = queryCount.getSingleResult();

            queryCount = em.createQuery("Select Count(m) From Member m Where m.team = :teamId", Long.class).setParameter("teamId", teamB);
            Long countB = queryCount.getSingleResult();

            assertThat(countTotal, is(countA + countB));
            assertThat(countA, is(4L));
            assertThat(countB, is(5L));
            
            TypedQuery<Long> querySumAge = em.createQuery("Select Sum(m.userAge) From Member m", Long.class);

            Long sumAge = querySumAge.getSingleResult();

            List<Object[]> sumList = em.createQuery("Select m.team, Sum(m.userAge) as age, Max(m.userAge) as mage, Min(m.userAge) as miage From Member m Group By m.team Order By m.team").getResultList();

            assertThat(sumAge, is(45L));
            assertThat(sumList.size(), is(2));
            // Sum
            assertThat((Long)((sumList.get(0))[1]), is(20L));
            assertThat((Long)((sumList.get(1))[1]), is(25L));

            // Max
            assertThat((Long)((sumList.get(0))[2]), is(8));
            assertThat((Long)((sumList.get(1))[2]), is(9));

            // Min
            assertThat((Long)((sumList.get(0))[3]), is(1));
            assertThat((Long)((sumList.get(1))[3]), is(2));

        } catch(Exception e) {
            logger.error("Err Msg : " + e.toString());
        } finally {
            tx.rollback();
            em.close();
        } 
    }

    @Test
    public void joinTest() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Member member1 = new Member("1", "테스트1", 1, "패스워드1");
            Member member2 = new Member("2", "테스트2", 2, "패스워드2");
            Member member3 = new Member("3", "테스트3", 3, "패스워드3");

            Team team = new Team("1111", "팀1");
            Team team2 = new Team("2222", "팀2");

            em.persist(team);
            em.persist(team2);

            member1.setTeam(team);
            member2.setTeam(team2);
            member3.setTeam(team);

            em.persist(member1);
            em.persist(member2);
            em.persist(member3);

            // Entity로 m만 호출하였음. Team은 호출 안 함
            String query = "Select m From Member m join fetch m.team Order By m.userAge";

            List<Member> memberList = em.createQuery(query, Member.class).getResultList();

            for(Member member : memberList) {
                assertThat(member.getTeam(), is(not(nullValue())));
            }

            query = "Select t From Team t join fetch t.memberList Where t.name = '팀1' ";
            
            List<Team> teamList = em.createQuery(query, Team.class).getResultList();

            // 멤버가 2이니까 2
            assertThat(teamList.size(), is(2));

            query = "Select distinct t From Team t join fetch t.memberList Where t.name = '팀1' ";

            // Team 하나만 호출해왔음
            teamList = em.createQuery(query, Team.class).getResultList();

            assertThat(teamList.size(), is(1));
        } catch(Exception e) {
            logger.error("Err Msg : " + e.toString());
        } finally {
            tx.rollback();
            em.close();
        } 
    }

    
}   