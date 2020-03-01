package com.github.hcsp.descriptorparser;

import java.util.ArrayList;
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
     * 先分离 参数和返回值
     * 第一个group 参数list
     * 第二个group 返回值
     */
    public static final Pattern REGEX = Pattern.compile("^\\((.*)\\)(.*)$");
    /**
     * [BCDFIJSZ] 基本类型  或 等价于 （B｜C｜D...)
     * (L.*?;)    引用类型 特征是 L xxx ;
     *  \[+  数组
     */
    public static final Pattern PARAMS_REGEX = Pattern.compile("([BCDFIJSZ])|(L.*?;)|\\[+([BCDFIJSZ])|\\[+(L.*?;)");

    public MethodDescriptor(String descriptor) {
        this.descriptor = descriptor;

        Matcher matcher = REGEX.matcher(descriptor);
        if (matcher.find()) {
            String group = matcher.group(1);
            Matcher matcher1 = PARAMS_REGEX.matcher(group);
            while (matcher1.find()){
                paramTypes.add(TypeDescriptor.parse(matcher1.group()));
            }
            returnType = TypeDescriptor.parse(matcher.group(2));

            StringBuilder sb = new StringBuilder(returnType.getName());
            sb.append(" (");
            for ( int i = 0; i < paramTypes.size(); i++) {
                sb.append(paramTypes.get(i).getName());
                if (i != (paramTypes.size() - 1)) {
                    sb.append(", ");
                }
            }
            sb.append(")");
            name = sb.toString();
        }
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

