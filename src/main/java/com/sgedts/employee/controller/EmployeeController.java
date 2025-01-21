package com.sgedts.employee.controller;

import com.sgedts.employee.bean.ApiResponse;
import com.sgedts.employee.bean.EmployeeBean;
import com.sgedts.employee.model.Employee;
import com.sgedts.employee.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<?> addEmployee(@Valid @RequestBody EmployeeBean employeeBean, BindingResult bindingResult){
        if (employeeBean == null){
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Request body is required", null));
        }

        ResponseEntity<?> errors = getResponseEntity(bindingResult);
        if (errors != null) return errors;

        Employee employee = employeeService.addEmployee(employeeBean);
        return ResponseEntity.ok(new ApiResponse(true, "Employee added successfully", employee));

    }

    private ResponseEntity<?> getResponseEntity(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Map all validation errors
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );

            // Return errors in the response
            return ResponseEntity.badRequest().body(
                    new ApiResponse(false, "Validation failed", errors)
            );
        }
        return null;
    }

    @GetMapping
    public ResponseEntity<?> getAllEmployees(){
        try {
            return ResponseEntity.ok(new ApiResponse(true, "Employees found", employeeService.getAllEmployees()));
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable Long id){
        try {
            return ResponseEntity.ok(new ApiResponse(true, "Employee found", employeeService.getEmployeeById(id)));
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployeeById(@PathVariable Long id, @Valid @RequestBody EmployeeBean employeeBean, BindingResult bindingResult){
        if (employeeBean == null){
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Request body is required", null));
        }

        ResponseEntity<?> errors = getResponseEntity(bindingResult);
        if (errors != null) return errors;

        try {
            Employee employee = employeeService.updateEmployee(id, employeeBean);
            return ResponseEntity.ok(new ApiResponse(true, "Employee updated successfully", employee));
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployeeById(@PathVariable Long id){
        try {
            return ResponseEntity.ok(new ApiResponse(true, employeeService.deleteEmployee(id), null));
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage(), null));
        }
    }
}
