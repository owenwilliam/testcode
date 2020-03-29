package com.owen.study.aop.ioc;

public class Test
{
	public static void main(String[] args) throws Exception
	{

		// º”‘ÿ≈‰÷√Œƒº˛
		BeanFactory f = new ClassPathXmlApplicationContext(
				"applicationContext.xml");

		Object os = f.getBean("dog");
		Animal dog = (Animal) os;
		dog.say();

		Object op = f.getBean("chicken");
		Animal chicken = (Animal) op;
		chicken.say();

		Object p = f.getBean("people");
		People people = (People) p;
		people.info();
	}
}
