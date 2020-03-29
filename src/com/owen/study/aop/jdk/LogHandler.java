package com.owen.study.aop.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


public class LogHandler implements InvocationHandler
{

	// Ŀ�����
	private Object targetObject;

	public LogHandler(Object targetObject)
	{
		this.targetObject = targetObject;
	}

	@Override
	// ���������ʵ����ķ���������ʱ����ִ��
	/*
	 * InvocationHandler�ӿڵķ�����proxy��ʾ����method��ʾԭ���󱻵��õķ����� args��ʾ�����Ĳ���
	 */
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable
	{
		Object ret = null;
		try
		{
			System.out.println("LogHandler����֮ǰ��" + System.currentTimeMillis());
			// ����Ŀ�귽��
			ret = method.invoke(targetObject, args);
			System.out.println("LogHandler����֮��" + System.currentTimeMillis());
		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("error");
			throw e;
		}
		return ret;
	}

}