package com.owen.study.aop.cglib;

public class UserManage
{
	public void addUser(String userId, String userName)
	{
		System.out.println("addUser(id:" + userId + ",name:" + userName + ")");
	}
}
