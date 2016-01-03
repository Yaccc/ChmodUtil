package org.zhaodong.readwritelock;


import java.util.Date;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by zhaodong on 16/1/3.
 * 一个简单的读写锁实现,使用方式看例程,这个实现读锁和写锁都是不可重入的,所以比较简单
 */
public class ReadWriteLockOfLockSimple {
    private int writers;
    private int readers;
    private int requests;//正在请求数量
    //排它锁
    private Lock lock=new ReentrantLock(false);
    private Condition condition=lock.newCondition();

    public  void readLock(){
        lock.lock();
        try {

        //  写锁被占用或者有正在请求写锁的线程,被挂起
            while (writers>0||requests>0){
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            readers++;
        }finally {
            lock.unlock();
        }
    }
    public  void unReadLock(){
        lock.lock();
        try {
            //唤醒所有线程,竞争锁
            readers--;
            condition.signalAll();
        }finally {
            lock.unlock();
        }


    }

    public  void writeLock(){
        lock.lock();
        try {
            //请求线程  如果锁被占用 不管是读锁还是写锁都不行
            requests++;
            while (readers>0||writers>0){
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            writers++;
            requests--;
        }finally {
            lock.unlock();
        }


    }
    public  void unWriterLock(){
        lock.lock();
        try {
            writers--;
            condition.signalAll();

        }finally {
            lock.unlock();
        }
    }



}
