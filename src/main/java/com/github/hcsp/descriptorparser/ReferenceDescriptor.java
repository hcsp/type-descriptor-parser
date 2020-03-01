package com.github.hcsp.descriptorparser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 代表引用类型的描述符号
 */
public class ReferenceDescriptor implements TypeDescriptor {
    /**
     * 全限定类名，如java.lang.Object
     */
    private String fqcn;
    private String descriptor;
    private static final Pattern pattern = Pattern.compile("L([\\w\\/\\$]*);");

    public ReferenceDescriptor(String descriptor) {
        Matcher matcher = pattern.matcher(descriptor);
        if (matcher.find()) {
            String group = matcher.group(1);
            String[] split = group.split("\\/");
            fqcn = String.join(".", split);
        }
    }

    @Override
    public String getName() {
        return fqcn;
    }

    @Override
    public String getDescriptor() {
        return descriptor;
    }
}
