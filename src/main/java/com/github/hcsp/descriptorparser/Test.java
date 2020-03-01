package com.github.hcsp.descriptorparser;

public class Test {
    public  static void main(String[] args){
        //(Ljava/lang/Thread;Ljava/lang/Thread;ILjava/lang/Thread;[[Ljava/lang/Object;DDDIIBB)V
        TypeDescriptor typeDescriptor = TypeDescriptor.parse("D");
        if (typeDescriptor != null){
            System.out.println("实际类型:" + typeDescriptor.getName());
        }
    }
}
