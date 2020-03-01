package com.github.hcsp.descriptorparser;

/**
 * 数组类型的描述符，如输入[[Ljava/lang/Object;
 * 得到的name是java.lang.Object[][]
 * dimension为2（二维数组）
 * rawType代表无数组的原始类型
 */
public class ArrayDescriptor implements TypeDescriptor {
    // [ -> 1
    // [[ -> 2
    private String name;
    private String descriptor;
    private int dimension;
    private TypeDescriptor rawType;

    public ArrayDescriptor(String descriptor) {
        this.descriptor = descriptor;
        char[] split = descriptor.toCharArray();
        for (char s : split) {
            if ('[' == s) {
                this.dimension += 1;
            } else {
                // 如果 不是 "[" 了，到 类型 了。
                if ('L' == s) {
                    String[] nameSplit = descriptor.split("" + s);
                    ReferenceDescriptor referenceDescriptor = new ReferenceDescriptor("L" + nameSplit[1]);
                    this.rawType = referenceDescriptor;
                } else {
                    PrimitiveTypeDescriptor parse = PrimitiveTypeDescriptor.of("" + s);
                    this.rawType = parse;
                }
                break;
            }
        }
        this.name = this.rawType.getName();
        for (int i = 0; i < this.dimension; i++) {
            this.name = this.name + "[]";
        }
    }

    @Override
    public String getName() {
        return name;
    }

    public int getDimension() {
        return dimension;
    }

    public TypeDescriptor getRawType() {
        return rawType;
    }

    @Override
    public String getDescriptor() {
        return descriptor;
    }

}
