package com.nxzgroup.intern.service;

import com.nxzgroup.intern.model.Student;
import com.nxzgroup.intern.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    void init() {
        Student student = new Student();
        student.setId(1L);
        student.setFirstName("Adam");
        student.setLastName("Godhand");
        student.setBirthDate("01/01/1998");

        Mockito.lenient().when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
    }

    @Test
    void test_getPage() {
        assertNotNull(studentService.getPage(1, 10, "asc", "id"));
        //assertNull(studentService.getPage(2, 10, "asc", "id"));
    }

    @Test
    void test_retrieveStudent() {
        Student student = new Student();
        student.setId(1L);
        student.setFirstName("Adam");
        student.setLastName("Godhand");
        student.setBirthDate("01/01/1998");

        assertEquals(Optional.of(student), studentService.retrieveStudent(1L));
    }
}
