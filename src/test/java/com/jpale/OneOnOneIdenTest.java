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

public class OneOnOneIdenTest {
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


    private void deleteAll() {
        EntityManager em = emf.createEntityManager();


        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            em.createQuery("Delete From BoardDetail").executeUpdate();
            em.createQuery("Delete From BoardIdentity").executeUpdate();

            tx.commit();
        } catch(Exception e) {
            tx.rollback();
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

            BoardIdentity board = new BoardIdentity();
            board.setTitle("123455");
            em.persist(board);


            BoardDetail boardDetail = new BoardDetail();
            boardDetail.setContent("dididi");
            boardDetail.setBoard(board);
            em.persist(boardDetail);

            assertThat(board.getNo(), is(boardDetail.getBoardNo()));

        } catch(Exception e) {
            logger.error(e.toString());
        } finally {
            tx.rollback();
            em.close();
        }
    }

    
}