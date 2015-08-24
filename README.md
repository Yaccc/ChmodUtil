### Tool description ###
-------------------
#### JAVA PACKAGE ####
- `FilterAnnotationUtil.java`Is a tool for filtering file notes,The main filtration and two kinds of notes `/**/` `//`. Call this method`String filterContent(URL url)`，URL path to the file
```java
FilterAnnotationUtil.Instance().filterContent(fileUrl)
```
Thanks for [Alienero's](https://github.com/Alienero) ideas
- `KeyGenForIDEA.java`IDEA development tool for the registration code, we use IDEA software development.Do not recommend using this tool. Respect copyright, from me
```java
1. javac KeyGenForIDEA.java
2. java KeyGenForIDEA yourname
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
[中文文档](https://github.com/xiexiaodong/Utils/blob/master/README_ZH_CN.md)
