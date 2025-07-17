package com.task5.springTask5;

import com.task5.springTask5.dto.DepartmentDto;
import com.task5.springTask5.dto.EmployeeDto;
import com.task5.springTask5.dto.UserDto;
import com.task5.springTask5.entity.Department;
import com.task5.springTask5.entity.Employee;
import com.task5.springTask5.entity.Users;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

public class ModelUtils {

    PasswordEncoder passwordEncoder;

    public ModelUtils(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public static UserDto userToDto(Users user){
        if(user == null)
            return null;

        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setPassword(user.getPassword());
        dto.setRoleType(user.getRoleType());
        return dto;

    }

    public static Users dtoToUser(UserDto dto){
        if(dto == null)
            return null;

        Users users = new Users();
        users.setId(dto.getId());
        users.setUsername(dto.getUsername());
        users.setPassword(dto.getPassword());
        users.setRoleType(dto.getRoleType());
        return users;
    }

    public static EmployeeDto employeeToDto(Employee employee){
        if (employee == null)
            return null;

        EmployeeDto dto = new EmployeeDto();
        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setEmail(employee.getEmail());
        dto.setUserDto(userToDto(employee.getUsers()));
        dto.setDepartmentDto(departmentToDto(employee.getDepartment()));
        return dto;
    }

    public static Employee dtoToEmployee(EmployeeDto dto){
        if (dto == null)
            return null;

        Employee employee = new Employee();
        employee.setId(dto.getId());
        employee.setName(dto.getName());
        employee.setEmail(dto.getEmail());
        employee.setUsers(dtoToUser(dto.getUserDto()));
        employee.setDepartment(dtoToDepartment(dto.getDepartmentDto()));
        return employee;
    }

    public static DepartmentDto departmentToDto(Department dept){
        if (dept == null)
            return null;

        DepartmentDto dto = new DepartmentDto();
        dto.setId(dept.getId());
        dto.setName(dept.getName());

        if(dept.getEmployeeList() != null){

//            List<EmployeeDto> employees = dept.getEmployeeList().stream().map(ModelUtils::employeeToDto).toList();
            List<Employee> employees = dept.getEmployeeList();
            List<EmployeeDto> employeeDto = new ArrayList<>();
            for (Employee emp : employees){
                EmployeeDto emp1 = ModelUtils.employeeToDto(emp);
                employeeDto.add(emp1);
            }
            dto.setEmployeeList(employeeDto);
        }
        return dto;
    }

    public static Department dtoToDepartment(DepartmentDto dto){
        if(dto == null)
            return null;

        Department dept = new Department();
        dept.setId(dto.getId());
        dept.setName(dto.getName());

        if(dto.getEmployeeList() != null){
            List<Employee> employees = dto.getEmployeeList().stream().map(ModelUtils::dtoToEmployee).toList();
            dept.setEmployeeList(employees);
        }
        return dept;
    }
}
