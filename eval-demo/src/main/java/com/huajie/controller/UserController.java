package com.huajie.controller;

import com.huajie.base.BaseApiService;
import com.huajie.base.ResponseBase;
import com.huajie.core.annotation.ExtRequiresPermissions;
import com.huajie.entity.User;
import com.huajie.service.IUserService;
import com.huajie.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController extends BaseApiService {

    @Autowired
    private IUserService iUserService;
    
    @PostMapping("/query1")
    @ExtRequiresPermissions(filterCondition = {"user:orgId=res:orgId"})
    @ResponseBody
    public PageUtils query1(HttpServletRequest request) {
        List<User> listUser = iUserService.listUser();
        return new PageUtils<>(listUser);
    }


    @PostMapping("/query2")
    @ExtRequiresPermissions(filterCondition = {"user:orgId=res:orgId","user:level=res:level"})
    @ResponseBody
    public PageUtils query2(HttpServletRequest request) {
        List<User> listUser = iUserService.listUser();
        return new PageUtils<>(listUser);
    }

}
