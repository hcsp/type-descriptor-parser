package com.github.hcsp.descriptorparser;

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
        char[] res=descriptor.trim().toCharArray();
        for (int i = 0; i <res.length ;i++ ) {
            if(res[i]=='/'){
                res[i]='.';
            }
        }
       fqcn=String.valueOf(res).substring(1,res.length-1);


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
