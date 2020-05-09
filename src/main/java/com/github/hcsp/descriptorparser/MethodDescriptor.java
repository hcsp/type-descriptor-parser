package com.github.hcsp.descriptorparser;

import java.util.ArrayList;
import java.util.List;

import static com.github.hcsp.descriptorparser.TypeDescriptor.buildEnd;
import static com.github.hcsp.descriptorparser.TypeDescriptor.getFullType;

/**
 * 代表方法的描述符，如给定方法描述符(IDLjava/lang/Thread;)Ljava/lang/Object;
 * paramTypes应该是对应的参数类型
 * returnType应该是返回值类型
 * name应该是人类可读的格式 java.lang.Object (int i, double d, java.lang.Thread t)
 */
public class MethodDescriptor implements TypeDescriptor {
    private List<String> paramTypes = new ArrayList<>();
    private String returnType;
    private String descriptor;
    private String name;

    public MethodDescriptor(String descriptor) {
        String[] split = descriptor.split("\\)");
        setReturnType(split[1]);
        String param = split[0].replace("(", "");
        if (!param.isEmpty()) {
            boolean flag = false;
            Integer dimensionCount = 0;
            String dimension = "";
            StringBuilder temp = new StringBuilder();
            for (String str : param.split("")) {
                String paramType = getFullType(str);
                if (str.equals("[")) {
                    dimensionCount++;
                } else if (dimensionCount != 0) {
                    dimension = buildEnd(dimensionCount);
                    dimensionCount = 0;
                }
                if (paramType != null) {
                    paramTypes.add(paramType + dimension);
                    dimension = "";
                }
                if (str.equals("L")) {
                    flag = true;
                    continue;
                } else if (str.equals(";")) {
                    paramTypes.add(temp.toString().replaceAll("/", ".") + dimension);
                    flag = false;
                    dimension = "";
                }
                if (flag) {
                    temp.append(str);
                }
            }
        }

        name = returnType + " " + getParamTypes();
    }

    private void setReturnType(String descriptor) {
        String typeFlag = descriptor.replace("[", "").substring(0, 1);
        if (typeFlag.equals("L")) {
            returnType = descriptor
                    .replace("[", "")
                    .replace("L", "")
                    .replace(";", "")
                    .replaceAll("/", ".");
        } else {
            returnType = getFullType(typeFlag);
        }
    }


    /**
     * 返回可读参数列表
     */
    public String getParamTypes() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        paramTypes.forEach(param -> {
            sb.append(param);
            sb.append(", ");
        });
        if (!paramTypes.isEmpty() && paramTypes.size() > 1) {
            sb.deleteCharAt(sb.length() - 1);
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append(")");
        return sb.toString();
    }

    public String getReturnType() {
        return returnType;
    }

    /**
     * 返回人类可读的类型名，如int或者java.lang.Object[]
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * 返回描述符的原始格式，如[I或者Ljava/lang/Object;
     */
    @Override
    public String getDescriptor() {
        return descriptor;
    }
}

