package com.owen.study.aop.jdk;

public class Test
{

	public static void main(String[] args)
	{
		UserManager um = new UserManagerImpl();
		LogHandler log = new LogHandler(um);
		um = ProxyUtil.proxyOne(um.getClass().getClassLoader(), um.getClass()
				.getInterfaces(), log);

		TimeHandler time = new TimeHandler(um);
		um = ProxyUtil.proxyOne(um.getClass().getClassLoader(), um.getClass()
				.getInterfaces(), time);

		um.addUser("1111", "张三");
		um.delUser("2222", "张三");
	}
}
