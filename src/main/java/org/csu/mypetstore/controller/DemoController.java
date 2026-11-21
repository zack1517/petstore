package org.csu.mypetstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用于验证 Spring MVC 是否初始化成功的示例控制器
 */
@Controller
public class DemoController {

    /**
     * GET /demo：直接返回纯文本，响应类型明确为 UTF-8 编码的文本
     */
    @GetMapping(value = "/demo", produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String demo() {
        return "Spring MVC 初始化成功！";
    }
}
