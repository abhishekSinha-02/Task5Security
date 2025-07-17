package com.task5.springTask5.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class DepartmentDto {
    private String id;
    private String name;
    @JsonIgnore
    List<EmployeeDto> employeeList;
}
