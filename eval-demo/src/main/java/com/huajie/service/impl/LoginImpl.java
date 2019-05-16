package com.huajie.service.impl;

import com.huajie.service.ILoginService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoginImpl implements ILoginService {
    /**
     * 这里获取该用户的权限
     * */
    @Override
    public List<String> getPermissonsById(String userid) {

        List<String> listPermissons = new ArrayList();
        //增加机构模块的增删改查权限
        listPermissons.add("org:add");
        listPermissons.add("org:delete");
//        listPermissons.add("org:update");
//        listPermissons.add("org:query");

        return listPermissons;
    }
}
