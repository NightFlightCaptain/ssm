package auth;

import model.SysPermission;
import model.SysUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import service.SysPermissionService;
import service.SysUserService;

import java.util.ArrayList;
import java.util.List;


/**
 * Copyright(C) 2018-2018
 * Author: wanhaoran
 * Date: 2018/6/5 10:00
 */
public class UserRealm extends AuthorizingRealm {

	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysPermissionService sysPermissionService;

	@Override
	public void setName(String name) {
		super.setName("userRealm");
	}

	/**
	 * 登陆认证
	 *
	 * @param token
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		//token是用户输入的
		String account = (String) token.getPrincipal();
		SysUser sysUser = sysUserService.getUserByAccount(account);
		if (sysUser == null) {
			return null;
		}
		System.out.println(sysUser.toString());
		SimpleAuthenticationInfo simpleAuthenticationInfo =
				new SimpleAuthenticationInfo(sysUser, sysUser.getPassword(), this.getName());
		return simpleAuthenticationInfo;
	}

	//	用于授权
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SysUser sysUser =  (SysUser)principals.getPrimaryPrincipal();
		List<SysPermission> sysPermissions = sysPermissionService.getPermissionsByUserAccount(sysUser.getAccount());
		List<String> permissionValus = new ArrayList<String>();
		if (sysPermissions != null) {
//			System.out.println(sysPermissions.size());
			for (SysPermission sysPermission : sysPermissions) {
				permissionValus.add(sysPermission.getValue());
//				System.out.println(sysPermission.toString());
			}
		}
		SimpleAuthorizationInfo simpleAuthorizationInfo
				= new SimpleAuthorizationInfo();
		simpleAuthorizationInfo.addStringPermissions(permissionValus);
		return simpleAuthorizationInfo;
	}

	//清除缓存
	public void clearCached() {
		PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
		super.clearCache(principals);
	}
}
