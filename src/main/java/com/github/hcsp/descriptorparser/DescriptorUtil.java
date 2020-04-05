package com.github.hcsp.descriptorparser;

public class DescriptorUtil {
    static String getClassName(String descriptor) {
        switch (descriptor.charAt(0)) {
            case 'B':
                descriptor = "byte";
                break;
            case 'C':
                descriptor = "char";
                break;
            case 'D':
                descriptor = "double";
                break;
            case 'F':
                descriptor = "float";
                break;
            case 'I':
                descriptor = "int";
                break;
            case 'J':
                descriptor = "long";
                break;
            case 'L':
                descriptor = descriptor.substring(1).replace("/", ".").replace(";", "");
                break;
            case 'S':
                descriptor = "short";
                break;
            case 'Z':
                descriptor = "boolean";
                break;
            case 'V':
                descriptor = "void";
                break;
            default:
                return null;
        }
        return descriptor;

    }
}
