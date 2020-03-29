package com.owen.study.aop.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

public class ProxyUtil
{
	@SuppressWarnings("unchecked")
	public static <T> T proxyOne(Class<?> clz, MethodInterceptor interceptor)
	{
		return (T) Enhancer.create(clz, interceptor);
	}

}