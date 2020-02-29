package com.github.hcsp.descriptorparser;

/**
 * 数组类型的描述符，如输入[[Ljava/lang/Object;
 * 得到的name是java.lang.Object[][]
 * dimension为2（二维数组）
 * rawType代表无数组的原始类型  返回人类可读的类型名，如int或者java.lang.Object[]
 */
public class ArrayDescriptor implements TypeDescriptor {
    // [ -> 1
    // [[ -> 2
    private String name;
    private String descriptor;
    private int dimension;
    private TypeDescriptor rawType;


    // [[Ljava/lang/Object;
    public ArrayDescriptor(String descriptor) {
        char[] res=descriptor.trim().toCharArray();
        for (int i = 0; i <res.length ;i++ ) {
            if(res[i]=='['){
                dimension++;
            }
            if(res[i]=='/'){
                res[i]='.';
            }
        }
        switch(res[dimension]){
            case 'L':name=String.valueOf(res).substring(dimension+1,res.length-1);break;
            case 'I':name="int";break;
            case 'B':name="byte";break;
            case'C':name="char";break;
            case 'D':name="double";break;
            case 'F':name="float";break;
            case 'J':name="long";break;
            case 'S':name="short";break;
            default:name="boolean";
        }


        for(int j=0;j<dimension;j++){ //根据数组维度加方括号
            name+="[]";
        }


    }

    @Override
    public String getName() {
        return name;
    }

    public int getDimension() {//数组维度
        return dimension;
    }

    public TypeDescriptor getRawType() {//无数组的原始类型
        return rawType;
    }

    @Override
    public String getDescriptor() {//JVM中的描述符
        return descriptor;
    }
}
