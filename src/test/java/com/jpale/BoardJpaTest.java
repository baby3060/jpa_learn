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

public class BoardJpaTest {
    private static EntityManagerFactory emf;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @BeforeClass
    public static void setup() {
        emf = ManagerFactoryMaker.getFactoryInstance();

    }

    @Test
    public void insertTest() {
        Board board = new Board();
        board.setWriterId("1");
        board.setDescription("TEst");
        board.setBoardType(BoardType.USER);

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            em.persist(board);

            tx.commit();
        } catch(Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {  
            em.close();
        }
    }
}