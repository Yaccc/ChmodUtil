### 工具说明 ###
-------------------
#### JAVA包 ####
- `FilterAnnotationUtil.java`类是一个过滤文件注释的工具类(filter包下),主要过滤`//`和`/**/`两类的注释.`String filterContent(URL url)`调用这个方法，传入文件的URL路径
```java

  FilterAnnotationUtil.Instance().filterContent(fileUrl)
  
```
 感谢[Alienero's](https://github.com/Alienero)提供的思路

- `ReadWriteLockOfLockSimple`，`ReadWriteLockCanAccess`这两个类是读写锁的实现，前者是比较简单的实现，后者实现比较完全，基本实现了读/写锁可重入，读锁升级，写锁降级等功能。当然和`JDK`中的差距还是非常大，只是给大家提供了一个可学习的思路。`readwritelock.example`包下面是例程
```java
public void run() {                                                                
         if (!isWrite){                                             
             readWrite.readLock();                                  
             try {                                                  
                 Thread.currentThread().sleep(5000);              
             } catch (InterruptedException e) {                 
                 e.printStackTrace();                              
             }                                                  
             readWrite.unReadLock();                          
         }else{                                                 
             readWrite.writeLock();                                             
             try {                                                   
                 Thread.currentThread().sleep(3000);             
             } catch (InterruptedException e) {                     
                 e.printStackTrace();                        
             }                                                          
             readWrite.unWriterLock();                                               
         }                                                                              
     }                                                                                  
```

#### Python包 ####
- `python-db.py`是对`SQL`工具类的一个封装.使用它(见下文)
```python
from DBUtils.PooledDB import PooledDB
#一行的CRUD都封装到了一个方法，对于多行插入也有实现
#最后一个参数表示是否是批量，第一个是SQL,第二个是value值得可变数组
k=[['wo222ijoi'],['1231231']]
print CRUDExceptSelect(sql='insert into user (name) values(?)',kw=k,batch=True)

```
- [English document](https://github.com/xiexiaodong/Utils/blob/master/README.md)
- [中文文档](https://github.com/xiexiaodong/Utils/blob/master/README_ZH_CN.md)
