package com.github.hcsp.descriptorparser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    public MethodDescriptor(String descriptor) {
        this.descriptor = descriptor;
        List<TypeDescriptor> TypeDescriptorList = new ArrayList<>();

        //拼接字符串
        StringBuffer stringBuffer = new StringBuffer();
        //返回值类型
        int index = descriptor.lastIndexOf(")");
        String returnTypeTemp = descriptor.substring(index + 1,descriptor.length());

        TypeDescriptor returnType = TypeDescriptor.parse(returnTypeTemp);
        this.returnType = returnType;
        String returnTypeStr = returnType.getName();
        stringBuffer.append(returnTypeStr + " ");

        //请求参数
        String paramTypesTemp = descriptor.substring(descriptor.lastIndexOf("(") + 1 ,descriptor.lastIndexOf(")"));
        if (paramTypesTemp.equals("")){
            this.name = stringBuffer.append("()").toString();
            this.paramTypes = TypeDescriptorList;
        }else{
            stringBuffer.append("(");
            String[] arr = paramTypesTemp.split("");
            //参数序号
            int params = 1;
            for(int i = 0; i < arr.length; i++){
                //参数
                String param;
                //原生类型
                if(Pattern.matches("[B,C,D,F,I,J,S,Z,V]", arr[i])){
                    param = arr[i];
                } else if("[".equalsIgnoreCase(arr[i])) {
                    if(Pattern.matches("[B,C,D,F,I,J,S,Z,V]", arr[i+1])){
                        param = arr[i] + arr[i+1];
                        i = ++i;
                    }else{
                        //引用类型、数组类型
                        param = paramTypesTemp.substring(0,paramTypesTemp.indexOf(";") + 1);
                        //多个字符
                        i = i + param.length() -1 ;
                    }
                }else{
                    //引用类型、数组类型
                    param = paramTypesTemp.substring(0,paramTypesTemp.indexOf(";") + 1);
                    //多个字符
                    i = i + param.length() -1 ;
                }
                TypeDescriptor typeDescriptor = TypeDescriptor.parse(param);
                TypeDescriptorList.add(typeDescriptor);
                String tempName = typeDescriptor.getName();
                stringBuffer.append(tempName + ", ");
                paramTypesTemp = paramTypesTemp.substring(param.length(),paramTypesTemp.length());
                params++;
            }
            stringBuffer.deleteCharAt(stringBuffer.length()-1).deleteCharAt(stringBuffer.length()-1);
            stringBuffer.append(")");

            this.name = stringBuffer.toString();
            this.paramTypes = TypeDescriptorList;
        }
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

