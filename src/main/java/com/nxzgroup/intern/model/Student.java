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
@Getter
@Setter
@Table(name = "students")
public class Student {
    public Student(){}
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
