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

public class ExtendsJpaTest {
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
            em.createQuery("Delete From BookPer").executeUpdate();
            em.createQuery("Delete From MoviePer").executeUpdate();
            em.createQuery("Delete From AlbumPer").executeUpdate();
            em.createQuery("Delete From Buyer").executeUpdate();
            em.createQuery("Delete From Seller").executeUpdate();


            em.createNativeQuery("Update items_sequence Set next_val = 0 Where sequence_name = 'ITEMS_SEQ' ").executeUpdate();
            em.createNativeQuery("Alter table Items auto_increment = 1 ").executeUpdate();
            em.createNativeQuery("Alter table Buyer auto_increment = 1 ").executeUpdate();
            em.createNativeQuery("Alter table Seller auto_increment = 1 ").executeUpdate();

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

    @Test
    public void perTest() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            AlbumPer album = new AlbumPer();
            album.setName("앨범1");
            album.setPrice(3000);
            album.setArtist("아티스트1");

            em.persist(album);

            BookPer book = new BookPer();
            book.setName("책1");
            book.setPrice(1000);
            book.setIsbn("1--2030401");

            em.persist(book);

            MoviePer movie = new MoviePer();
            movie.setName("영화1");
            movie.setPrice(6000);
            movie.setActor("이길동");

            em.persist(movie);

            List<ItemsPer> totalList = new ArrayList<ItemsPer>();

            List<BookPer> itemList = em.createQuery("Select i From BookPer i Order By i.id", BookPer.class).getResultList();
            List<MoviePer> itemList2 = em.createQuery("Select i From MoviePer i Order By i.id", MoviePer.class).getResultList();
            List<AlbumPer> itemList3 = em.createQuery("Select i From AlbumPer i Order By i.id", AlbumPer.class).getResultList();
            
            totalList.addAll(itemList);
            totalList.addAll(itemList2);
            totalList.addAll(itemList3);

            assertThat(totalList.size(), is(3));

            totalList.stream()
                    .forEach(itemPer -> {
                        logger.info("ItemPer Name : " + itemPer.getName());
                    });

        } catch(Exception e) {
            logger.error(e.toString());
        } finally {
            tx.rollback();
            em.close();
        }
    }

    @Test
    public void mappedClassTest() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Buyer buyer1 = new Buyer();
            buyer1.setName("Buyer1");
            buyer1.setEmail("1@dfi.com");

            em.persist(buyer1);
            
        } catch(Exception e) {
            logger.error(e.toString());
        } finally {
            tx.rollback();
            em.close();
        }
    }

}