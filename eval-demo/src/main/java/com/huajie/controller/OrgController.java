package com.huajie.controller;

import com.huajie.base.BaseApiService;
import com.huajie.base.ResponseBase;
import com.huajie.core.annotation.ExtRequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/org")
public class OrgController extends BaseApiService {

    @GetMapping(value = "/index")
    public String org() {
        return "org";
    }
    
    @PostMapping("/query")
    @ExtRequiresPermissions("org:query")
    @ResponseBody
    public ResponseBase query(HttpServletRequest request) {
        return setResultSuccess("查询成功");
    }

    @PostMapping("/add")
    @ExtRequiresPermissions("org:add")
    @ResponseBody
    public ResponseBase add(HttpServletRequest request) {
        return setResultSuccess("添加成功");
    }

    @PostMapping("/update")
    @ExtRequiresPermissions("org:update")
    @ResponseBody
    public ResponseBase update(HttpServletRequest request) {
        return setResultSuccess("更新成功");
    }

    @PostMapping("/delete")
    @ExtRequiresPermissions("org:delete")
    @ResponseBody
    public ResponseBase delete(HttpServletRequest request) {
        return setResultSuccess("删除成功");
    }
}
