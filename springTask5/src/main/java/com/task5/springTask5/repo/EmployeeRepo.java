package com.task5.springTask5.repo;

import com.task5.springTask5.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee,String> {

    Employee findByEmail(String email);
    Employee findByName(String name);
    List<Employee> findByDepartmentName(String name);
    List<Employee> findById(Long id);
    List<Employee> findByIdAndDepartmentName(Long id , String name);
}
