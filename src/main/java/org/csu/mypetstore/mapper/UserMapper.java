package org.csu.mypetstore.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.csu.mypetstore.entity.User;

/**
 * store_user 的通用 CRUD 入口，继承 BaseMapper 后无需编写基础增删改查语句。
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
