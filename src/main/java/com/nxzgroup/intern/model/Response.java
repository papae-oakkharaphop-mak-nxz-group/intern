package com.nxzgroup.intern.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

import org.springframework.http.HttpStatus;

@Setter
@Getter
@ToString
public class Response {
    private HttpStatus status;
    private List<Student> student;
    private Integer totalPage;
    private Integer totalStudent;
}
