package com.codinger.mapper.test1;

import java.util.List;

import com.codinger.mapper.test1.User1Mapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.codinger.entity.User;

@RunWith(SpringRunner.class)
@SpringBootTest
public class User1MapperTest {

	@Autowired
	private User1Mapper userMapper;

	@Test
	public void testInsert() throws Exception {
		userMapper.insert(new User("a1","111111","man","a1"));
		userMapper.insert(new User("a2","222222","man","a2"));
		userMapper.insert(new User("a3","333333","man","a3"));

		Assert.assertEquals(3, userMapper.selectAll().size());
	}

	@Test
	public void testQuery() throws Exception {
		List<User> users = userMapper.selectAll();
		if(users==null || users.size()==0){
			System.out.println("is null");
		}else{
			System.out.println(users.size());
		}
	}
	
	
	@Test
	public void testUpdate() throws Exception {
		User user = userMapper.selectByPrimaryKey(2l);
		System.out.println(user.toString());
		user.setNickName("codinger");
		userMapper.updateByPrimaryKey(user);
		Assert.assertTrue(("codinger".equals(userMapper.selectByPrimaryKey(2l).getNickName())));
	}

}