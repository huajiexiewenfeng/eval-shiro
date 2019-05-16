package com.huajie.core.aop;

import java.lang.reflect.Method;

public interface MethodInvocation {

    Object proceed() throws Throwable;

    Method getMethod();

    Object[] getArguments();

    Object getThis();


}