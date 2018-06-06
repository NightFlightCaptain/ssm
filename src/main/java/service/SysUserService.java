package service;

import common.exception.IException;
import model.SysUser;

/**
 * Copyright(C) 2018-2018
 * Author: wanhaoran
 * Date: 2018/5/31 11:12
 */
public interface SysUserService {

	SysUser getUser(String account, String password);


	SysUser getUserByAccount(String account);

	int addUser(SysUser sysUser) throws IException;
}
