package dao;

import model.SysUser;
import org.apache.ibatis.annotations.Param;

/**
 * dao层与对应的Mapper文件自己写，或者直接使用生成的文件
 */
public interface SysUserMapper {

    SysUser selectUser(@Param("account") String account,@Param("password") String password);

    SysUser selectUserByAccount(@Param("account")String account);

    int insertUser(SysUser sysUser);


}