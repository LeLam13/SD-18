package com.example.demo.Service;

import com.example.demo.dto.EmployeeSignupDTO;

import java.util.List;

public interface EmployeeService {
    List<EmployeeSignupDTO> getAllEmployees();
}