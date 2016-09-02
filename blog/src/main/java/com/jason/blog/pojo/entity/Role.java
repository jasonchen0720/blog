package com.jason.blog.pojo.entity;

import javax.persistence.*;
import javax.persistence.Column;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jason on 2016/8/30.
 */
@Entity
@Table(name = "T_ROLE")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer id;

    @Column(name = "role_name")
    private String roleName;

    @OneToMany(mappedBy = "role")
    private List<Permission> permissions;

    @ManyToMany
    @JoinTable(name = "T_USER_ROLE", joinColumns = {@JoinColumn(name = "role_id")}, inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private List<User> users;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Transient
    public List<String> getPermissionsName() {
        List<Permission> permissions = getPermissions();
        List<String> list = new ArrayList<>();
        permissions.forEach(permission -> list.add(permission.getPermissionName()));
        return list;
    }
}
