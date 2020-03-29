package com.owen.study.aop.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


public class TimeHandler implements InvocationHandler
{

	// 目标对象
	private Object targetObject;

	public TimeHandler(Object targetObject)
	{
		this.targetObject = targetObject;
	}

	@Override
	// 关联的这个实现类的方法被调用时将被执行
	/*
	 * InvocationHandler接口的方法，proxy表示代理，method表示原对象被调用的方法， args表示方法的参数
	 */
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable
	{
		Object ret = null;
		try
		{
			System.out.println("TimeHandler方法之前：" + System.currentTimeMillis());
			// 调用目标方法
			ret = method.invoke(targetObject, args);
			System.out.println("TimeHandler方法之后：" + System.currentTimeMillis());
		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("error");
			throw e;
		}
		return ret;
	}

}