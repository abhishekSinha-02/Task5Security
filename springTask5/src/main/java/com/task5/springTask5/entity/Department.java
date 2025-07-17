package com.task5.springTask5.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "department")
    List<Employee> employeeList = new ArrayList<>();
}
