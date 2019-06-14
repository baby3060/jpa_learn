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

public class TransportationJpaTest {

    private static EntityManagerFactory emf;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @BeforeClass
    public static void setup() {
        emf = ManagerFactoryMaker.getFactoryInstance();
    }

    @Before
    public void initProc() {
        deleteAll();
    }

    private void deleteAll() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            em.createQuery("Delete From Bus").executeUpdate();
            em.createQuery("Delete From Taxi").executeUpdate();
            em.createQuery("Delete From Transportation").executeUpdate();
            em.createNativeQuery("alter table Transportation auto_increment = 1").executeUpdate();

            tx.commit();
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

            Bus bus = new Bus();
            
            TransDriver driver = new TransDriver();

            Long count = em.createQuery("Select Count(d) From TransDriver d", Long.class).getSingleResult();

            if( count == 0L ) {
                driver.setDriverName("Hello");
                em.persist(driver);
            } else {
                driver = em.createQuery("Select d From TransDriver d", TransDriver.class).getSingleResult();
            }

            em.persist(bus);

            bus.setAdultPrice(1120);
            bus.setChildPrice(650);
            bus.setStudentPrice(900);
            bus.setTimeAlloc(11);
            bus.setAreaCode(AreaCode.BUSAN);
            bus.setDriver(driver);

            Taxi taxi = new Taxi();
            taxi.setCarNumber("120Bu1002");
            taxi.setBasePrice(2000);
            taxi.setDriver(driver);

            em.persist(taxi);

            tx.commit();
        } catch(Exception e) {
            tx.rollback();
            logger.error(e.toString());
        } finally {
            em.close();
        }

    }

}