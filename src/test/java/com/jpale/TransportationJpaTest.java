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

            Long count = em.createQuery("Select Count(d) From TransDriver d Where d.driverId = :driverId", Long.class).setParameter("driverId", 1L).getSingleResult();

            if( count == 0L ) {
                driver.setDriverName("Hello");
                em.persist(driver);
            } else {
                driver = em.createQuery("Select d From TransDriver d Where d.driverId = :driverId", TransDriver.class).setParameter("driverId", 1L).getSingleResult();
            }

            em.persist(bus);

            bus.setAdultPrice(1120);
            bus.setChildPrice(650);
            bus.setStudentPrice(900);
            bus.setTimeAlloc(11);
            bus.setAreaCode(AreaCode.BUSAN);
            bus.setDriver(driver);

            
            logger.info("driver Size : " + driver.getTransList().size());

            Bus bus2 = new Bus();
            bus2.setAdultPrice(920);
            bus2.setChildPrice(750);
            bus2.setStudentPrice(1040);
            bus2.setTimeAlloc(15);
            bus2.setAreaCode(AreaCode.BUSAN);
            bus2.setDriver(driver);

            logger.info("driver Size : " + driver.getTransList().size());

            em.persist(bus2);

            Taxi taxi = new Taxi();
            taxi.setCarNumber("120Bu1002");
            taxi.setBasePrice(2000);
            taxi.setDriver(driver);

            logger.info("driver Size : " + driver.getTransList().size());

            em.persist(taxi);

            count = em.createQuery("Select Count(d) From TransDriver d Where d.driverId = :driverId", Long.class).setParameter("driverId", 2L).getSingleResult();
            
            if( count == 0 ) {
                TransDriver driver2 = new TransDriver();
                driver2.setDriverName("Hello2");
                em.persist(driver2);
            }

            TransDriver driver_find = em.find(TransDriver.class, 1L);

            TransDriver driver_find2 = em.find(TransDriver.class, 2L);
            logger.info(driver_find.getDriverId() + ", " + driver_find.getDriverName());
            logger.info("driver_find.getTransList().size() => " + driver_find.getTransList().size());

            
            assertThat(driver_find.getTransList().size(), is(3));
            assertThat(driver_find2.getTransList().size(), is(0));
            

            tx.commit();
        } catch(Exception e) {
            tx.rollback();
            logger.error(e.toString());
        } finally {
            em.close();
        }

    }

}