package model;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.Date;

/**
 * 如果有构造器，一定要写一个无参的构造器
 * SysUser作为Session储存对象需要实现序列化接口
 */
public class SysUser implements Serializable{
	public SysUser( String account, String password, String nickname, Integer roleId, Boolean status, Date createTime) {
		this.account = account;
		this.password = password;
		this.nickname = nickname;
		this.roleId = roleId;
		this.status = status;
		this.createTime = createTime;
	}

    /**
     * 必须有
     */
	public SysUser() {
	}

	@ApiModelProperty(hidden = true)
	private Integer id;

	@ApiModelProperty(example = "wan")
    @NotEmpty(message = "姓名不能为空")
    private String account;

	@ApiModelProperty(example = "123")
    @Length(min = 6,message = "密码不能少于6位")
    private String password;

	@ApiModelProperty(example = "管理员")
    private String nickname;

	@ApiModelProperty(example = "1")
    private Integer roleId;

	@ApiModelProperty(example = "false")
    private Boolean status;

    @ApiModelProperty(hidden = true)
    private Date createTime;

    @ApiModelProperty(hidden = true)
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", account=").append(account);
        sb.append(", password=").append(password);
        sb.append(", nickname=").append(nickname);
        sb.append(", roleId=").append(roleId);
        sb.append(", status=").append(status);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append("]");
        return sb.toString();
    }

    
    public String toParam() {
        StringBuilder sb = new StringBuilder();
        sb.append("id=").append(id);
        sb.append("&account=").append(account);
        sb.append("&password=").append(password);
        sb.append("&nickname=").append(nickname);
        sb.append("&roleId=").append(roleId);
        sb.append("&status=").append(status);
        sb.append("&createTime=").append(createTime);
        return sb.toString();
    }
}