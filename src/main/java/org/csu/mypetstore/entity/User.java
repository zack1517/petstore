package org.csu.mypetstore.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 用户及管理员账号，对应表 store_user。
 * 列名为下划线风格，属性为驼峰风格，由 MyBatis-Plus 自动转换。
 */
@Getter
@Setter
@TableName("store_user")
public class User {

    /** 内部主键，数据库自增 BIGINT。 */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 登录名，创建后不修改，唯一。 */
    private String username;

    /** BCrypt 散列，不保存明文密码。 */
    private String passwordHash;

    /** 显示名称。 */
    private String displayName;

    /** 角色：CUSTOMER 或 ADMIN。 */
    private String role;

    /** 状态：ENABLED 或 DISABLED。 */
    private String status;

    private String email;

    private String phone;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
