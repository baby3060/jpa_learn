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

public class BoardJpaTest {
    private static EntityManagerFactory emf;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @BeforeClass
    public static void setup() {
        emf = ManagerFactoryMaker.getFactoryInstance();
    }

    @Test
    public void insertBoard() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            Member member = new Member("1", "테스트1", 1, "패스워드1");

            String countSql = "Select Count(m) As cnt From Member m Where m.userId = :userId";

            TypedQuery<Long> query = em.createQuery(countSql, Long.class).setParameter("userId", member.getUserId());

            long count = query.getSingleResult();

            if( count == 0L ) {
                em.persist(member);
            } else {
                member = em.find(Member.class, member.getUserId());
            }
            
            

            Board board = new Board();
            board.setDescription("Test1");
            board.setBoardType(BoardType.USER);
            board.setMember(member);

            em.persist(board);
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
            Member member = new Member("1", "테스트1", 1, "패스워드1");

            String countSql = "Select Count(m) As cnt From Member m Where m.userId = :userId";

            TypedQuery<Long> query = em.createQuery(countSql, Long.class).setParameter("userId", member.getUserId());

            long count = query.getSingleResult();

            if( count == 0L ) {
                em.persist(member);
            } else {
                member = em.find(Member.class, member.getUserId());
            }

            Board board = new Board();
            board.setDescription("Test1");
            board.setBoardType(BoardType.USER);
            board.setMember(member);

            em.persist(board);

            Board board2 = new Board();
            board2.setDescription("Test2");
            board2.setBoardType(BoardType.USER);
            board2.setMember(member);

            em.persist(board2);

            Member member_find = em.find(Member.class, "1");

            assertThat(member_find.getBoardList().size(), is(2));

        } finally {
            tx.rollback();
            em.close();
        }
    }

}