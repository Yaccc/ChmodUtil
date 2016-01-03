### Tool description ###
-------------------
#### JAVA PACKAGE ####
- `FilterAnnotationUtil.java`Is a tool for filtering file notes,The main filtration and two kinds of notes `/**/` `//`. Call this method`String filterContent(URL url)`，URL path to the file
```java
FilterAnnotationUtil.Instance().filterContent(fileUrl)
```
Thanks for [Alienero's](https://github.com/Alienero) ideas

- `ReadWriteLockOfLockSimple`，`ReadWriteLockCanAccess`These two classes are read and write lock implementation, the former is relatively simple to achieve, the latter is relatively complete，Basically realize the read / write lock can be re entered，Read lock upgrade, write lock degradation and other functions。Of course, the gap is still very large and `JDK`，Just to provide you with a learning idea。`readwritelock.example`Package is an example.
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


#### Python PACKAGE ####
- `python-db.py`Is a package of `SQL` tools.Use it(see below)
```python
from DBUtils.PooledDB import PooledDB
#CRUDExceptSelect Can execute SQL CRUD
#The last parameter indicates whether is batch，the first is your SQL,the second is your value arrays
k=[['wo222ijoi'],['1231231']]
print CRUDExceptSelect(sql='insert into user (name) values(?)',kw=k,batch=True)

```
- [English document](https://github.com/xiexiaodong/Utils/blob/master/README.md)
- [中文文档](https://github.com/xiexiaodong/Utils/blob/master/README_ZH_CN.md)
