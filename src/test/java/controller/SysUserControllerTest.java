package controller;

import common.PostTest;
import model.SysUser;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import service.SysUserService;

import java.util.Date;

public class SysUserControllerTest {

	@Autowired
	private SysUserService sysUserService;
	@Test
	public void getUser() {
	}

	@Test
	public void addUser() {
		SysUser sysUser = new SysUser("wan","123","管理员",1,true,new Date());

		String result = PostTest.sendPost("http://localhost:8585/user/t",sysUser.toParam());
		System.out.println(result);
	}
}