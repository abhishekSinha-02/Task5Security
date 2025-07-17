package com.task5.springTask5.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EmployeeDto {
    private Long id;
    private String name;
    private String email;
    private UserDto userDto;
    private DepartmentDto departmentDto;
}
