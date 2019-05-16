package com.huajie.core.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.AdviceSignature;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

public class BeforeAdviceMethodInvocationAdapter implements MethodInvocation {

    private Object _object;
    private Method _method;
    private Object[] _arguments;

    /**
     * Factory method that creates a new {@link BeforeAdviceMethodInvocationAdapter} instance
     * using the AspectJ {@link JoinPoint} provided. If the joint point passed in is not
     * a method joint point, this method throws an {@link IllegalArgumentException}.
     *
     * @param aJoinPoint The AspectJ {@link JoinPoint} to use to adapt the advice.
     * @return The created instance.
     * @throws IllegalArgumentException If the join point passed in does not involve a method call.
     */
    public static BeforeAdviceMethodInvocationAdapter createFrom(JoinPoint aJoinPoint) {
        if (aJoinPoint.getSignature() instanceof MethodSignature) {
            return new BeforeAdviceMethodInvocationAdapter(aJoinPoint.getThis(),
                    ((MethodSignature) aJoinPoint.getSignature()).getMethod(),
                    aJoinPoint.getArgs());

        } else if (aJoinPoint.getSignature() instanceof AdviceSignature) {
            return new BeforeAdviceMethodInvocationAdapter(aJoinPoint.getThis(),
                    ((AdviceSignature) aJoinPoint.getSignature()).getAdvice(),
                    aJoinPoint.getArgs());

        } else {
            throw new IllegalArgumentException("The joint point signature is invalid: expected a MethodSignature or an AdviceSignature but was " + aJoinPoint.getSignature());
        }
    }

    /**
     * Creates a new {@link BeforeAdviceMethodInvocationAdapter} instance.
     *
     * @param aMethod       The method to invoke.
     * @param someArguments The arguments of the method invocation.
     */
    public BeforeAdviceMethodInvocationAdapter(Object anObject, Method aMethod, Object[] someArguments) {
        _object = anObject;
        _method = aMethod;
        _arguments = someArguments;
    }

    public Object[] getArguments() {
        return _arguments;
    }

    public Method getMethod() {
        return _method;
    }

    public Object proceed() throws Throwable {
        return null;
    }

    public Object getThis() {
        return _object;
    }
}