package com.nxzgroup.intern.service;

import com.nxzgroup.intern.model.Student;
import com.nxzgroup.intern.repository.StudentRepository;
import com.nxzgroup.intern.service.StudentService.StudentFilter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        student.setFirstName("Prayut");
        student.setLastName("Chanangkarn");
        student.setBirthDate("01/01/1998");

        Mockito.lenient().when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
    }

    @Test
    void test_getPage() {
        assertNotNull(studentService.getPage(1, 10, "asc", "id"));
        //assertNull(studentService.getPage(2, 10, "asc", "id"));
    }
    @Test
    void test_getPageNew() {
        Integer page = 2;
        Integer pageSize = 10;
        String sortOrder = "asc";
        String sortBy = "id";
        Pageable pageable = studentService.getPage(page, pageSize, sortOrder, sortBy);

        assertEquals(pageable.getPageNumber(), page - 1);
        assertEquals(pageable.getPageSize(), pageSize);
        assertEquals(pageable.getSort().getOrderFor(sortBy).getDirection().name().toLowerCase(), sortOrder);
    }

    @Test
    void test_retrieveStudent() {
        Student student = new Student();
        student.setId(1L);
        student.setFirstName("Prayut");
        student.setLastName("Chanangkarn");
        student.setBirthDate("01/01/1998");

        assertEquals(Optional.of(student), studentService.retrieveStudent(1L));
    }

    @Test
    void test_getStudent_Exist() {
        Student student = new Student();

        student.setId(1L);
        student.setFirstName("Prayut");
        student.setLastName("Chanangkarn");
        student.setBirthDate("01/01/1998");
        
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        ResponseEntity<Object> responseEntity = studentService.getStudent(1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Optional.of(student), responseEntity.getBody());
    }

    @Test
    void test_getStudent_NotExist() {
        Student student = new Student();

        student.setId(1L);
        student.setFirstName("Prayut");
        student.setLastName("Chanangkarn");
        student.setBirthDate("01/01/1998");
        
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentService.retrieveStudent(2L)).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = studentService.getStudent(2L);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() == null);
    }

    @Test
    void test_studentFilter_All_Cases() {
        String name = "Prayut";
        String lastName = "Chanangkarn";

        StudentFilter filter1 = StudentFilter.fromValues(name, lastName);
        StudentFilter filter2 = StudentFilter.fromValues(name, null);
        StudentFilter filter3 = StudentFilter.fromValues(null, lastName);
        StudentFilter filter4 = StudentFilter.fromValues(null, null);
        
        assertEquals(StudentFilter.BY_NAME_AND_LASTNAME, filter1);
        assertEquals(StudentFilter.BY_NAME, filter2);
        assertEquals(StudentFilter.BY_LASTNAME, filter3);
        assertEquals(StudentFilter.ALL, filter4);
    }

    @Test
    void test_CreateStudent_No_Input() {
        Student student = null;
        Student createdStudent = studentService.createStudent(student);
        assertNull(createdStudent);
    }

    @Test
    void testUpdateStudent_Exist() {
        Student student = new Student();
        student.setId(1L);
        student.setFirstName("Prayut");
        student.setLastName("Chanangkarn");
        student.setBirthDate("01/01/1998");

        Student student2 = new Student();
        student2.setId(1L);
        student2.setFirstName("Prayut");
        student2.setLastName("Chan");
        student2.setBirthDate("01/01/1998");

        Long id = 1L;
        Student existingStudent = student;
        Student updatedStudent = student2;
        when(studentRepository.findById(id)).thenReturn(Optional.of(existingStudent));
        when(studentRepository.save(updatedStudent)).thenReturn(updatedStudent);

        Optional<Student> result = studentService.updateStudent(id, updatedStudent);

        assertTrue(result.isPresent());
        assertEquals(result.get().getId(), id);
        assertEquals(result.get().getFirstName(), updatedStudent.getFirstName());
        assertEquals(result.get().getLastName(), updatedStudent.getLastName());
        assertEquals(result.get().getBirthDate(), updatedStudent.getBirthDate());

    }
    @Test
    void test_UpdateStudent_Non_Exist() {
        Student student = new Student();
        student.setId(1L);
        student.setFirstName("Prayut");
        student.setLastName("Chanangkarn");
        student.setBirthDate("01/01/1998");
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());
  
        Optional<Student> result = studentService.updateStudent(1L, student);
        assertFalse(result.isPresent());
    }
    @Test
    void test_UpdateStudent_with_Exception() {
        Student student = new Student();
        student.setId(1L);
        student.setFirstName("Prayut");
        student.setLastName("Chanangkarn");
        student.setBirthDate("01/01/1998");

        Student student2 = new Student();
        student2.setId(1L);
        student2.setFirstName("Prayut");
        student2.setLastName("Chanangkarn");
        student2.setBirthDate("01/01/1998");

        Student existingStudent = student;
        Student updatedStudent = student2;
        when(studentRepository.findById(1L)).thenReturn(Optional.of(existingStudent));
        when(studentRepository.save(updatedStudent)).thenThrow(new RuntimeException());
        Optional<Student> result = studentService.updateStudent(1L, updatedStudent);
        assertNull(result);
    }

    @Test
    void test_DeleteStudent_with_Exception() {
        this.studentService.deleteStudent(2L);
        Optional<Student> student = this.studentRepository.findById(2L);
        assertFalse(student.isPresent());
    }
}
