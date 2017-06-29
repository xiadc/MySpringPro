package com.xdc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xdc.dao.UserMapper;
import com.xdc.po.User;
import com.xdc.service.UserService;

/** 
 * @author xiadc 
 * createtime：2017年4月10日 下午4:11:41 
 * 类说明 
 */

@Transactional
@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserMapper userMapper;
	
	@Override
	public List<User> findUser() {
		return  userMapper.findUser();		
	}

}
