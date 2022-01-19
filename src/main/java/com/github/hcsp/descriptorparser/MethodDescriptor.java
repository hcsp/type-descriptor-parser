package com.github.hcsp.descriptorparser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 代表方法的描述符，如给定方法描述符(IDLjava/lang/Thread;)Ljava/lang/Object;
 * paramTypes应该是对应的参数类型
 * returnType应该是返回值类型
 * name应该是人类可读的格式 java.lang.Object (int i, double d, java.lang.Thread t)
 */
public class MethodDescriptor implements TypeDescriptor {
    private List<TypeDescriptor> paramTypes = new ArrayList<>();
    private TypeDescriptor returnType;
    private String descriptor;
    private String name;

    /**
     * 思路
     * 1. 正则解析出小括号内的  入参
     * 2. 正则解析出小括号右边的 返回值
     * 3. 将返回值转换格式 拼接字符串 在一边等着
     * 4. 将入参解析（遍历每一个字符）
     *      - 引用类型 --> 转换格式，并跳过长度+2个索引
     *      - 数组类型 --> 遍历查看下一个字符是不是数组，得出n维数组，跳过n个索引，格式转换
     *      - 基本类型 --> 转换格式
     *
     * @param descriptor
     */
    public MethodDescriptor(String descriptor) {

        StringBuilder builder = new StringBuilder();
        String params = "";
        String returnParam = "";

        String paramRegex = "^\\((.*?)\\)";
        String fqcnRegex = "L(.*?);";
        String returnRegex = "\\)(.*)";

        //获取返回值的完全限定名
        returnParam = DescriptorUtils.getStrByRegex(returnRegex,descriptor);
        String type = String.valueOf(returnParam.charAt(0));
        HashMap returnFqcn = DescriptorUtils.getFqcnByDataType(0, type, returnParam);
        builder.append(returnFqcn.get("fqcn")).append(" (");

        //Ljava/lang/Object;
        params = DescriptorUtils.getStrByRegex(paramRegex,descriptor);
        for (int index = 0; index < params.length(); index++){
            type = String.valueOf(params.charAt(index));
            HashMap fqcn = DescriptorUtils.getFqcnByDataType(index, type, params);

            if (index != 0){
                builder.append(", ");
            }
            builder.append(fqcn.get("fqcn"));
            index = index + Integer.valueOf(fqcn.get("length").toString());

        }

        builder.append(")");

        this.name = builder.toString();

    }


    public List<TypeDescriptor> getParamTypes() {
        return paramTypes;
    }

    public TypeDescriptor getReturnType() {
        return returnType;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescriptor() {
        return descriptor;
    }

}

