package com.sgedts.employee.service;

import com.sgedts.employee.bean.EmployeeBean;
import com.sgedts.employee.model.Employee;
import com.sgedts.employee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee addEmployee(EmployeeBean employeeBean){
        Employee employee = new Employee();
        employee.setName(employeeBean.getName());
        employee.setPosition(employeeBean.getPosition());
        employee.setSalary(employeeBean.getSalary());

        return employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployees(){
        if (employeeRepository.findAll().isEmpty()){
            throw new RuntimeException("No employees found");
        }

        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id){
        if (isEmployeeExists(id)){
            return employeeRepository.findById(String.valueOf(id)).get();
        }
        throw new RuntimeException("Employee not found");
    }

    private boolean isEmployeeExists(Long id){
        return employeeRepository.existsById(String.valueOf(id));
    }

    public Employee updateEmployee(Long id, EmployeeBean employeeBean){
        Employee employee = employeeRepository.findById(String.valueOf(id)).orElse(null);
        if(employee != null){
            employee.setName(employeeBean.getName());
            employee.setPosition(employeeBean.getPosition());
            employee.setSalary(employeeBean.getSalary());
            return employeeRepository.save(employee);
        }
        throw new RuntimeException("Employee not found");
    }

    public String deleteEmployee(Long id){
        if(isEmployeeExists(id)){
            employeeRepository.deleteById(String.valueOf(id));
            return "Employee deleted successfully";
        }

        throw new RuntimeException("Employee not found");
    }

}
