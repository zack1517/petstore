package org.csu.mypetstore.service;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.csu.mypetstore.dto.UserReadResult;
import org.csu.mypetstore.entity.User;
import org.csu.mypetstore.mapper.UserMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.dao.DataAccessResourceFailureException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * UserService 的纯单元测试：Mapper 由 Mockito 模拟，Service 为真实对象，
 * 不启动 Spring 容器、不连接数据库。
 */
class UserServiceTest {

    private UserMapper userMapper;
    private UserService userService;

    /**
     * LambdaQueryWrapper 在 select 阶段就依赖 TableInfo 缓存，纯单元测试未启动 MyBatis，
     * 因此仅按实体注解补建 User 的表元数据；不涉及数据源、Mapper 代理或 SQL 执行。
     */
    @BeforeAll
    static void initTableInfoCache() {
        MapperBuilderAssistant assistant = new MapperBuilderAssistant(new MybatisConfiguration(), "");
        assistant.setCurrentNamespace(UserMapper.class.getName());
        TableInfoHelper.initTableInfo(assistant, User.class);
    }

    @BeforeEach
    void setUp() {
        userMapper = Mockito.mock(UserMapper.class);
        userService = new UserService(userMapper);
    }

    /**
     * 命中用户时，只把五个展示字段搬到 UserReadResult，字段顺序与内容不得错位。
     */
    @Test
    void findByUsername_returnsMappedUserReadResult_whenMapperFindsUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("alice");
        user.setDisplayName("林同学");
        user.setRole("CUSTOMER");
        user.setStatus("ENABLED");
        when(userMapper.selectOne(any())).thenReturn(user);

        Optional<UserReadResult> result = userService.findByUsername("alice");

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(
                new UserReadResult(1L, "alice", "林同学", "CUSTOMER", "ENABLED"));
    }

    /**
     * Mapper 查不到记录时返回 null，Service 应转为空 Optional 而不是 NPE。
     */
    @Test
    void findByUsername_returnsEmptyOptional_whenMapperFindsNothing() {
        when(userMapper.selectOne(any())).thenReturn(null);

        Optional<UserReadResult> result = userService.findByUsername("nobody");

        assertThat(result).isEmpty();
    }

    /**
     * 数据访问失败时必须原样抛出同一个异常实例，Service 不吞异常、不重新包装。
     */
    @Test
    void findByUsername_propagatesSameException_whenDataSourceUnavailable() {
        DataAccessResourceFailureException failure =
                new DataAccessResourceFailureException("simulated: database unavailable");
        when(userMapper.selectOne(any())).thenThrow(failure);

        assertThatThrownBy(() -> userService.findByUsername("alice"))
                .isSameAs(failure);
    }
}
