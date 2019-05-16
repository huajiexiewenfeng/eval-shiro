package com.huajie.core.subject;

public interface ExtSecurityManager {

    void login(Subject subject);

    void logout(Subject subject);

    Subject getSubject(String keyId);
}