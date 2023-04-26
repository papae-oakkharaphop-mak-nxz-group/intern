package com.nxzgroup.intern.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Data
@Entity
@Table(name = "students")
public class Student {
    public Student(){}
    public Student(long l, String string, String string2) {
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

    @NonNull
    private String birthDate;


}
