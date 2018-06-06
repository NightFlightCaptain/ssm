package service;

import model.SysPermission;

import java.util.List;

public interface SysPermissionService {
	public List<SysPermission> getPermissionsByRoleId(Integer roleId);

	public List<SysPermission> getPermissionsByUserAccount(String account);

	public Integer getRoleByUserAccount(String account);
}
