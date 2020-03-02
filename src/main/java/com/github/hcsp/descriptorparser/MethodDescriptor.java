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
            int end_position_reference = 0;
            int end_position_array = 0;
            String new_char = Character.toString(chars[i]);
            //如果是Primitive, new_char是I, 直接从I parse为 int
            if (new_char.matches("[BCDFIJSZ]")) {
                paramTypes.add(TypeDescriptor.parse(new_char));
            }
            //如果是数组, new_char是[, 但需要[D或[[I去解析, 所以要找和它最近的type
            if (new_char.equals("[")) {
                /**
                 * 找到下一个type的位置，通过substring得到[D or [[I
                 * 有一个坑点是substring的参数endindex, 虽然是substring(beginindex, endindex)
                 * 实际得到的是Beginindex到endindex-1的字符串, 长度会减一
                 * 所以这里的end_position_array应该加一, 再传入substring
                 * 例如[的index 是1, D的index是2, end_position_array = 2,
                 * 但substring(1, 2)只能得到[, substring(1, 3)才能得到[D
                 * */
                for (find_boundary_for_array = i + 1; find_boundary_for_array < descriptors.get(0).length(); find_boundary_for_array++) {
                    if (Character.toString(chars[find_boundary_for_array]).matches("[BCDFIJSZL]")) {
                        end_position_array = find_boundary_for_array + 1;
                        break;
                    }
                }
                //如果是引用类型的数组
                if (chars[find_boundary_for_array] == 'L') {
                    /**
                     * 检查L过后还有没有标识符, 以下一个Type标识符的index作为endIndex,
                     * 用substring得到引用的完整字符串Ljava/lang/Thread
                     * */
                    for (int j = find_boundary_for_array + 1; j < descriptors.get(0).length(); j++) {
                        //如果有
                        if (Character.toString(chars[j]).matches("[BCDFIJSLZ\\[]")) {
                            end_position_array = j;
                            break;
                        } else if (j == descriptors.get(0).length() - 1) {//找到最后都没有找到
                            end_position_array = descriptors.get(0).length();
                        }
                    }

                }
                String reference = descriptors.get(0).substring(current_i, end_position_array);
                paramTypes.add(TypeDescriptor.parse(reference));
                i = end_position_array - 1;
            }
            if (new_char.matches("L")) {
                /**
                 * 检查L过后还有没有标识符, 以下一个Type标识符的index作为endIndex,
                 * 用substring得到引用的完整字符串Ljava/lang/Thread
                 * */
                //
                int end_position_reference_L = 0;
                for (int j = i + 1; j < descriptors.get(0).length(); j++) {
                    //如果有
                    if (Character.toString(chars[j]).matches("[BCDFIJSZL\\[]")) {
                        end_position_reference_L = j;
                        break;
                    } else if (j == descriptors.get(0).length() - 1) {
                        //找到最后都没找到
                        end_position_reference_L = descriptors.get(0).length();
                    }
                }

                String reference = descriptors.get(0).substring(i, end_position_reference_L);
                paramTypes.add(TypeDescriptor.parse(reference));
                /**
                 * 因为一轮循环后，开始下轮循环之前会i++
                 * 所以提前减一
                 * */

                i = end_position_reference_L - 1;
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
//        new MethodDescriptor("([Ljava/lang/ObjectI;)V");
        new MethodDescriptor("([DLjava/lang/Thread[I[[BF;)Ljava/lang/Object");
    }
}

