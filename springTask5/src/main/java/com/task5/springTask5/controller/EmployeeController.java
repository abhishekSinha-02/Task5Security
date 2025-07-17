package com.task5.springTask5.controller;

import com.task5.springTask5.dto.DepartmentDto;
import com.task5.springTask5.dto.EmployeeDto;
import com.task5.springTask5.dto.UserDto;
import com.task5.springTask5.models.StandardResponse;
import com.task5.springTask5.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.CredentialNotFoundException;
import java.security.PublicKey;
import java.util.List;

@RestController
@RequestMapping("/api")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/register")
    public UserDto registerUser(@RequestBody UserDto userDto){
        return employeeService.registerUser(userDto);
    }

    @PostMapping("/login")
    public StandardResponse userLogin(@RequestBody UserDto userDto) throws CredentialNotFoundException {
        return employeeService.userLogin(userDto);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAuthority('ADMIN' , 'MANAGER', 'EMPLOYEE')")
    public UserDto myDetails(){
        return employeeService.myDetails();
    }

    @GetMapping("/all-employees")
    @PreAuthorize("hasAuthority('ADMIN','MANAGER')")
    public List<EmployeeDto> AllEmployee(){
        return employeeService.allEmployee();
    }

    @GetMapping("/get-employees/{id}")
    @PreAuthorize("hasAuthority('ADMIN','MANAGER')")
    public List<EmployeeDto> getEmployeeById(@PathVariable Long id){
        return employeeService.getEmployeeById(id);
    }

    @PostMapping("/save-departments")
    public DepartmentDto saveDepartment(@RequestBody DepartmentDto departmentDto){
        return employeeService.saveDepartment(departmentDto);
    }

    @GetMapping("/get-department/{id}")
    @PreAuthorize("hasAuthority('ADMIN','MANAGER')")
    public DepartmentDto getDepartmentById(@PathVariable String id){
        return employeeService.getDepartmentById(id);
    }

    @PostMapping("/save-employees")
    @PreAuthorize("hasAuthority('ADMIN','MANAGER')")
    public EmployeeDto saveEmployee(@RequestBody EmployeeDto employeeDto){
        return employeeService.saveEmployee(employeeDto);
    }
}
