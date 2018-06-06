package service.impl;

import dao.SysUserMapper;
import model.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.SysUserService;

import java.util.Date;

/**
 * 这个地方Transactional报错，也不懂为什么
 *
 * Copyright(C) 2018-2018
 * Author: wanhaoran
 * Date: 2018/5/31 11:24
 */
@Service("sysUserService")
@Transactional(value = "transactionManager")
public class SysUserServiceImpl implements SysUserService {

	@Autowired
	private SysUserMapper sysUserMapper;

	@Override
	public SysUser getUser(String account, String password) {
		return sysUserMapper.selectUser(account, password);
	}

	@Override
	public SysUser getUserByAccount(String account){
		return sysUserMapper.selectUserByAccount(account);
	}
	@Override
	public int addUser(SysUser sysUser) {
		//这个地方最好是使用多个model，例如qo，vo之类的来进行参数传递
		sysUser.setCreateTime(new Date());
		return sysUserMapper.insertUser(sysUser);
	}
}
