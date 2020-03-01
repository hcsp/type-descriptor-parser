package com.github.hcsp.descriptorparser;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DescriptorUtils {

    public static String getStrByRegex(String regex, String descriptor) {
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(descriptor);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    /**
     * 传入类型，将其转换为日常显示格式
     * @param index 当前字符在字符串中的索引
     * @param baseType JVM基本类型
     * @param descriptor 方法描述符
     * @return
     */
    public static HashMap<String, Object> getFqcnByDataType(int index, String baseType, String descriptor) {
        HashMap<String, Object> fqcnAndLength = new HashMap<>(2);
        String fqcn = null;
        //引用类型
        if ("L".equals(baseType)) {
            fqcn = getStrByRegex("L(.*?);", descriptor);
            fqcn = fqcn.replaceAll("/", ".");
            fqcnAndLength.put("fqcn", fqcn);
            fqcnAndLength.put("length", fqcn.length() + 2);
            //数组类型
        } else if ("[".equals(baseType)) {
            int dimension = 1;
            while ('[' == descriptor.charAt(++index)) {
                dimension++;
            }
            fqcnAndLength = getFqcnByDataType(0, String.valueOf(descriptor.charAt(index)), descriptor);
            fqcn = fqcnAndLength.get("fqcn").toString();
            for (int x = 0; x < dimension; x++) {
                fqcn = fqcn + "[]";
            }
            fqcnAndLength.put("fqcn", fqcn);
            fqcnAndLength.put("length", dimension + Integer.valueOf(fqcnAndLength.get("length").toString()));
            //基本数据类型
        } else {
            for (PrimitiveTypeDescriptor descType : PrimitiveTypeDescriptor.values()) {
                if (descType.getDescriptor().equals(baseType)) {
                    fqcn = descType.name().toLowerCase();
                    fqcnAndLength.put("fqcn", fqcn);
                    fqcnAndLength.put("length", 0);
                    break;
                }
            }
        }
        return fqcnAndLength;
    }
}
