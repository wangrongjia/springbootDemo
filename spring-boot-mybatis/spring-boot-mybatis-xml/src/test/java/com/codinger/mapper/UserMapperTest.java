package com.codinger.mapper;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.codinger.entity.User;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTest {

	@Autowired
	private UserMapper UserMapper;

	@Test
	public void testInsert() throws Exception {
		UserMapper.insert(new User("a1","111111","man","a1"));
		UserMapper.insert(new User("a2","222222","man","a2"));
		UserMapper.insert(new User("a3","333333","man","a3"));

		Assert.assertEquals(3, UserMapper.selectAll().size());
	}

	@Test
	public void testQuery() throws Exception {
		List<User> users = UserMapper.selectAll();
		if(users==null || users.size()==0){
			System.out.println("is null");
		}else{
			System.out.println(users.toString());
		}
	}
	
	
	@Test
	public void testUpdate() throws Exception {
		User user = UserMapper.selectByPrimaryKey(2l);
		System.out.println(user.toString());
		user.setNickName("codinger");
		UserMapper.updateByPrimaryKey(user);
		Assert.assertTrue(("codinger".equals(UserMapper.selectByPrimaryKey(2l).getNickName())));
	}

}