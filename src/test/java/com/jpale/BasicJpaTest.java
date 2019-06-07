package com.jpale;

import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import javax.persistence.*;

public class BasicJpaTest {
    private static EntityManagerFactory emf;

    @BeforeClass
    public static void setup() {
        emf = Persistence.createEntityManagerFactory("learningPersistance");
    }

    @Test
    public void createTest() {
        EntityManager em = emf.createEntityManager();

        em.close();
    }

}