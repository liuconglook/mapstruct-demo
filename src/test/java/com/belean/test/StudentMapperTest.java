package com.belean.test;

import com.belean.entity.Classes;
import com.belean.vo.StudentVO;
import com.belean.entity.Address;
import com.belean.entity.Gender;
import com.belean.entity.Student;
import com.belean.mapper.StudentMapper;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author belean
 * @date 2021/11/27
 */
public class StudentMapperTest extends TestCase {

    public void testStudentToStudentVO() {
        Student student = new Student("belean", 21, Gender.MAN, new Address("home"));

        StudentVO studentVO = StudentMapper.INSTANCE.studentToStudentVO(student);
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

    public void testStudentVOTOStudent() {
        StudentVO studentVO = new StudentVO("belean", 21, "男", "home", null);

        Student student = StudentMapper.INSTANCE.studentVOToStudent(studentVO);
        assertNotNull(student);
        studentCompareVO(student, studentVO);
    }

    public void testStudentAndClassesToStudentVO(){
        Student student = new Student("belean", 21, Gender.MAN, new Address("home"));
        Classes classes = new Classes("六年一班");

        StudentVO studentVO = StudentMapper.INSTANCE.studentAndClassesToStudentVO(student, classes);
        studentCompareVO(student, studentVO);
        assertEquals(classes.getName(), studentVO.getClasses());
    }

    private void studentCompareVO(Student student, StudentVO studentVO) {
        assertNotNull(student);
        assertNotNull(studentVO);
        assertEquals(student.getName(), studentVO.getName());
        assertSame(student.getAge(), studentVO.getAge());
        assertEquals(student.getGender().getName(), studentVO.getGender());
        assertEquals(student.getAddress().getName(), studentVO.getAddress());
    }
}
