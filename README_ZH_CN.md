### 工具说明 ###
-------------------
#### JAVA包 ####
- `FilterAnnotationUtil.java`类是一个过滤文件注释的工具类,主要过滤`//`和`/**/`两类的注释.`String filterContent(URL url)`调用这个方法，传入文件的URL路径
```java

  FilterAnnotationUtil.Instance().filterContent(fileUrl)
  
```
 感谢[Alienero's](https://github.com/Alienero)提供的思路

#### Python包 ####
- `python-db.py`是对`SQL`工具类的一个封装.使用它(见下文)
```python
from DBUtils.PooledDB import PooledDB
#一行的CRUD都封装到了一个方法，对于多行插入也有实现
#最后一个参数表示是否是批量，第一个是SQL,第二个是value值得可变数组
k=[['wo222ijoi'],['1231231']]
print CRUDExceptSelect(sql='insert into user (name) values(?)',kw=k,batch=True)

```
