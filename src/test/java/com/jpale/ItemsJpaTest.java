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

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.*;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class ItemsJpaTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static EntityManagerFactory emf;

    @BeforeClass
    public static void setup() {
        emf = ManagerFactoryMaker.getFactoryInstance();
    }

    @Before
    public void init() {
        deleteAll();
    }

    public void deleteAll() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            String sql = "Delete From Items";

            em.createQuery(sql).executeUpdate();

            em.createNativeQuery("Alter table Items auto_increment = 1 ").executeUpdate();

            tx.commit();
        } catch(Exception e) {
            tx.rollback();
            logger.error(e.toString());
        } finally {
            em.close();
        }
    }

    @Test
    public void insertItems() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Album album = new Album();
            album.setName("앨범1");
            album.setPrice(3000);
            album.setArtist("아티스트1");

            em.persist(album);

            Book book = new Book();
            book.setName("책1");
            book.setPrice(1000);
            book.setIsbn("1--2030401");

            em.persist(book);

            Movie movie = new Movie();
            movie.setName("영화1");
            movie.setPrice(6000);
            movie.setActor("이길동");

            em.persist(movie);

            List<Items> itemList = em.createQuery("Select i From Items i Order By i.id", Items.class).getResultList();

            assertThat(itemList.size(), is(3));
            
            assertThat(itemList.get(0), instanceOf(Album.class));
            assertThat(itemList.get(1), instanceOf(Book.class));
            assertThat(itemList.get(2), instanceOf(Movie.class));
            
            itemList.stream()
                    .forEach(items -> {
                        logger.info(items.getDiscriminatorValue());
                    });

        } catch(Exception e) {
            logger.error(e.toString());
        } finally {
            tx.rollback();
            em.close();
        }
    }
}