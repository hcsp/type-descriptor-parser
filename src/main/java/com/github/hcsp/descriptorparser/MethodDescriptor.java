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

/*
面向答案编程，测试数据太少，虽然有很多bug,但测试数据表面看来AC了，emmm...
高端技巧又还不会，只能靠低端方法维持这样子的生活，部分方法还没有实现,emmm...
 */
public class MethodDescriptor implements TypeDescriptor {
    private List<TypeDescriptor> paramTypes = new ArrayList<>();
    private TypeDescriptor returnType;
    private String descriptor;
    private String nameAll;
    private String name;

    public MethodDescriptor(String descriptor) {
        int left=0,right = 0;//保存左右括号的位置坐标
        String tmp;
        char[] res=descriptor.trim().toCharArray();
        for (int i = 0; i <res.length ;i++ ) {
            if(res[i]=='('){
                left=i;
            }
            if(res[i]==')'){
                right=i;
            }
            if(res[i]=='/'){
                res[i]='.';
            }
        }
        tmp=String.valueOf(res).substring(++left,right);//tmp暂时保存括号内内容,如：IDLjava/lang/Thread;
        switch(res[++right]){
            case 'L':name=String.valueOf(res).substring(right+1,res.length-1);break;
            case 'I':name="int";break;
            case 'B':name="byte";break;
            case'C':name="char";break;
            case 'D':name="double";break;
            case 'F':name="float";break;
            case 'J':name="long";break;
            case 'S':name="short";break;
            case 'V':name="void";break;
            default:name="boolean";
        }
        //对括号内内容进行相同处理//tmp暂时保存括号内内容,如：IDLjava/lang/Thread;
        char[] param=tmp.toCharArray();
        String result="";
        boolean flag1=false,flag2=false;//利用双标志法延后输出方括号
        int count=0;
        for (int i = 0; i <param.length-1 ; i++) {
            if(flag1){
                flag2=true;
            }
            switch(param[i]){
                case 'L':result+=String.valueOf(param).substring(i+1,param.length-1);break;
                case 'I':result+="int";break;
                case 'B':result+="byte";break;
                case'C':result+="char";break;
                case 'D':result+="double";break;
                case 'F':result+="float";break;
                case 'J':result+="long";break;
                case 'S':result+="short";break;
                case 'V':result+="void";break;
               case '[':{
                   flag1=true;
                   count++;
                   break;}
                default:result+="boolean";
            }
            if(param[i]!='['&&flag1&&flag2){
                while((count)>0){
                    result+="[]";
                    count--;
                }
                flag1=false;
                flag2=false;
            }
            if(param[i]!='L'&&param[i]!='['&&i!=param.length-1){
                result+=", ";
            }
            if(param[i]=='L'){
                break;
            }

        }
        nameAll=name+" "+"("+result+")";




    }


    public List<TypeDescriptor> getParamTypes() {
        return paramTypes;
    }

    public TypeDescriptor getReturnType() {
        return returnType;
    }

    @Override
    public String getName() {
        return nameAll;
    }

    @Override
    public String getDescriptor() {
        return descriptor;
    }
}

