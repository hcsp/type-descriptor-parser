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
    private static final Pattern descriptorPattern = Pattern.compile("^L([\\w/\\\\$]+);");

    public ReferenceDescriptor(String descriptor) {
        this.descriptor = descriptor;
        this.parseDescriptor(descriptor);
    }

    public static ReferenceDescriptor of(String descriptor) {
        return new ReferenceDescriptor(descriptor);
    }

    public static boolean isReference(String descriptor) {
        return descriptorPattern.matcher(descriptor).find();
    }

    @Override
    public String getName() {
        return fqcn;
    }

    @Override
    public String getDescriptor() {
        return descriptor;
    }

    private void parseDescriptor(String descriptor) {
        Matcher matcher = descriptorPattern.matcher(descriptor);
        if (matcher.find()) {
            this.fqcn = matcher.group(1).replaceAll("/", ".");
        }
    }
}
