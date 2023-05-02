package com.nxzgroup.intern.service;

import com.nxzgroup.intern.model.Student;
import com.nxzgroup.intern.repository.StudentRepository;
import com.nxzgroup.intern.service.StudentService.StudentFilter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    @LocalServerPort
    int randomServerPort;
    
    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    Student student = new Student();

    
    @BeforeEach
    void init() {
        //Student student = new Student();
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
    @DisplayName("Test get page with pagination and sorting.")
    void testGetPageWithPaginationAndSorting() {
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
        assertEquals(Optional.of(student), studentService.retrieveStudent(1L));
    }

    @Test
    @DisplayName("Test getStudent by ID to return Student details if student Exist.")
    void testGetStudentById_ifStudentExist_returnsStudentDetails() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        ResponseEntity<Object> responseEntity = studentService.getStudent(1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Optional.of(student), responseEntity.getBody());
    }

    @Test
    @DisplayName("Test getStudent by ID to return NotFound if student Exist.")
    void testGetStudentById_ifStudentNotExist_returnsNotFound() {
            when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
            when(studentService.retrieveStudent(2L)).thenReturn(Optional.empty());
            ResponseEntity<Object> responseEntity = studentService.getStudent(2L);
            assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
            assertTrue(responseEntity.getBody() == null);
    }

    @Test
    @DisplayName("Test studentFilter by providing both name and lastname")
    void test_studentFilterByNameAndLastName() {
        String name = "Prayut";
        String lastName = "Chanangkarn";
        StudentFilter filter = StudentFilter.fromValues(name, lastName);
        assertEquals(StudentFilter.BY_NAME_AND_LASTNAME, filter);
    }

    @Test
    @DisplayName("Test studentFilter by providing only name")
    void test_studentFilterByName() {
        String name = "Prayut";
        StudentFilter filter = StudentFilter.fromValues(name, null);
        assertEquals(StudentFilter.BY_NAME, filter);
    }

    @Test
    @DisplayName("Test studentFilter by providing only lastname")
    void test_studentFilterByLastName() {
        String lastName = "Chanangkarn";
        StudentFilter filter = StudentFilter.fromValues(null, lastName);
        assertEquals(StudentFilter.BY_LASTNAME, filter);
    }

    @Test
    @DisplayName("Test studentFilter by providing nothing Should return all Student")
    void test_studentFilterWithoutFilter() {
        StudentFilter filter = StudentFilter.fromValues(null, null);
        assertEquals(StudentFilter.ALL, filter);
    }


    @Test
    void test_CreateStudent_No_Input() {
        // Student student = null;
        // Student createdStudent = studentService.createStudent(student);
        // assertNull(createdStudent);
    
        Student student = new Student();
        Student createdStudent = null;
        try {
            createdStudent = studentService.createStudent(student);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertNull(createdStudent);
    
    }

    @Test
    @DisplayName("Test updateStudent by updating exist student and it should present")
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
    @DisplayName("Test updateStudent by updating non-exist student and it should not be present since finding")
    void test_UpdateStudent_Non_Exist() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());
  
        Optional<Student> result = studentService.updateStudent(1L, student);
        assertFalse(result.isPresent());
    }
    @Test
    @DisplayName("Test updateStudent by updating exist student with same data")
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
    @DisplayName("Test deleteStudent by giving ID of student")
    void test_DeleteStudent_with_Exception() {
        this.studentService.deleteStudent(2L);
        Optional<Student> student = this.studentRepository.findById(2L);
        assertFalse(student.isPresent());
    }
}
