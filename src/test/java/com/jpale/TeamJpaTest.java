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

public class TeamJpaTest {
    private static EntityManagerFactory emf;

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static List<Member> memberList;

    @BeforeClass
    public static void setup() {
        Random random = new Random();

        emf = ManagerFactoryMaker.getFactoryInstance();

        memberList = new ArrayList<Member>(
            Arrays.asList(
                new Member("1", "테스트1", (random.nextInt(50) + 15), "패스워드1"),
                new Member("2", "테스트2", (random.nextInt(50) + 15), "패스워드2"),
                new Member("3", "테스트3", (random.nextInt(50) + 15), "패스워드3"),
                new Member("4", "테스트4", (random.nextInt(50) + 15), "패스워드4"),
                new Member("5", "테스트5", (random.nextInt(50) + 15), "패스워드5")
            )
        );
    }

    @Before
    public void initMember() {

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            memberList
                .stream()
                .forEach(member -> {
                    TypedQuery<Long> query = em.createQuery("Select Count(m) From Member m Where m.userId = :userId", Long.class);

                    long count = query.setParameter("userId", member.getUserId())
                                      .getSingleResult();

                    if( count == 0L ) {
                        em.persist(member);
                    }
                });
            
            tx.commit();
        } catch(Exception e) {
            tx.rollback();
            logger.error("initMember Exception : " + e.toString());
        } finally {
            em.close();
        }
    }

    @After
    public void deleteMember() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        String sql = "Delete From Member m";

        try {
            tx.begin();

            Query query = em.createQuery(sql);

            int result = query.executeUpdate();

            if( result > 0 ) {
                tx.commit();
            }
            
        } catch(Exception e) {
            tx.rollback();
            logger.error(e.toString());
        } finally {
            em.close();
        }
    }

    @Test
    public void createTest() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Team team = new Team("1111", "팀1");
            Team team2 = new Team("2222", "팀2");

            Member member1 = em.find(Member.class, "1");
            Member member2 = em.find(Member.class, "2");
            Member member3 = em.find(Member.class, "3");
            Member member4 = em.find(Member.class, "4");
            Member member5 = em.find(Member.class, "5");

            member1.setTeam(team);
            member2.setTeam(team2);
            member3.setTeam(team);
            member4.setTeam(team);

            em.persist(team);
            em.persist(team2);

            assertThat(team.getMemberList().size(), is(3));
            assertThat(team2.getMemberList().size(), is(1));

        } finally {
            tx.rollback();
            em.close();
        }
    }

    @Test
    public void teamListNull() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Team team = new Team("1111", "팀1");
            Team team2 = new Team("2222", "팀2");

            em.persist(team);
            em.persist(team2);

            Member member1 = em.find(Member.class, "1");
            Member member2 = em.find(Member.class, "2");
            Member member3 = em.find(Member.class, "3");
            Member member4 = em.find(Member.class, "4");
            Member member5 = em.find(Member.class, "5");

            member1.setTeam(team);
            member2.setTeam(team2);
            member3.setTeam(team);
            member4.setTeam(team);

            assertThat(team.getMemberList().size(), is(3));
            assertThat(team2.getMemberList().size(), is(1));

            member1.setTeam(null);
            member2.setTeam(null);
            assertThat(team.getMemberList().size(), is(2));
            assertThat(team2.getMemberList().size(), is(0));
        } finally {
            tx.rollback();
            em.close();
        }
    }

    
    @Test
    public void subqueryTest() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Team team = new Team("A", "Team A");
            Team team2 = new Team("B", "Team B");

            em.persist(team);
            em.persist(team2);

            Member member1 = em.find(Member.class, "1");
            Member member2 = em.find(Member.class, "2");
            Member member3 = em.find(Member.class, "3");
            Member member4 = em.find(Member.class, "4");
            Member member5 = em.find(Member.class, "5");

            member1.setTeam(team);
            // member2.setTeam(team2);
            member3.setTeam(team);
            member4.setTeam(team);

            // 회원이 존재하는 Team
            String query = "Select t From Team t Where (Select Count(m) From Member m Where t = m.team ) > 0 ";

            List<Team> teamList = em.createQuery(query, Team.class).getResultList();

            assertThat(teamList.size(), is(1));

            query = "Select t From Team t Where t.memberList.size > 0 ";
            teamList = em.createQuery(query, Team.class).getResultList();

            assertThat(teamList.size(), is(1));

            // 70 이상 User 없는 Team 수
            query = "Select Count(t) From Team t Where t Not In (Select t2 From Team t2 Join t2.memberList m2 Where m2.userAge >= 70) ";

            Long count = em.createQuery(query, Long.class).getSingleResult();
            
            assertThat(count, is(2L));

            // Team Member가 2~20 사이 
            query = "Select Count(t) From Team t Where t.memberList.size Between 2 And 20";

            count = em.createQuery(query, Long.class).getSingleResult();
            
            assertThat(count, is(1L));

            query = "Select Count(t) From Team t Where t.memberList is Empty";

            count = em.createQuery(query, Long.class).getSingleResult();
            
            assertThat(count, is(1L));

            query = "Select t From Team t Where :memberParam member of t.memberList";
            Team queryTeam = em.createQuery(query, Team.class).setParameter("memberParam", member1).getSingleResult();

            assertThat(queryTeam.getName(), is("Team A"));

            query = "Select t From Team t Where upper(t.name) = 'TEAM A' ";
            queryTeam = em.createQuery(query, Team.class).getSingleResult();

            assertThat(queryTeam.getId(), is("A"));

            // "Select Count(t) From Team t Where t.memberList is Not Empty"와 동일
            query = "Select Count(t) From Team t Where size(t.memberList) > 0 ";

            count = em.createQuery(query, Long.class).getSingleResult();

            assertThat(count, is(1L));

        } catch(Exception e) {
            logger.error("Err Msg : " + e.toString());
        } finally {
            tx.rollback();
            em.close();
        } 
    }
}