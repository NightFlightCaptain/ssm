package controller;

import common.exception.IException;
import common.utils.RedisUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import model.Msg;
import model.SysPermission;
import model.SysUser;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.SysPermissionService;
import service.SysUserService;

import javax.validation.Valid;
import java.util.List;

/**
 * Copyright(C) 2018-2018
 * Author: wanhaoran
 * Date: 2018/5/31 11:12
 */
@RestController
@RequestMapping("/user")
public class SysUserController {
	@Autowired
	RedisUtil redisUtil ;

	@Autowired
	private SysUserService sysUserService;

	@Autowired
	private SysPermissionService sysPermissionService;

	@ApiOperation(value = "查询用户",notes = "根据账号和密码查询用户")
	@GetMapping()
	@RequiresPermissions("user:select")
	public Msg getUser(@ApiParam(defaultValue = "wan2")@RequestParam("account") String account,
	                   @ApiParam(defaultValue = "123")@RequestParam("password") String password) throws IException {
		SysUser sysUser = sysUserService.getUser(account, password);
		return Msg.success("成功查询用户").add("user",sysUser);
	}

	@RequiresPermissions("user:add")
	@ApiOperation(value = "新增用户", notes = "根据用户对象来新增")
	@PostMapping()
	public Msg addUser(@RequestBody @Valid SysUser sysUser) throws Exception{
		sysUserService.addUser(sysUser);
		return Msg.message(201,"成功新增用户");
	}

	@GetMapping(value = "/permission")
	@RequiresPermissions("permission:select")
	public Msg getPermissions(@ApiParam(defaultValue = "wan2")@RequestParam("account")String account){
		List<SysPermission> permissions = sysPermissionService.getPermissionsByUserAccount(account);
		return Msg.success("获取权限").add("permission",permissions);
	}

	@GetMapping(value = "/redis")
	public Msg testRedis(@ApiParam(defaultValue = "bling")@RequestParam("string")String string){
		redisUtil.set("name",string);
		return Msg.success("成功添加redis").add("string",string);
	}

	@GetMapping(value = "/getRedis")
	public Msg testGetRedis(){
		return Msg.success("取回数据").add("string",redisUtil.get("name"));
	}
}
