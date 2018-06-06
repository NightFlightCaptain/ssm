package model;

public class SysRolePermission {
    private Integer id;

    private Integer sysRoleId;

    private Integer sysPermissionId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSysRoleId() {
        return sysRoleId;
    }

    public void setSysRoleId(Integer sysRoleId) {
        this.sysRoleId = sysRoleId;
    }

    public Integer getSysPermissionId() {
        return sysPermissionId;
    }

    public void setSysPermissionId(Integer sysPermissionId) {
        this.sysPermissionId = sysPermissionId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", sysRoleId=").append(sysRoleId);
        sb.append(", sysPermissionId=").append(sysPermissionId);
        sb.append("]");
        return sb.toString();
    }
}