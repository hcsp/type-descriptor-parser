package com.github.hcsp.descriptorparser;

class TypeDescriptorUtil {
    static TypeDescriptor getTypeDescriptor(String descriptor) {
        if (PrimitiveTypeDescriptor.isPrimitive(descriptor)) {
            return PrimitiveTypeDescriptor.of(descriptor);
        }

        if (ReferenceDescriptor.isReference(descriptor)) {
            return new ReferenceDescriptor(descriptor);
        }

        if (MethodDescriptor.isMethod(descriptor)) {
            return new MethodDescriptor(descriptor);
        }

        return new ArrayDescriptor(descriptor);

    }
}
