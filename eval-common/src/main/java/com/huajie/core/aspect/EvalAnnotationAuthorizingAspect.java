package com.huajie.core.aspect;

import com.huajie.base.ResponseBase;
import com.huajie.core.EvalDataFilter;
import com.huajie.core.annotation.ExtRequiresPermissions;
import com.huajie.core.aop.BeforeAdviceMethodInvocationAdapter;
import com.huajie.core.subject.DefaultSecurityManager;
import com.huajie.core.subject.Subject;
import com.huajie.entity.EvalUser;
import com.huajie.utils.PageUtils;
import com.huajie.utils.SessionUtils;
import com.huajie.utils.StringUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
@Aspect()
@Order(20)
public class EvalAnnotationAuthorizingAspect extends EvalDataFilter {

    @Autowired
    private DefaultSecurityManager defaultSecurityManager;

    @Pointcut("@annotation(com.huajie.core.annotation.ExtRequiresPermissions)")
    void anyShiroAnnotatedMethodCall() {
    }

    @Around("anyShiroAnnotatedMethodCall()")
    public Object executeAnnotatedMethod(ProceedingJoinPoint aJoinPoint) throws Throwable {
        BeforeAdviceMethodInvocationAdapter mi = BeforeAdviceMethodInvocationAdapter.createFrom(aJoinPoint);
        Method method = mi.getMethod();
        Object[] args = mi.getArguments();
        Object res = null;
        if (method.isAnnotationPresent(ExtRequiresPermissions.class)) {
            ExtRequiresPermissions annotation = method.getAnnotation(ExtRequiresPermissions.class);
            String perm = annotation.value();
            String[] filterCondition = annotation.filterCondition();
            EvalUser evalUser = getEvalUser(args);
            if (null == evalUser) {
                return setResultFail("权限验证失败");
            }
            Subject subject = getSubject(evalUser);
            boolean flag = checkPermission(perm, subject);
            if (!flag) {
                return setResultFail("权限验证失败");
            }
            res = aJoinPoint.proceed();
            if (filterCondition.length > 0) {
                return filterResult(filterCondition, res, evalUser);
            } else {
                return res;
            }
        }
        return res;
    }

    /**
     * user表示当前用户 res表示返回结果
     * 用user中的type字段与res中的type字段进行对比 过滤需要的数据
     * -> 表示推导关系 由前面的条件推导后面的过滤条件
     * = 表示匹配关系 过滤前面的字段和res结果中的字段进行对比
     * | 多条件兼容
     * 1. res结果中带有value(固定值)的过滤条件 配合‘=’使用
     * 2. 多个条件 res和user中的字段进行匹配
     */
    //filterCondition = {"user:userField=1->res:resField=1","user:userField=0->res:resField=all","user:userField=res:resField"}
    //user:userField=res:resField|res:resSpecialField=1
    //user:userid=res:userId|res:creatorId
    private Object filterResult(String[] filterConditions, Object res, EvalUser evalUser) throws Throwable {
        List data = getData(res);
        List<String> filterCondition = Arrays.asList(filterConditions);
        for (int i = 0; i < filterCondition.size(); i++) {
            data = filterData(data, filterCondition.get(i).trim(), evalUser);
        }
        return returnData(res, data);
    }

    private Object returnData(Object res, List resNew) {
        if (res instanceof PageUtils) {
            PageUtils returnList = new PageUtils(resNew);
            return returnList;
        } else if (res instanceof List) {
            return resNew;
        } else {
            return setResultSuccess(resNew);
        }
    }

    private List getData(Object res) {
        List data = new ArrayList<>();
        if (res instanceof PageUtils) {
            data = ((PageUtils) res).getRows();
        } else if (res instanceof ResponseBase) {
            data = (List) ((ResponseBase) res).getData();
        } else if (res instanceof List) {
            data = (List) res;
        }
        return data;
    }

    private boolean checkPermission(String perm, Subject subject) {
        List<String> allParts = subject.getPermissions();
        if (StringUtil.isEmpty(perm)) {
            return true;
        }
        if (null == allParts || allParts.size() <= 0) {
            return false;
        }
        boolean resFlag = false;
        for (int i = 0; i < allParts.size(); i++) {
            String part = allParts.get(i);
            if (part.equals(perm)) {
                resFlag = true;
            }
        }
        return resFlag;
    }

    private EvalUser getEvalUser(Object[] args) {
        List listArg = Arrays.stream(args).filter(arg -> arg instanceof HttpServletRequest).collect(toList());
        if (null == listArg || listArg.size() <= 0) {
            throw new RuntimeException("接口参数中没有HttpServletRequest");
        }
        EvalUser user = SessionUtils.getSessionEvalUser((HttpServletRequest) (listArg.get(0)));
        return user;
    }

    private Subject getSubject(EvalUser user) {
        Subject subject = defaultSecurityManager.getSubject(user.getUserid());
        return subject;
    }

}