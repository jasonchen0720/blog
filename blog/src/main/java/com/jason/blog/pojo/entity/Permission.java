package com.jason.blog.pojo.entity;

import javax.persistence.*;
import javax.persistence.Column;

/**
 * Created by jason on 2016/8/30.
 */
@Entity
@Table(name="T_PERMISSION")
public class Permission {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private Integer id;

    @Column(name = "permission_name")
    private String permissionName;

    @ManyToOne
    @JoinColumn(name="role_id")
    private Role role;


    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getPermissionName() {
        return permissionName;
    }
    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }

}
