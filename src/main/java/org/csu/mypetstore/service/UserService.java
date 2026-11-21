package org.csu.mypetstore.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.csu.mypetstore.dto.UserReadResult;
import org.csu.mypetstore.entity.User;
import org.csu.mypetstore.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 用户查询服务。只读取展示字段，不加载密码散列；数据访问异常由调用方处理。
 */
@Service
public class UserService {

    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 按登录名查询用户展示信息。
     * 用户名唯一约束由数据库保证，因此最多返回一条记录。
     *
     * @param username 登录名，与表字段同样不区分大小写比较
     * @return 命中时返回用户展示信息，不存在时返回空 Optional
     */
    public Optional<UserReadResult> findByUsername(String username) {
        // select 限定列清单，SQL 只查这五列，password_hash 不出现在结果集中
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery()
                .select(User::getId, User::getUsername, User::getDisplayName,
                        User::getRole, User::getStatus)
                .eq(User::getUsername, username);

        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            return Optional.empty();
        }
        return Optional.of(new UserReadResult(
                user.getId(),
                user.getUsername(),
                user.getDisplayName(),
                user.getRole(),
                user.getStatus()));
    }
}
