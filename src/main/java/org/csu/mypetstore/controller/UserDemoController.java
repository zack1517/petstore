package org.csu.mypetstore.controller;

import org.csu.mypetstore.dto.UserReadResult;
import org.csu.mypetstore.service.UserService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

/**
 * store_user 查询演示接口，仅在 dev 环境注册。
 * 只负责参数接收与状态码选择，查询逻辑留在 UserService。
 */
@Controller
@Profile("dev")
public class UserDemoController {

    private final UserService userService;

    public UserDemoController(UserService userService) {
        this.userService = userService;
    }

    /**
     * GET /demo/user?username=alice：命中返回 200 与用户展示信息 JSON，
     * 用户不存在返回 404；数据访问异常不在此拦截，交由容器按 5xx 处理。
     */
    @GetMapping("/demo/user")
    @ResponseBody
    public ResponseEntity<UserReadResult> findUser(
            @RequestParam(name = "username", defaultValue = "alice") String username) {

        Optional<UserReadResult> user = userService.findByUsername(username);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(user.get());
    }

    /**
     * GET /demo/user/page?username=alice：命中时把消息与用户放入 Model，
     * 返回视图名 demo/user 交由 Thymeleaf 渲染；用户不存在返回 404。
     * 本方法不标注 @ResponseBody，因此返回的字符串按视图名解析而非写入响应体。
     */
    @GetMapping("/demo/user/page")
    public String findUserPage(
            @RequestParam(name = "username", defaultValue = "alice") String username,
            Model model) {

        Optional<UserReadResult> user = userService.findByUsername(username);
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "用户不存在：" + username);
        }
        model.addAttribute("message", "Thymeleaf 初始化成功！");
        model.addAttribute("user", user.get());
        return "demo/user";
    }
}
