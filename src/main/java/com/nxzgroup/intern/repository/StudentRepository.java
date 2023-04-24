package com.nxzgroup.intern.repository;

import java.util.List;
import com.nxzgroup.intern.model.Student;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

public interface StudentRepository extends CrudRepository<Student, Long> {
    List<Student> findByFirstNameContainingIgnoreCase(String firstName, Sort sort);
    List<Student> findByLastNameContainingIgnoreCase(String lastName, Sort sort);
    List<Student> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(String firstName, String lastName, Sort sort);

    List<Student>  findAll(Sort id);

    Page<Student> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(String firstName, String lastName, Pageable pageable);

    Page<Student> findByFirstNameContainingIgnoreCase(String firstName, Pageable pageable);

    Page<Student> findByLastNameContainingIgnoreCase(String lastName, Pageable pageable);
    Page<Student> findAll(Pageable pageable);


}
