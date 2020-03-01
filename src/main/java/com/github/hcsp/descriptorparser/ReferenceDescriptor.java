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

    public static final Pattern REFERENCE_REGEX = Pattern.compile("^L?(.*?);?$");


    public ReferenceDescriptor(String descriptor) {
        this.descriptor = descriptor;
        Matcher matcher = REFERENCE_REGEX.matcher(descriptor);
        if (matcher.find()) {
            fqcn = matcher.group(1).replaceAll("/", ".");
        } else {
            throw new RuntimeException(" no match");
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
