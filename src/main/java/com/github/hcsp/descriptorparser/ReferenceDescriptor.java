package com.github.hcsp.descriptorparser;

import java.util.regex.Matcher;

import static com.github.hcsp.descriptorparser.ArrayDescriptor.getMatcher;

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
        String regex = "L(.+(/)?);";
        Matcher matcher = getMatcher(descriptor, regex);
        while (matcher.find()) {
            this.descriptor = matcher.group(0);
            this.fqcn = matcher.group(1).replaceAll("/", ".");
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
