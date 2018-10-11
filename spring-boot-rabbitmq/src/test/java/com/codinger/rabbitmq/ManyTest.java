package com.codinger.rabbitmq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.codinger.manyToMany.CodingerSender;
import com.codinger.manyToMany.CodingerSender2;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ManyTest {
	@Autowired
	private CodingerSender codingerSender;

	@Autowired
	private CodingerSender2 codingerSender2;

	@Test
	public void oneToMany() throws Exception {
		for (int i=0;i<100;i++){
			codingerSender.send(i);
		}
	}

	@Test
	public void manyToMany() throws Exception {
		for (int i=0;i<100;i++){
			codingerSender.send(i);
			codingerSender2.send(i);
		}
	}

}