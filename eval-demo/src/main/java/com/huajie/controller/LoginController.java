package com.huajie.controller;

import com.huajie.base.BaseApiService;
import com.huajie.base.ResponseBase;
import com.huajie.core.subject.ExtSecurityManager;
import com.huajie.core.subject.Subject;
import com.huajie.entity.EvalUser;
import com.huajie.service.ILoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping
public class LoginController extends BaseApiService {

    @Autowired
    protected ExtSecurityManager securityManager;

    @Autowired
    protected ILoginService iLoginService;

    /**
     * 首页
     */
    @GetMapping(value = "/")
    public String defalut() {
        return home();
    }

    @GetMapping(value = "/login")
    public String home() {
        return "login";
    }

    @PostMapping("/loginValida")
    @ResponseBody
    public ResponseBase valida(HttpServletRequest request, String username, String password) {
        HttpSession session = request.getSession();
        EvalUser user = this.ckeckUser(username, password);
        if (user == null) {
            return setResultFail("用户不存在");
        }
        session.setAttribute("user", user);

        List<String> permissons = iLoginService.getPermissonsById(user.getUserid());
        Subject subject = new Subject();
        subject.setPermissions(permissons);
        subject.setKeyId(user.getUserid());
        securityManager.login(subject);

        return setResultSuccess("成功");
    }

    private EvalUser ckeckUser(String username, String password) {
        if ("eval".equals(username)) {
            EvalUser user = new EvalUser();
            user.setUserid("001");
            user.setUsername(username);
            user.setField1("1");//过滤字段1  可以是部门
            user.setField2("2");//过滤字段2  可以是职位
            return user;
        } else {
            return null;
        }
    }
}
