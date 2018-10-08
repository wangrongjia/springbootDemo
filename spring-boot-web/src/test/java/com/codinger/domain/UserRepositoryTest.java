package com.codinger.domain;

import java.text.DateFormat;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;
	
	@Test
	public void test() throws Exception {
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);        
		String formattedDate = dateFormat.format(date);
		
		userRepository.save(new User("a1", "111111", "a1@163.com", "a1",formattedDate));
		userRepository.save(new User("a2", "222222", "a2@263.com", "a2",formattedDate));
		userRepository.save(new User("a3", "333333", "a3@363.com", "a3",formattedDate));
		userRepository.save(new User("a4", "444444", "a4@463.com", "a4",formattedDate));
		userRepository.save(new User("a5", "555555", "a5@563.com", "a5",formattedDate));
		userRepository.save(new User("a6", "666666", "a6@663.com", "a6",formattedDate));

		Assert.assertEquals(6, userRepository.findAll().size());
		Assert.assertEquals("a2", userRepository.findByUserNameOrEmail("a2", "a2@163.com").getNickName());
		userRepository.delete(userRepository.findByUserName("a1"));
	}
}
