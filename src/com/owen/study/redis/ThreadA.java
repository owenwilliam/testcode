package com.owen.study.redis;

public class ThreadA extends Thread
{
	
	private Service service;
	
	public ThreadA(Service service)
	{
		this.service = service;
	}

	@Override
	public void run()
	{
		service.seckill();
	}
	

}
