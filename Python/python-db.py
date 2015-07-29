
import MySQLdb,functools

from DBUtils.PooledDB import PooledDB
import sys 
reload(sys) 
sys.setdefaultencoding('utf-8') 
sys.path.append('../')
import readConfig 

__author__='xiezhaodong at 2014-8-14,'

class Mysql(object):
	'''
	This method can get the connection pool and a connection object
	'''
	conn=None
	__pool=None
	def __init__(self):
		
		self.__pool=self.__initPool()
		self.conn=self.getConn()
	@staticmethod
	def __initPool():
		'''
		A static method cannot access the instance variables
		'''
		if Mysql.__pool is None:


			try:
				print 'install pool'
				__pool=PooledDB(creator=MySQLdb,maxusage=readConfig.maxusage,mincached=readConfig.mincached,maxcached=readConfig.maxcached, db=readConfig.db, host=readConfig.host, user=readConfig.user, passwd=readConfig.passwd, charset=readConfig.charset,port=readConfig.port)
				return __pool
			except Exception,e :
				print 'create pool default',e
				return None		
	def getConn(self):
		'''
		Get a link from the connection pool
		'''
		#print 'get connection from pool'
		if self.__pool is None:
			return None
		return self.__pool.connection()
init=Mysql()
class _ConnetionCtx(object):
	'''
	Ctx get connection and  exit close connection
	can user with _ConnetionCtx():

	'''
   
	def __enter__(self):
		global init
		self.mysql=init#Load the MySQL object get links
		self.conn=None
		self.clean=False
		if self.conn is None:
			#print 'connect'
			self.conn=self.mysql.getConn()
			self.clean=True
			#print self.conn,'---------connection'

		return self
	def __exit__(self,exc_type,exc_value,traceback):
		
		if self.conn is not None and self.clean is True:
			'''
			Release the connection object
			'''
			#print 'close conn'
			self.conn.close()
			self.conn=None

def ConnectionCtxController(func):
	'''
	decorator to get connection and release connection
	The CTX parameter passed to the function

	'''

	@functools.wraps(func)
	def _wrapper(**args):
		with _ConnetionCtx() as Ctx:
			return func(Ctx=Ctx,**args)
	return _wrapper

@ConnectionCtxController
def select(Ctx,sql,kw):
	'''	get select rows
		Returns None if the said no database links, return () said there is no corresponding data
	'''
	sql=sql.replace('?','%s')
	conn=Ctx.conn
	result=None
	if conn is None:
		'''
		no conn
		'''

		return None
	else:
		'''have conn'''
		try:
			cur=conn.cursor()
			cur.execute(sql,kw)
			result=cur.fetchall()
		except Exception ,e:
			print 'select default',e
			return None
		finally:	
			cur.close()
		return result
@ConnectionCtxController
def CRUDExceptSelect(Ctx,sql,kw,batch=False):
	'''
	This method can add, delete, modify, the default batch is False if True will use batch SQL statements, 
	the return value is None for no connection or the SQL is abnormal, 
	the other back value represents the number of successful execution

	'''
	sql=sql.replace('?','%s')
	conn=Ctx.conn
	row_succcess=None
	if conn is None:
		return None
	else:
		cur=None
		try:
			cur=conn.cursor()
			if batch:
				row_succcess=cur.executemany(sql,kw)				
			else:
				row_succcess=cur.execute(sql,kw)
		except Exception, e:
			conn.rollback()
			print 'insetr default',e
		finally:
			if cur is not None:
				print 'close cur'
				cur.close()
			conn.commit()
		#
	return row_succcess



#k=[['wo222ijoi'],['1231231']]
#print CRUDExceptSelect(sql='insert into user (name) values(?)',kw=k,batch=True)
##k=[['xie1'],['xie2']]
#print insertmany(sql='insert into user(name) values(?)',kw=k)
#sql='SELECT * from reply where leave_message_id=?'
#result=select(sql=sql,kw=[1])
#print result
#print result











