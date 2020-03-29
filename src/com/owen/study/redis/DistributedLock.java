package com.owen.study.redis;

import java.util.List;
import java.util.UUID;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.exceptions.JedisException;

/**
 * �ֲ�ʽ����ʵ������
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
			//��ȡ����
			conn = jedisPool.getResource();
			//�������һ��value
			String identifier = UUID.randomUUID().toString();
			//��������keyֵ
			String lockKey = "lock:" + lockName;
			//��ʱʱ�䣬������ʱʱ�����Զ��ͷ���
			int lockExpire = (int) (timeout/ 1000);
			//��ʱʱ�䣬�����󳬹���ʱ�����Զ��ͷ���
			long end = System.currentTimeMillis() + acquireTime;
			
			while (System.currentTimeMillis() < end)
			{
				//keyֵ������
				if(conn.setnx(lockKey, identifier) == 1)
				{
					conn.expire(lockKey, lockExpire);
					
					//����valueֵ�������ͷ���ʱ��ȷ��
					retIdentifier = identifier;
					
					return retIdentifier;
				}
				
				//����-1����keyû�����ó�ʱʱ�䣬Ϊkey����һ����ʱʱ��
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
				//����lock��׼����ʼ����
				conn.watch(lockKey);
				
				//ͨ��ǰ�淵�ص�valueֵ�ж��ǲ��Ǹ��������Ǹ�������ɾ�����ͷ���
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
