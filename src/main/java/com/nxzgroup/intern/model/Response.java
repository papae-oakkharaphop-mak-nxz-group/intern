package com.nxzgroup.intern.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class Response {
    private List<Student> student;
    private Integer totalPage;
    private Integer totalStudent;
}
