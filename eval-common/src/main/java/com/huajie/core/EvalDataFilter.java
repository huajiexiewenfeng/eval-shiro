package com.huajie.core;

import com.huajie.base.BaseApiService;
import com.huajie.entity.EvalUser;
import com.huajie.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class EvalDataFilter extends BaseApiService {

    public List filterData(List data, String filterCondition, EvalUser evalUser) {
        //第一种情况 field user:userField=res:resField
        List res = new ArrayList();
        if (isMapping(filterCondition)) {
            if (isNormalMapping(filterCondition)) {
                res = filterDataMapping(data, filterCondition, evalUser);
            } else if (isSpecialMapping(filterCondition)) {
                if (isMangMapping(filterCondition)) {
                    res = filterDataMangMapping(data, filterCondition, evalUser);
                } else {
                    res = filterDataResValueMapping(data, filterCondition, evalUser);
                }
            }
        } else if (isDerive(filterCondition)) {
            res = filterDataDerive(data, filterCondition, evalUser);
        }
        return res;
    }

    //"user:fdTestevalCenterId=res:fdTestevalCenterId|res:fdTestevalCenterId=null"}
    private List filterDataResValueMapping(List data, String filterCondition, EvalUser evalUser) {
        String userField = filterCondition.substring(filterCondition.indexOf("user:") + 5, filterCondition.indexOf("="));
        String resField = filterCondition.substring(filterCondition.indexOf("res:") + 4, filterCondition.indexOf("|"));

        String resSpecialField = filterCondition.substring(StringUtil.getIndexByCharAndCnt(filterCondition, "res:", 2) + 4, StringUtil.getIndexByCharAndCnt(filterCondition, "=", 2));
        String resSpecialValue = filterCondition.substring(StringUtil.getIndexByCharAndCnt(filterCondition, "=", 2) + 1);

        String getUserMethodName = "get" + StringUtil.firstCharToUpperCase(userField);
        String getResMethodName = "get" + StringUtil.firstCharToUpperCase(resField);
        String getSpecialResMethodName = "get" + StringUtil.firstCharToUpperCase(resSpecialField);

        List resNew = (List) data.stream().filter(t -> {
                    try {
                        return String.valueOf(t.getClass().getMethod(getResMethodName).invoke(t)).equals(
                                String.valueOf(evalUser.getClass().getMethod(getUserMethodName).invoke(evalUser))) ||
                                String.valueOf(t.getClass().getMethod(getSpecialResMethodName).invoke(t)).equals(resSpecialValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                }
        ).collect(toList());

        return resNew;
    }

    private boolean isMangMapping(String filterCondition) {
        return StringUtil.getIndexByCharAndCnt(filterCondition, "user:", 2) >= 0;
    }

    //"user:fdTestevalCenterId=res:fdTestevalCenterId|user:fdTestevalCenterId=res:fdTestevalCenterId"
    private List filterDataMangMapping(List data, String filterCondition, EvalUser evalUser) {
        String userField = filterCondition.substring(filterCondition.indexOf("user:") + 5, filterCondition.indexOf("="));
        String resField = filterCondition.substring(filterCondition.indexOf("res:") + 4, filterCondition.indexOf("|"));

        String resSpecialField = filterCondition.substring(StringUtil.getIndexByCharAndCnt(filterCondition, "user:", 2) + 5,
                StringUtil.getIndexByCharAndCnt(filterCondition, "=", 2));
        String resSpecialValue = filterCondition.substring(StringUtil.getIndexByCharAndCnt(filterCondition, "res:", 2) + 4);

        String getUserMethodName = "get" + StringUtil.firstCharToUpperCase(userField);
        String getResMethodName = "get" + StringUtil.firstCharToUpperCase(resField);
        String getSpecialResMethodName = "get" + StringUtil.firstCharToUpperCase(resSpecialField);
        String getSpecialValueMethodName = "get" + StringUtil.firstCharToUpperCase(resSpecialValue);

        List resNew = (List) data.stream().filter(t -> {
                    try {
                        return String.valueOf(t.getClass().getMethod(getResMethodName).invoke(t)).equals(
                                String.valueOf(evalUser.getClass().getMethod(getUserMethodName).invoke(evalUser))) ||
                                String.valueOf(t.getClass().getMethod(getSpecialValueMethodName).invoke(t)).equals(
                                        String.valueOf(evalUser.getClass().getMethod(getSpecialResMethodName).invoke(evalUser)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                }
        ).collect(toList());

        return resNew;
    }


    //"user:userField=1->res:resField=1" "user:userField=0->res:resField=all"
    private List filterDataDerive(List data, String filterCondition, EvalUser evalUser) {
        String userField = filterCondition.substring(filterCondition.indexOf("user:") + 5, filterCondition.indexOf("="));
        String resField = filterCondition.substring(filterCondition.indexOf("res:") + 4, filterCondition.lastIndexOf("="));

        String getUserMethodName = "get" + StringUtil.firstCharToUpperCase(userField);
        String getResMethodName = "get" + StringUtil.firstCharToUpperCase(resField);

        String userValue = filterCondition.substring(filterCondition.indexOf("=") + 1, filterCondition.indexOf("->"));
        String resValue = filterCondition.substring(filterCondition.lastIndexOf("=") + 1);

        List resNew = (List) data.stream().filter(t -> {
                    try {
                        if (String.valueOf(evalUser.getClass().getMethod(getUserMethodName).invoke(evalUser)).equals(userValue)) {
                            if ("all".equals(resValue)) {
                                return true;
                            } else {
                                return String.valueOf(t.getClass().getMethod(getResMethodName).invoke(t)).equals(resValue);
                            }
                        } else {
                            if ("all".equals(userValue)) {
                                return String.valueOf(t.getClass().getMethod(getResMethodName).invoke(t)).equals(resValue);
                            } else {
                                return true;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                }
        ).collect(toList());

        return resNew;
    }

    //"user:userField=res:resField"
    private List filterDataMapping(List data, String filterCondition, EvalUser evalUser) {
        String userField = filterCondition.substring(filterCondition.indexOf("user:") + 5, filterCondition.indexOf("="));
        String resField = filterCondition.substring(filterCondition.indexOf("res:") + 4);

        String getUserMethodName = "get" + StringUtil.firstCharToUpperCase(userField);
        String getResMethodName = "get" + StringUtil.firstCharToUpperCase(resField);

        List resNew = (List) data.stream().filter(t -> {
                    try {
                        return String.valueOf(t.getClass().getMethod(getResMethodName).invoke(t)).equals(
                                String.valueOf(evalUser.getClass().getMethod(getUserMethodName).invoke(evalUser)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                }
        ).collect(toList());

        return resNew;
    }

    private boolean isMapping(String filterCondition) {
        return !isDerive(filterCondition);
    }

    private boolean isNormalMapping(String filterCondition) {
        return !isSpecialMapping(filterCondition);
    }

    private boolean isDerive(String filterCondition) {
        return filterCondition.indexOf("->") > 0;
    }

    private boolean isSpecialMapping(String filterCondition) {
        return filterCondition.indexOf("|") > 0;
    }
}
