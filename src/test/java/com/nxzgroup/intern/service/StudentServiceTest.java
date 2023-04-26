package com.nxzgroup.intern.service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import com.nxzgroup.intern.controller.StudentController;
import com.nxzgroup.intern.model.Student;
import com.nxzgroup.intern.model.Response;
import com.nxzgroup.intern.repository.StudentRepository;

import net.minidev.json.JSONObject;

@WebMvcTest(StudentController.class)
class StudentServiceTest {
    
    @Autowired
    private MockMvc mvc;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;


    @BeforeEach
    public void setup() {

    }


    private Student buildTestingStudent() {
        Student student = new Student();
        student.setId((long) 1);
        student.setFirstName("Leo");
        student.setLastName("Kim");
        student.setBirthDate("01/23/2001");
 
        return student;
    }

    @Test
    void testGetAllStudents_wow() throws Exception {
        // Arrange
        int pageSize = 10;
        Integer page = null;
        String name = "Oakkharaphop";
        String lastname = "Makanat";
        String sortBy = "id";
        String sortOrder = "asc";
        Student student = new Student();
        student.setId(1L);
        student.setFirstName(name);
        student.setLastName(lastname);
        student.setBirthDate("02/02/2002");
        List<Student> students = new ArrayList<>();
        students.add(new Student(1L, "Oakkharaphop", "Makanat"));
        Response expectedResponse = new Response(); //new Response(HttpStatus.OK, students.size(), students.size(), students);
        expectedResponse.setStatus(HttpStatus.OK);
        expectedResponse.setStudent(students);
        expectedResponse.setTotalPage(page);
        expectedResponse.setTotalStudent(students.size());

        when(studentService.getAllStudents(pageSize, page, name, lastname, sortBy, sortOrder)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<Object> responseEntity = studentController.getAllStudents(page, pageSize, name, lastname, sortBy, sortOrder);

        // Assert
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(expectedResponse);
    }

    @SneakyThrows
    @Test
    void findAll_should_return_student_list() {
        mvc.perform(get("/students/").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
//        mvc.perform(MockMvcRequestBuilders
//        .get("/students/")
//        .accept(MediaType.APPLICATION_JSON))
//        .andExpect(status().isOk())
//.andExpect(MockMvcResultMatchers.jsonPath("$.student").exists())
//.andExpect(MockMvcResultMatchers.jsonPath("$.student[*].id").isNotEmpty());
    }

    @Test
    public void testGetAllStudents() {
        // Given
        // Student student = this.buildTestingStudent();
        // // When
        // when(studentRepository.findAll()).thenReturn(List.of(student));
        // List<Student> students = this.studentService.findAll();
        // // Then
        // assertEquals(1, students.size());
        
        // verify(this.studentRepository).findAll();
    }


    
}