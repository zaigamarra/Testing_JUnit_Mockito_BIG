package com.example.demo.student;

import com.example.demo.student.exception.BadRequestException;
import com.sun.tools.javac.jvm.Gen;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumenteCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownsBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock private StudentRepository studentRepository;

    private StudentService underTest;

    @BeforeEach
    void setUp(){

        underTest = new StudentService(studentRepository);
    }

    @Test
    void canGetAllStudents() {
        //when
        underTest.getAllStudents();

        //then
        verify(studentRepository).findAll();
    }

    @Test
    void canAddStudent() {
        //given
        Student student = new Student(
                "Jamila",
                "jamila@gmail.com",
                Gender.FEMALE
        );

        //when
        underTest.addStudent(student);

        //then
        ArgumentCaptor<Student> studentArgumentCaptor =
                ArgumentCapture.forClass(Student.class);

        verify(studentRepository).save(studentArgumentCaptor.capture());
        Student capturedStudent = studentArgumentCaptor.getValur();

        assertThat(capturedStudent).isEqualTo(student);
    }

    @Test
    void willThrowEmailIsTaken() {
        //given
        Student student = new Student(
                "Jamila",
                "jamila@gmail.com",
                Gender.FEMALE
        );

        given(studentRepository.selectExistsEmail(student.getEmail()))
                .willReturn(true);

        //when
        //then
         asssetThatThrownBy(() -> underTest.addStudent(student))
                 .isInstanceOf(BadRequestException.class)
                 .hasMessageContainig("Email " + student.getEmail() + "taken");

         verify(studentRepository, never().save(any));
    }

    @Test
    @Disabled
    void deleteStudent() {
    }
}