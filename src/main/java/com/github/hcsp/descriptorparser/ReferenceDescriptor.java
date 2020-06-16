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
    private static final Pattern PATTERN = Pattern.compile("^L((?:[a-z][a-zA-Z0-9_]*/)*[$_a-zA-Z][$_a-zA-Z0-9]*);$");

    public ReferenceDescriptor(String descriptor) {
        this.descriptor = descriptor;
        Matcher matcher = PATTERN.matcher(descriptor);
        if (matcher.find()) {
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

    public static boolean isReference(String descriptor) {
        return PATTERN.matcher(descriptor).find();
    }
}
