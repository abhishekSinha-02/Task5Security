package com.task5.springTask5.repo;

import com.task5.springTask5.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepo extends JpaRepository<Department,String> {

    Department findByName(String name);
    Department findByIdAndName(String id, String name);
}
