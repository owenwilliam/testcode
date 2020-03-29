package com.owen.study.redis;

import java.util.List;
import java.util.UUID;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.exceptions.JedisException;

/**
 * 分布式锁简单实例代码
 * @author owenlin
 *
 */
public class DistributedLock
{

	private final JedisPool jedisPool;
	
	public DistributedLock(JedisPool jedisPool)
	{
		this.jedisPool = jedisPool;
	}
	
	public String lockWithTimeout(String lockName, long acquireTime, long timeout)
	{
		Jedis conn = null;
		String retIdentifier = null;
		
		try
		{
			//获取连接
			conn = jedisPool.getResource();
			//随机生成一个value
			String identifier = UUID.randomUUID().toString();
			//锁名，即key值
			String lockKey = "lock:" + lockName;
			//超时时间，上锁后超时时间则自动释放锁
			int lockExpire = (int) (timeout/ 1000);
			//超时时间，上锁后超过此时间则自动释放锁
			long end = System.currentTimeMillis() + acquireTime;
			
			while (System.currentTimeMillis() < end)
			{
				//key值不存在
				if(conn.setnx(lockKey, identifier) == 1)
				{
					conn.expire(lockKey, lockExpire);
					
					//返回value值，用于释放锁时间确认
					retIdentifier = identifier;
					
					return retIdentifier;
				}
				
				//返回-1代表key没有设置超时时间，为key设置一个超时时间
				if(conn.ttl(lockKey) == 1)
				{
					conn.expire(lockKey, lockExpire);
				}
				
				try
				{
					Thread.sleep(10);
				} catch (InterruptedException e)
				{
					Thread.currentThread().interrupt();
				}
				
			}
			
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			if(conn != null)
			{
				conn.close();
			}
		}
		return retIdentifier;
	}
	
	public boolean releaseLock(String lockName, String identifier)
	{
		Jedis conn = null;
		String lockKey = "lock:" + lockName;
		boolean retFlag = false;
		
		try
		{
			conn = jedisPool.getResource();
			
			while(true)
			{
				//监视lock，准备开始事务
				conn.watch(lockKey);
				
				//通过前面返回的value值判断是不是该锁，若是该锁，则删除，释放锁
				if(identifier.equals(conn.get(lockKey)))
				{
					Transaction transaction = conn.multi();
					transaction.del(lockKey);
					
					List<Object> result = transaction.exec();
					
					if(result == null)
					{
						continue;
					}
					
					retFlag = true;
					
				}
				
				conn.unwatch();
				break;
			}
		} catch (JedisException e)
		{
			e.printStackTrace();
		}finally
		{
			if(conn != null)
			{
				conn.close();
			}
		}
		
		return retFlag;
	}
}
