package com.task5.springTask5.service;

import com.task5.springTask5.ModelUtils;
import com.task5.springTask5.dto.DepartmentDto;
import com.task5.springTask5.dto.EmployeeDto;
import com.task5.springTask5.dto.UserDto;
import com.task5.springTask5.entity.Department;
import com.task5.springTask5.entity.Employee;
import com.task5.springTask5.entity.Users;
import com.task5.springTask5.models.StandardResponse;
import com.task5.springTask5.repo.DepartmentRepo;
import com.task5.springTask5.repo.EmployeeRepo;
import com.task5.springTask5.repo.UserRepo;
import com.task5.springTask5.security.JwtSecurityTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.security.auth.login.CredentialNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class EmployeeService {

    /**Constructor Injection**/

    UserRepo userRepo;
    PasswordEncoder passwordEncoder;
    JwtSecurityTokenUtils jwtSecurityTokenUtils;
    DepartmentRepo departmentRepo;
    EmployeeRepo employeeRepo;

    public EmployeeService(UserRepo userRepo, PasswordEncoder passwordEncoder, JwtSecurityTokenUtils jwtSecurityTokenUtils, DepartmentRepo departmentRepo, EmployeeRepo employeeRepo) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtSecurityTokenUtils = jwtSecurityTokenUtils;
        this.departmentRepo = departmentRepo;
        this.employeeRepo = employeeRepo;
    }

    public UserDto registerUser(UserDto userDto){

        Users users = ModelUtils.dtoToUser(userDto);
        users.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Users savedUsers = userRepo.save(users);

        return ModelUtils.userToDto(savedUsers);
    }

    public StandardResponse userLogin(UserDto userDto) throws CredentialNotFoundException{

        Users users = userRepo.findByUsername(userDto.getUsername());
        if (users == null){
            throw new RuntimeException("Username doesn't found");
        }
        if (passwordEncoder.matches(userDto.getPassword(), users.getPassword())){
            String token = jwtSecurityTokenUtils.generateToken(users.getUsername());
            StandardResponse response = new StandardResponse();
            response.setToken(token);
            response.setUserDto(ModelUtils.userToDto(users));
            return response;
        }else {
            throw new CredentialNotFoundException("password doesn't match");
        }
    }

    public UserDto myDetails(){
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = userRepo.findByUsername(username);
        return ModelUtils.userToDto(users);
    }

    public DepartmentDto saveDepartment(DepartmentDto departmentDto){
        Department department = ModelUtils.dtoToDepartment(departmentDto);
        Department savedDepartment = departmentRepo.save(department);
        return ModelUtils.departmentToDto(savedDepartment);
    }

    public EmployeeDto saveEmployee(EmployeeDto employeeDto){
        Employee requestEmployee = employeeRepo.findByEmail(employeeDto.getEmail());

        if(requestEmployee != null){
            Employee employee = ModelUtils.dtoToEmployee(employeeDto);
            employee.getUsers().setPassword(passwordEncoder.encode(employee.getUsers().getPassword()));

            String deptName = employeeDto.getDepartmentDto().getName();

            Department department = departmentRepo.findByName(deptName);

            if (department == null){
                department = departmentRepo.save(ModelUtils.dtoToDepartment(employeeDto.getDepartmentDto()));
            }
            employee.setDepartment(department);

            Employee savedEmployee = employeeRepo.save(employee);
            return ModelUtils.employeeToDto(savedEmployee);
        }else {
            throw new RuntimeException("User already exists");
        }
    }

    public List<EmployeeDto> allEmployee(){
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users user = userRepo.findByUsername(username);

        if (user.getRoleType().equals("ADMIN")){
            List<Employee> listOfUser = employeeRepo.findAll();
            List<EmployeeDto> dtoList = new ArrayList<>();
            for (Employee emp : listOfUser){
                dtoList.add(ModelUtils.employeeToDto(emp));
            }
            return dtoList;
        }
        Employee employee = employeeRepo.findByName(user.getUsername());
        log.info("employee info : {}",employee.getName());
        if (user.getRoleType().equals("MANAGER")){
            List<Employee> listOfSameEmployee = employeeRepo.findByDepartmentName(employee.getDepartment().getName())
                    .stream().filter(e -> e.getUsers().getRoleType().equals("EMPLOYEE")).toList();

            return listOfSameEmployee.stream().map(ModelUtils::employeeToDto).toList();
        }else {
            throw new RuntimeException("can't find the employee");
        }
    }

    public List<EmployeeDto> getEmployeeById(Long id){
        /**
         * line 135 will fetches the current logged in person
         */
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users user = userRepo.findByUsername(username);

        if (user.getRoleType().equals("ADMIN")){
            List<Employee> listOfUser = employeeRepo.findById(id);
            return listOfUser.stream().map(ModelUtils::employeeToDto).toList();
        }

        Employee employee = employeeRepo.findByName(username);
        if (employee.getUsers().getRoleType().equals("MANAGER")){
            List<Employee> listOfSameEmployee = employeeRepo.findByIdAndDepartmentName(id,employee.getDepartment().getName());
            return listOfSameEmployee.stream().map(ModelUtils::employeeToDto).toList();
        }
        else {
            throw new RuntimeException("Can't find the employee");
        }
    }

    public DepartmentDto getDepartmentById(String id){
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users user = userRepo.findByUsername(username);

        if(user.getRoleType().equals("ADMIN")){
            Department department = departmentRepo.findById(id).orElseThrow(()-> new RuntimeException("Can't find department by "+ id));
            log.info("department :{}",department);
            return ModelUtils.departmentToDto(department);
        }

        Employee employee = employeeRepo.findByName(username);
        if (employee.getUsers().getRoleType().equals("MANAGER")){
            Department department = departmentRepo.findByIdAndName(id,employee.getDepartment().getName());
            if(department == null){
                throw new RuntimeException("can't find the department "+id);
            }
            return ModelUtils.departmentToDto(department);
        }else {
            throw new RuntimeException("can't find the employee");
        }
    }
}
