package com.jpale.common;

import java.util.concurrent.locks.ReentrantLock;

import javax.persistence.*;

public class ManagerFactoryMaker {
    private static EntityManagerFactory managerFactory;

    // 싱글톤으로 생성하기 위한 또다른 방법 syncronized가 아닌
    private static final ReentrantLock lock = new ReentrantLock();

    private ManagerFactoryMaker() {}

    public static EntityManagerFactory getFactoryInstance() {

        if( managerFactory == null ) {
            lock.lock();

            try {
                managerFactory = Persistence.createEntityManagerFactory("learningPersistance"); 
            } finally {
                lock.unlock();
            }
        } 

        return managerFactory;
    }
}