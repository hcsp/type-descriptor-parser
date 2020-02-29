package com.github.hcsp.descriptorparser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 代表方法的描述符，如给定方法描述符(IDLjava/lang/Thread;)Ljava/lang/Object;
 * paramTypes应该是对应的参数类型
 * returnType应该是返回值类型
 * name应该是人类可读的格式 java.lang.Object (int i, double d, java.lang.Thread t)
 */
public class MethodDescriptor implements TypeDescriptor {
    private List<TypeDescriptor> paramTypes = new ArrayList<>();
    private TypeDescriptor returnType;
    private String descriptor;
    private String name;

    public static final String END_POINT = "\\)";

    public MethodDescriptor(String descriptor) {
        String[] array = descriptor.split(END_POINT);

        this.descriptor = descriptor;
        this.returnType = TypeDescriptor.parse(array[1]);

        char[] descArray = array[0].toCharArray();

        for(int i = 0 ;i<descArray.length; i++){
            switch (descArray[i]){
                case '(':
                    break;
                case ')':
                    break;


                case 'L': {

                    StringBuffer stringBuffer = new StringBuffer();
                    while ( i < descArray.length ) {
                        stringBuffer.append(descArray[i]);
                        if(isEndOfReferenceType(descArray[i])){
                            break;
                        }
                        i++;
                    }

                    paramTypes.add(TypeDescriptor.parse(stringBuffer.toString()));
                    break;
                }


                case '[': {
                    StringBuffer stringBuffer = new StringBuffer();

                    while ( i < descArray.length) {
                        stringBuffer.append(descArray[i]);
                        boolean isPrimitiveType = PrimitiveTypeDescriptor.isPrimitive(Character.toString(descArray[i]));
                        if (isPrimitiveType){
                            break;
                        }
                        if (isEndOfReferenceType(descArray[i])){
                            break;
                        }
                        i++;
                    }

                    paramTypes.add(TypeDescriptor.parse(stringBuffer.toString()));
                    break;
                }


                default:
                    paramTypes.add(TypeDescriptor.parse(Character.toString(descArray[i])));
                    break;
            }
        }

        StringBuffer paramsDesc = new StringBuffer();
        paramsDesc.append(this.returnType.getName());
        paramsDesc.append(" (");

        if (!paramTypes.isEmpty()) {
            boolean firstFlag = true;
            for (TypeDescriptor typeDescriptor:paramTypes){
                if (firstFlag){
                    paramsDesc.append(typeDescriptor.getName());
                    firstFlag = false;
                }else {
                    paramsDesc.append(", "+typeDescriptor.getName());
                }
            }
        }

        paramsDesc.append(")");
        this.name = paramsDesc.toString();
    }

    /**
     * 是否是引用类型结束
     * @param c
     * @return
     */
    private boolean isEndOfReferenceType(char c) {
        return c == ';';
    }


    public List<TypeDescriptor> getParamTypes() {
        return paramTypes;
    }

    public TypeDescriptor getReturnType() {
        return returnType;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescriptor() {
        return descriptor;
    }
}

