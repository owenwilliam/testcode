package com.owen.study.aop.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class ProxyUtil
{

	@SuppressWarnings("unchecked")
	public static <T> T proxyOne(ClassLoader loader, Class<?>[] clz,
			InvocationHandler handler)
	{
		System.out.println(">>>:"+loader+">>>clz:"+clz);
		return (T) Proxy.newProxyInstance(loader, clz, handler);
	}
}