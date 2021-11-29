# mapstruct-demo
mapstruct的简单使用案例

java bean映射，例如VO/BO/DTO等，告别BeanUtils.CopyProperties.

使用非常简单，只需配置实体类之间的映射关系即可。

#### 引入

> pom.xml

可配合lombok使用

~~~xml
<properties>
    <maven.compiler.source>1.8</maven.compiler.source>
    <maven.compiler.target>1.8</maven.compiler.target>
    <org.mapstruct.version>1.4.2.Final</org.mapstruct.version>
</properties>

<dependencies>
        <!-- junit4 -->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
            <scope>test</scope>
        </dependency>

        <!-- mapstruct -->
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct</artifactId>
            <version>${org.mapstruct.version}</version>
        </dependency>
</dependencies>

<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.8.1</version>
            <configuration>
                <source>${maven.compiler.source}</source>
                <target>${maven.compiler.target}</target>
                <annotationProcessorPaths>
                    <path>
                        <groupId>org.mapstruct</groupId>
                        <artifactId>mapstruct-processor</artifactId>
                        <version>${org.mapstruct.version}</version>
                    </path>
                    <!-- lombok -->
                    <path>
                        <groupId>org.projectlombok</groupId>
                        <artifactId>lombok</artifactId>
                        <version>${org.projectlombok.version}</version>
                    </path>
                    <!-- lombok and mapstruct binding -->
                    <path>
                        <groupId>org.projectlombok</groupId>
                        <artifactId>lombok-mapstruct-binding</artifactId>
                        <version>${lombok-mapstruct-binding.version}</version>
                    </path>
                </annotationProcessorPaths>
            </configuration>
        </plugin>
    </plugins>
</build>
~~~

#### 准备

> 实体类

~~~java
public class Student {
    private String name;
    private Integer age;
    private Gender gender;
    private Address address;
    // all args constructor
    // getter and setter
}
~~~

~~~java
public class Address {
    private String name;
    // all args constructor
    // getter and setter
}
~~~

~~~java
public enum Gender {
    MAN("男"),
    WOMAN("女");
    
    private String name;
    Gender(String name) { this.name = name; }
    public String getName() { return name; }
}
~~~

~~~java
public class Classes {
    private String name;
    // all args constructor
    // getter and setter
}
~~~

> VO

~~~java
public class StudentVO {
    private String name;
    private Integer age;
    private String gender;
    private String address;
    private String classes;
    // all args constructor
    // getter and setter
}
~~~

#### 一对一映射

~~~java
@Mapper
public interface StudentMapper {
	StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    /**
     * 转VO
     * @param student
     * @return
     */
    @Mapping(target = "address", source = "address.name")
    @Mapping(target = "gender", source = "gender")
    @Mapping(target = "classes", ignore = true)
    StudentVO studentToStudentVO(Student student);
    
    /**
     * VO逆转
     * @param studentVO
     * @return
     */
    @InheritInverseConfiguration(name = "studentToStudentVO")
    Student studentVOToStudent(StudentVO studentVO);
    
    /**
     * 转list
     * @param students
     * @return
     */
    @InheritConfiguration(name = "studentToStudentVO")
    List<StudentVO> studentsToStudentVOs(List<Student> students);
    
    /**
     * 传入参数：source
     * 输出参数：target
     * @param gender
     * @return
     */
    default String getGender(Gender gender) {
        return gender.getName();
    }

    /**
     * 传入参数：source
     * 输出参数：target
     * @param gender
     * @return
     */
    default Gender setGender(String gender) {
        if(Gender.MAN.getName().equals(gender)){
            return Gender.MAN;
        }else {
            return Gender.WOMAN;
        }
    }
}
~~~

> 测试

~~~java
public void testStudentToStudentVO() {
    Student student = new Student("belean", 21, Gender.MAN, new Address("home"), null);

    StudentVO studentVO = StudentMapper.INSTANCE.studentToStudentVO(student);
    studentCompareVO(student, studentVO);
}
public void testStudentVOTOStudent() {
    StudentVO studentVO = new StudentVO("belean", 21, "男", "home", null);

    Student student = StudentMapper.INSTANCE.studentVOToStudent(studentVO);
    assertNotNull(student);
    studentCompareVO(student, studentVO);
}
public void testStudentsToStudentVOs() {
    Student student = new Student("belean", 21, Gender.MAN, new Address("home"));
    Student student2 = new Student("hello", 22, Gender.WOMAN, new Address("my home"));

    List<Student> students = new ArrayList<>();
    students.add(student);
    students.add(student2);

    List<StudentVO> studentVOs = StudentMapper.INSTANCE.studentsToStudentVOs(students);
    assertNotNull(studentVOs);
    assertEquals(2, studentVOs.size());
    studentCompareVO(student, studentVOs.get(0));
    studentCompareVO(student2, studentVOs.get(1));
}
private void studentCompareVO(Student student, StudentVO studentVO) {
    assertNotNull(student);
    assertNotNull(studentVO);
    assertEquals(student.getName(), studentVO.getName());
    assertSame(student.getAge(), studentVO.getAge());
    assertEquals(student.getGender().getName(), studentVO.getGender());
    assertEquals(student.getAddress().getName(), studentVO.getAddress());
}
~~~

> 总结

- @Mapper：注解这个映射接口
  - componentModel = "spring" // 交由Spring容器管理，通过@Autowired注入使用
- StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);
  - 通过该方式获取实例，交由spring管理后可省略。
- @Mapping：配置映射关系
  - target：输出参数
  - source：输入参数
  - ignore：忽略对输入参数的映射
- getGender/setGender
  - 由于枚举类型无法通过构造方法创建，所以需要手动转换。
  - setGender是VO逆转才需要的。

#### 原理

mapstruct本质上是通过反射生成Mapper接口对应的实现，从而省去了我们手动编写转换代码的工作。

~~~java
/**
 * 转VO
 * @param student
 * @return
 */
@Mapping(target = "address", source = "address.name")
@Mapping(target = "gender", source = "gender")
@Mapping(target = "classes", ignore = true)
StudentVO studentToStudentVO(Student student);
~~~

> 在target目录下，通过反编译查看StudentMapperImpl.class

~~~java
public StudentVO studentToStudentVO(Student student) {
    if (student == null) {
        return null;
    } else {
        String address = null;
        String gender = null;
        String name = null;
        Integer age = null;
        address = this.studentAddressName(student);
        gender = this.getGender(student.getGender());
        name = student.getName();
        age = student.getAge();
        String classes = null;
        StudentVO studentVO = new StudentVO(name, age, gender, address, (String)classes);
        return studentVO;
    }
}
~~~

#### 多对一映射

~~~java
/**
 * 多对象转VO
 * @param student
 * @param classes
 * @return
 */
@Mapping(target = "name", source = "student.name")
@Mapping(target = "address", source = "student.address.name")
@Mapping(target = "gender", source = "student.gender")
@Mapping(target = "classes", source = "classes.name")
StudentVO studentAndClassesToStudentVO(Student student, Classes classes);
~~~

> 测试

~~~java
public void testStudentAndClassesToStudentVO(){
    Student student = new Student("belean", 21, Gender.MAN, new Address("home"));
    Classes classes = new Classes("六年一班");

    StudentVO studentVO = StudentMapper.INSTANCE.studentAndClassesToStudentVO(student, classes);
    studentCompareVO(student, studentVO);
    assertEquals(classes.getName(), studentVO.getClasses());
}
~~~

#### 总结

除了必要的实体类和转换类之外，只需简单的编写映射所需的Mapper接口即可。

学习以上案例基本上够用了，更多使用案例请参考：https://github.com/mapstruct/mapstruct-examples

