package com.owen.study.aop.cglib;

public class Test
{

	public static void main(String[] args)
	{
		UserManage um = new UserManage();
		TimeInterceptor time = new TimeInterceptor(um);
		um = ProxyUtil.proxyOne(um.getClass(), time);
		um.addUser("111", "¿œÕı");
	}
}
