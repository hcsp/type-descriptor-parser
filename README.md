# JVM/正则表达式/面向对象综合练习

这是一道有些难度的题目，你可能会在上面花上一天的时间，请做好心理准备。

但是如果你完成了这个挑战，你绝对会获得很多知识（JVM、正则表达式、面向对象）。

Ready? Go.

根据[Java虚拟机规范4.3.2](https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.3.2)，
在Java虚拟机内部使用精简的描述符来描述一个类型，以节省空间（有的时候你会在报错里看到这样的表示，所以了解这些知识绝对不会吃亏），如：

| 描述符        | 代表类型  | 备注                                                                                                     |
|---------------|-----------|----------------------------------------------------------------------------------------------------------|
| B             | byte      |                                                                                                          |
| C             | char      |                                                                                                          |
| D             | double    |                                                                                                          |
| F             | float     |                                                                                                          |
| I             | int       |                                                                                                          |
| J             | long      |                                                                                                          |
| L`<ClassName>`; | reference | 例如`java.lang.String`表示为`Ljava/lang/String;`                                                             |
| S             | short     |                                                                                                          |
| Z             | boolean   |                                                                                                          |
| [             | 数组      | 多维数组每增加一个维度，前面增加一个`[`。例如，`int[]`的描述符是`[I`, `String[][]`的描述符是`[[Ljava/lang/String;`|


同理，这些描述符也被用来描述方法，如方法

```
Object m(int i, double d, Thread t) {...}
```

在JVM内部的描述符是

```
(IDLjava/lang/Thread;)Ljava/lang/Object;
```

现在我们的问题是，给定一个类型描述符或者一个方法描述符，解析出它代表的实际类型。

如，给定方法描述符`(IDLjava/lang/Thread;)Ljava/lang/Object;`，你应该能够解析出`java.lang.Object (int i, double d, java.lang.Thread t)`。

在提交Pull Request之前，你应当在本地确保所有代码已经编译通过，并且通过了测试(`mvn clean verify`)

-----
注意！我们只允许你修改以下文件，对其他文件的修改会被拒绝：
- [src/main/java/com/github/hcsp/descriptorparser/](https://github.com/hcsp/type-descriptor-parser/blob/master/src/main/java/com/github/hcsp/descriptorparser/)
-----

