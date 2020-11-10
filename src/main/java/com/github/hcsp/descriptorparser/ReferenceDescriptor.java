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

    public ReferenceDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    @Override
    public String getName() {
        Pattern pattern = Pattern.compile("L((.*/)?.*);");
        Matcher matcher = pattern.matcher(descriptor);
        if (matcher.find()) {
            String group = matcher.group(1);
            fqcn = group.replace("/", ".");
        }
        return fqcn;
    }

    @Override
    public String getDescriptor() {
        return descriptor;
    }
}
