package com.bjpowernode.crm.setting.service;

import com.bjpowernode.crm.setting.domain.User;

import java.util.List;
import java.util.Map;

/**
 * @date: 2024/7/3 22:44
 */
public interface UserService {

	User queryUserByLoginActAndPwd(Map<String,Object> map);

	/* 查询所有的用户信息 */
	List<User> queryAllUsers();
}
