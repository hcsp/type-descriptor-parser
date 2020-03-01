package com.github.hcsp.descriptorparser;

import java.util.ArrayList;
import java.util.Arrays;
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

    public MethodDescriptor(String descriptor) {
        List<String> descriptors = Arrays.asList(descriptor.replaceAll(";", "").split("\\)"));
        //处理方法名
        String type_of_method_name = Character.toString(descriptors.get(1).charAt(0));

        if (type_of_method_name.equals("L")) {
            name = descriptors.get(1).trim().replaceAll("/", "\\.").substring(1);

        }
        if (type_of_method_name.matches("[BCDFIJSZV]")) {
            name = TypeDescriptor.parse(type_of_method_name).getName();
        }


        //处理参数 (IDLjava/lang/Thread -> (int, double, java.lang.Thread)
        System.out.println(descriptors.get(0));
        char[] chars = descriptors.get(0).toCharArray();
        int find_boundary_for_array;
        for (int i = 0; i < chars.length; i++) {
            int current_i = i;
            String new_char = Character.toString(chars[i]);
            //如果是Primitive
            if (new_char.matches("[BCDFIJSZ]")) {
                paramTypes.add(TypeDescriptor.parse(new_char));
            }
            //如果是数组， 找和它最近的type
            if (new_char.equals("[")) {
                //找到下一个type的位置
                for (find_boundary_for_array = i + 1; find_boundary_for_array < descriptors.get(0).length(); find_boundary_for_array++) {
                    if (Character.toString(chars[find_boundary_for_array]).matches("[BCDFIJSZL]")) {
                        i = find_boundary_for_array;
                        break;
                    }
                }
                //如果是引用类型的数组
                if (chars[find_boundary_for_array] == 'L') {
                    //需要输入L过后的所有char
                    //检查L过后还有没有标识符
                    int end_position_reference = 0;
                    for (int j = descriptors.get(0).indexOf("L"); j < descriptors.get(0).length(); j++) {
                        //如果有
                        if (Character.toString(chars[j]).matches("[BCDFIJSZ]")) {
                            end_position_reference = j;
                        } else {
                            end_position_reference = descriptors.get(0).length() - 1;
                        }
                    }
                    i = end_position_reference;
                }
                String reference = descriptors.get(0).substring(current_i, i + 1);
                paramTypes.add(TypeDescriptor.parse(reference));
            }
            if (new_char.matches("L")) {
                //需要输入L过后的所有char
                //检查L过后还有没有标识符
                int end_position_reference = 0;
                for (int j = descriptors.get(0).indexOf("L"); j < descriptors.get(0).length(); j++) {
                    //如果有
                    if (Character.toString(chars[j]).matches("[BCDFIJSZ]")) {
                        end_position_reference = j;
                    } else { //如果没有
                        end_position_reference = descriptors.get(0).length();
                    }
                }
                i = end_position_reference;
                String reference = descriptors.get(0).substring(descriptors.get(0).indexOf("L"), end_position_reference);
                paramTypes.add(TypeDescriptor.parse(reference));
            }
        }

        System.out.println(paramTypes);
        StringBuffer params_string = buildMethodParams();
        name += params_string.toString();
        System.out.println(name);
    }


    private StringBuffer buildMethodParams() {
        StringBuffer params = new StringBuffer();
        params.append(" (");

        for (TypeDescriptor typeDescriptor : paramTypes) {
            params.append(typeDescriptor.getName());
            if (!(paramTypes.indexOf(typeDescriptor) == paramTypes.size() - 1)) {
                params.append(", ");
            }
        }

        params.append(")");
        return params;
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

    public static void main(String[] args) {
        new MethodDescriptor("(IDLjava/lang/Thread;)Ljava/lang/Object");
    }
}

