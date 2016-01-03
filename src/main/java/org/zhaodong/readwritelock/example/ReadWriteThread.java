package org.zhaodong.readwritelock.example;


import org.zhaodong.readwritelock.ReadWriteLockOfLockSimple;

import java.util.Date;

public class ReadWriteThread implements Runnable{
     private boolean isWrite;
     private ReadWriteLockOfLockSimple readWrite;
                                                                                        
     public ReadWriteThread( boolean isWrite, ReadWriteLockOfLockSimple readWrite) {    
                                                                                        
         this.isWrite = isWrite;                                                        
         this.readWrite = readWrite;                                                    
     }                                                                                  
                                                                                        
                                                                                        
     public void run() {                                                                
         if (!isWrite){                                                                 
             System.out.println(Thread.currentThread().getId()+"[尝试]读锁"+new Date());
             readWrite.readLock();                                                      
             System.out.println(Thread.currentThread().getId()+"[获得]读锁[5s]"+new Date());
             try {                                                                      
                 Thread.currentThread().sleep(5000);                                    
             } catch (InterruptedException e) {                                         
                 e.printStackTrace();                                                   
             }                                                                          
             System.out.println(Thread.currentThread().getId()+"[释放]读锁"+ new Date());   
             readWrite.unReadLock();                                                    
         }else{                                                                         
             System.out.println(Thread.currentThread().getId()+"[尝试]写锁"+new Date());    
                                                                                        
             readWrite.writeLock();                                                     
             System.out.println(Thread.currentThread().getId()+"[获得]写锁 [3s]"+new Date());
                                                                                        
             try {                                                                      
                 Thread.currentThread().sleep(3000);                                    
             } catch (InterruptedException e) {                                         
                 e.printStackTrace();                                                   
             }                                                                          
             System.out.println(Thread.currentThread().getId()+"[释放]写锁"+new Date());    
             readWrite.unWriterLock();                                                  
                                                                                        
                                                                                        
                                                                                        
         }                                                                              
     }                                                                                  
 }                                                                                      