package com.huajie.core.subject;

import com.huajie.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultSecurityManager implements ExtSecurityManager {

    private static int redisExpireMinite = 1440;

    @Autowired
    private RedisUtil redisUtil;

    private static final String SECURITY_KEY = "eval_";

    @Override
    public void login(Subject subject) {
        redisUtil.set(SECURITY_KEY + subject.getKeyId(), subject,redisExpireMinite*60);
    }

    @Override
    public void logout(Subject subject) {
        redisUtil.del(SECURITY_KEY + subject.getKeyId());
    }

    @Override
    public Subject getSubject(String keyId) {
        return (Subject) redisUtil.get(SECURITY_KEY + keyId);
    }
}
