/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.kruger.vaccine.controller;

import ec.com.kruger.vaccine.dto.CreateEmployeeRQ;
import ec.com.kruger.vaccine.dto.DatesRQ;
import ec.com.kruger.vaccine.dto.LoginRQ;
import ec.com.kruger.vaccine.dto.DataEmployeeRQ;
import ec.com.kruger.vaccine.model.Employee;
import ec.com.kruger.vaccine.services.EmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
/**
 *
 * @author Carlos
 */

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/employee")
@Slf4j
@Api(tags = "Employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    @ApiOperation(value = "Register a new employee",
            notes = "Just the person like admin can add a new employee")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Ok - Successful registration"),
        @ApiResponse(code = 400, message = "Bad Request - Invalid data"),
        @ApiResponse(code = 500, message = "Internal Server Error - Server error during process")})
  
    public ResponseEntity createEmployee(@RequestBody CreateEmployeeRQ employeeRequest) {
        try {
            LoginRQ generatedCredentials = this.employeeService.registerEmployee(employeeRequest);
            return ResponseEntity.ok(generatedCredentials);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping
    @ApiOperation(value = "Get all employees",
            notes = "Get all employees")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Ok - Successful serch"),
        @ApiResponse(code = 500, message = "Internal Server Error - Server error during process")})
    public ResponseEntity getAllEmployees() {
        try {
            List<Employee> employees = this.employeeService.getAllEmployees();
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "Get employee by ID",
            notes = "Get employee information by ID")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Ok - Successful serch"),
        @ApiResponse(code = 404, message = "Not Found - User not found"),
        @ApiResponse(code = 500, message = "Internal Server Error - Server error during process")})
    public ResponseEntity getEmployeeById(@PathVariable int id) {
        try {
            Employee employee = this.employeeService.getEmployeeById(id);
            return ResponseEntity.ok(employee);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping(value = "/{id}")
    @ApiOperation(value = "Update employee by ID",
            notes = "Update employee information by ID")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Ok - Successful update"),
        @ApiResponse(code = 400, message = "Bad Request - Invalid data"),
        @ApiResponse(code = 500, message = "Internal Server Error - Server error during process")})
    public ResponseEntity updateEmployeeById(@PathVariable int id, @RequestBody DataEmployeeRQ dateEmployeeRQ) {
        try {
            this.employeeService.updateEmployeeById(id, dateEmployeeRQ);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "Delete employee by ID",
            notes = "Delete employee by ID")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Ok - Successful update"),
        @ApiResponse(code = 404, message = "Not Found - User not found"),
        @ApiResponse(code = 500, message = "Internal Server Error - Server error during process")})
    public ResponseEntity deleteEmployeeById(@PathVariable int id) {
        try {
            this.employeeService.deleteEmployeeById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping(value = "/filter/status/{status}")
    @ApiOperation(value = "Filter by vaccination status",
            notes = "Filter employees by vaccination status")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Ok - Successful search"),
        @ApiResponse(code = 500, message = "Internal Server Error - Server error during process")})
    public ResponseEntity findEmployeesByVaccinationStatus(@PathVariable boolean status) {
        try {
            List<Employee> employees = this.employeeService.findByVaccinationStatus(status);
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            log.info("{}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping(value = "/filter/vaccination/dates")
    @ApiOperation(value = "Filter by vaccination date range",
            notes = "Filter employees by vaccination date range")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Ok - Successful search"),
        @ApiResponse(code = 500, message = "Internal Server Error - Server error during process")})
    public ResponseEntity findEmployeesByVaccinationDates(@RequestBody DatesRQ datesRQ) {
        try {
            List<Employee> employees = this.employeeService.findByDates(datesRQ.getStart(), datesRQ.getEnd());
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/filter/vaccine/type/{type}")
    @ApiOperation(value = "Filter by type of vaccine",
            notes = "Filter employees by type of vaccine")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Ok - Successful search"),
        @ApiResponse(code = 500, message = "Internal Server Error - Server error during process")})
    public ResponseEntity findVaccineByVaccineType(@PathVariable String type) {
        try {
            List<Employee> employees = this.employeeService.findByVaccineType(type);
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
