package com.huajie.service.impl;

import com.huajie.entity.User;
import com.huajie.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserImpl implements IUserService {
    @Override
    public List<User> listUser() {
        List<User> res = new ArrayList<>();
        //机构1
        res.add(new User("用户1","16","1","1"));
        res.add(new User("用户2","16","1","2"));
        res.add(new User("用户3","16","1","2"));

        res.add(new User("用户4","16","1","1"));
        res.add(new User("用户5","16","1","2"));

        //机构2
        res.add(new User("用户6","16","2","1"));
        res.add(new User("用户7","16","2","2"));
        res.add(new User("用户8","16","2","2"));

        res.add(new User("用户9","16","2","1"));
        res.add(new User("用户10","16","2","2"));

        return res;
    }
}
