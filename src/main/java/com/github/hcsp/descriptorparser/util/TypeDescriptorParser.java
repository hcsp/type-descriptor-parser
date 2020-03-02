package com.github.hcsp.descriptorparser.util;

import com.github.hcsp.descriptorparser.ArrayDescriptor;
import com.github.hcsp.descriptorparser.PrimitiveTypeDescriptor;
import com.github.hcsp.descriptorparser.ReferenceDescriptor;
import com.github.hcsp.descriptorparser.TypeDescriptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 利用正则表达式对类型描述符进行判断匹配的工具类
 *
 * @author BirdSnail
 * @date 2020/3/1
 */
public class TypeDescriptorParser {

    /**
     * 原生类型的regex
     */
    public final static Pattern primitive = Pattern.compile("[BCDFIJSZV]");

    /**
     * 数组的维度
     */
    public final static Pattern dimension = Pattern.compile("\\[");

    /**
     * 提取方法的参数列表
     */
    public final static Pattern params = Pattern.compile("(?<=\\()[^\\)]+");

    public static boolean isPrimitive(String descriptor) {
        return primitive.matcher(descriptor).matches();
    }

    public static int getDimensionFromArrayDescriptor(String descriptor) {
        int count = 0;
        Matcher matcher = dimension.matcher(descriptor);
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    /**
     * 从方法签名的描述符中获取关于参数列表的描述
     * @param descriptor method descriptor
     * @return 原始的参数列表描述符
     */
    public static String getParameterListFromMethodDescriptor(String descriptor) {
        Matcher matcher = params.matcher(descriptor);
        return matcher.find() ? matcher.group() : "";
    }

    /**
     * 获取数组的原生类型描述符
     *
     * @param descriptor 数组描述符
     * @return TypeDescriptor
     */
    public static TypeDescriptor getRowTypeFromArrayDescriptor(String descriptor) {
        String rowTypeDescriptor = descriptor.replace("[", "");
        if (isPrimitive(rowTypeDescriptor)) {
            return PrimitiveTypeDescriptor.of(rowTypeDescriptor);
        } else {
            return new ReferenceDescriptor(rowTypeDescriptor);
        }
    }

    /**
     * 返回人类可读的数组名
     * @param rawType {@link TypeDescriptor}
     * @param dimension 数组维度
     * @return array name
     */
    public static String getArrayName(TypeDescriptor rawType, int dimension) {
        StringBuilder sb = new StringBuilder();
        sb.append(rawType.getName());
        for (int i = 0; i < dimension; i++) {
            sb.append("[]");
        }
        return sb.toString();
    }

    /**
     * 分割沾黏在一起的参数描述符
     * @param parameterDescriptor descriptor list
     * @return {@link TypeDescriptor} 集合
     */
    public static List<TypeDescriptor> parseParameterDescriptor(String parameterDescriptor) {
        List<TypeDescriptor> result = new ArrayList<>();
        char[] chars = parameterDescriptor.toCharArray();

        for (int start = 0, end = 0; end < chars.length;) {
            String s = String.valueOf(chars[start]);
            if ("[".equals(s)) { // 如果是数组
                String next = String.valueOf(chars[++end]);
                if ("[".equals(next)) {
                    continue;
                }

                if (isPrimitive(next)) {
                    result.add(new ArrayDescriptor(parameterDescriptor.substring(start, ++end)));
                    start = end;
                }else {
                    result.add(new ArrayDescriptor(parameterDescriptor.substring(start)));
                    break;
                }
            }else if (isPrimitive(s)){
                result.add(PrimitiveTypeDescriptor.of(parameterDescriptor.substring(start, ++end)));
                start = end;
            }else {
                result.add(new ReferenceDescriptor(parameterDescriptor.substring(start)));
                break;
            }
        }
        return result;
    }

    /**
     * 获取返回类型的描述符
     * @param descriptor desc
     * @return PrimitiveTypeDescriptor/ReferenceDescriptor/PrimitiveTypeDescriptor
     */
    public static TypeDescriptor getReturnTypeDescriptor(String descriptor) {
        String returnType = descriptor.split("\\)")[1];
        if (returnType.startsWith("[")){
            return new ArrayDescriptor(returnType.trim());
        } else if (isPrimitive(returnType)) {
            return PrimitiveTypeDescriptor.of(returnType);
        }else {
            return new ReferenceDescriptor(returnType);
        }
    }
}
