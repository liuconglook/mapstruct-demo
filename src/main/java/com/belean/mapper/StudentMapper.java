package com.belean.mapper;

import com.belean.entity.Classes;
import com.belean.vo.StudentVO;
import com.belean.entity.Gender;
import com.belean.entity.Student;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author belean
 * @date 2021/11/27
 */
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
     * 转list
     * @param students
     * @return
     */
    @InheritConfiguration(name = "studentToStudentVO")
    List<StudentVO> studentsToStudentVOs(List<Student> students);

    /**
     * VO逆转
     * @param studentVO
     * @return
     */
    @InheritInverseConfiguration(name = "studentToStudentVO")
    Student studentVOToStudent(StudentVO studentVO);

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
