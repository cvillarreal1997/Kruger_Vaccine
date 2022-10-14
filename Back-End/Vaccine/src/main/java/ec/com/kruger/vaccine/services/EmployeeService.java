/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.kruger.vaccine.services;

import ec.com.kruger.vaccine.dao.EmployeeRepository;
import ec.com.kruger.vaccine.dao.UserRespository;
import ec.com.kruger.vaccine.dao.VaccineTypeRepository;
import ec.com.kruger.vaccine.dto.CreateEmployeeRQ;
import ec.com.kruger.vaccine.dto.LoginRQ;
import ec.com.kruger.vaccine.dto.DataEmployeeRQ;
import ec.com.kruger.vaccine.dto.VaccineRQ;
import ec.com.kruger.vaccine.model.Employee;
import ec.com.kruger.vaccine.model.User;
import ec.com.kruger.vaccine.model.Vaccine;
import ec.com.kruger.vaccine.model.VaccineType;
import ec.com.kruger.vaccine.util.Validations;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ec.com.kruger.vaccine.dao.VaccineRepository;
import ec.com.kruger.vaccine.transform.Encode;

/**
 *
 * @author Carlos
 */
@Service
@Slf4j
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRespository userRespository;

    @Autowired
    private VaccineRepository vaccinationDetailRepository;

    @Autowired
    private VaccineTypeRepository vaccineTypeRepository;
    Validations v = new Validations();

    public List<Employee> getAllEmployees() {
        return this.employeeRepository.findAll();
    }

    public Employee getEmployeeById(int id) throws Exception {
        Optional<Employee> optionalEmployee = this.employeeRepository.findById(id);
        if (optionalEmployee.isEmpty()) {
            throw new Exception("employee not found");
        }
        return optionalEmployee.get();
    }

    @Transactional
    public LoginRQ registerEmployee(CreateEmployeeRQ createEmployeeRequest) throws Exception {
        List<Employee> employees = this.employeeRepository.findByIdentification(createEmployeeRequest.getIdentification());
        if (!employees.isEmpty()) {
            throw new Exception("cedula already in use");
        }
        employees = this.employeeRepository.findByEmail(createEmployeeRequest.getEmail());
        if (!employees.isEmpty()) {
            throw new Exception("Email already in use");
        }
        log.info("INFO: {}", v.validateEmail(createEmployeeRequest.getEmail()));
        log.info("INFO: {}", v.validateIdentification(createEmployeeRequest.getIdentification()));
        log.info("INFO: {}", v.validateNames(createEmployeeRequest.getNames(), createEmployeeRequest.getSurnames()));
        if (!v.validateEmail(createEmployeeRequest.getEmail()) || !v.validateIdentification(createEmployeeRequest.getIdentification())
                || !v.validateNames(createEmployeeRequest.getNames(), createEmployeeRequest.getSurnames())) {
            throw new Exception("the information is wrong");
        }
        Employee employee = Employee.builder()
                .names(createEmployeeRequest.getNames().toUpperCase())
                .lastnames(createEmployeeRequest.getSurnames().toUpperCase())
                .identification(createEmployeeRequest.getIdentification())
                .email(createEmployeeRequest.getEmail())
                .build();
        Employee newEmployee = this.employeeRepository.saveAndFlush(employee);
        String username = createEmployeeRequest.getNames().substring(0, 2) + createEmployeeRequest.getSurnames().substring(0, 4);
        username = username.toLowerCase();
        Optional<User> optionalUser = this.userRespository.findByUsername(username);
        int i = 1;
        String baseUsername = username;
        while (optionalUser.isPresent()) {
            username = baseUsername + Integer.toString(i);
            optionalUser = this.userRespository.findByUsername(username);
            i++;
        }
        Encode encode = new Encode();
        String password = username + "593";
        User user = User.builder()
                .username(username)
                .password(encode.Encriptar(password))
                .employee(newEmployee)
                .role("EMP")
                .build();
        log.info("INFO: {}", user);
        this.userRespository.save(user);
        LoginRQ generaredCredentials = LoginRQ.builder()
                .username(username)
                .password(encode.Encriptar(password))
                .build();
        return generaredCredentials;
    }

    public void deleteEmployeeById(int id) throws Exception {
        Optional<Employee> optionalEmployee = this.employeeRepository.findById(id);
        if (optionalEmployee.isEmpty()) {
            throw new Exception("User not found");
        }
        Optional<User> optionalUser = this.userRespository.findByEmployee(optionalEmployee.get());
        if (optionalUser.isEmpty()) {
            throw new Exception("User not found");
        }
        this.userRespository.delete(optionalUser.get());
        this.employeeRepository.delete(optionalEmployee.get());
    }

    public List<Employee> findByVaccinationStatus(boolean vaccinationStatus) {
        return this.employeeRepository.findByVaccinationStatus(vaccinationStatus);
    }

    public List<Employee> findByVaccineType(String vaccineType) throws Exception {
        log.info("TYPE: {}", this.vaccineTypeRepository.findAll());
        Optional<VaccineType> optionalVaccineType = this.vaccineTypeRepository.findByName(vaccineType);
        if (optionalVaccineType.isEmpty()) {
            throw new Exception("Type of vaccine not found");
        }
        List<Vaccine> details = this.vaccinationDetailRepository.findByVaccineType(optionalVaccineType.get());
        List<Employee> employees = new ArrayList<>();
        for (Vaccine detail : details) {
            if (!employees.contains(detail.getEmployee())) {
                employees.add(detail.getEmployee());
            }
        }
        return employees;
    }

    public void updateEmployeeById(int id, DataEmployeeRQ updateEmployeeRQ) throws Exception {
        //, lista que envio
        if (!updateEmployeeRQ.getVaccinationStatus() && updateEmployeeRQ.getVaccinationDetails().size() > 0) {
            throw new Exception("If you have not been vaccinated you cannot add details");
        }
        if (updateEmployeeRQ.getVaccinationStatus() && updateEmployeeRQ.getVaccinationDetails().isEmpty()) {
            throw new Exception("You need to add your vaccination details");
        }
        Optional<Employee> optionalEmployee = this.employeeRepository.findById(id);
        if (optionalEmployee.isEmpty()) {
            throw new Exception("Employee not found");
        }
        if (!v.validateEmail(updateEmployeeRQ.getEmail()) || !v.validateNames(updateEmployeeRQ.getNames(), updateEmployeeRQ.getSurnames())) {
            throw new Exception("Invalid information");
        }
        if (updateEmployeeRQ.getPhone() != null) {
            if (!v.validatePhone(updateEmployeeRQ.getPhone())) {
                throw new Exception("cellphone wring");
            }
        }
        Employee employee = optionalEmployee.get();
        employee.setNames(updateEmployeeRQ.getNames().toUpperCase());
        employee.setLastnames(updateEmployeeRQ.getSurnames().toUpperCase());
        employee.setBirthday(updateEmployeeRQ.getBirthday());
        employee.setAddress(updateEmployeeRQ.getAddress());
        employee.setPhone(updateEmployeeRQ.getPhone());
        employee.setVaccinationStatus(updateEmployeeRQ.getVaccinationStatus());

        this.employeeRepository.save(employee);
        if (updateEmployeeRQ.getVaccinationDetails().size() > 0 && updateEmployeeRQ.getVaccinationStatus()) {
            for (VaccineRQ detailRQ : updateEmployeeRQ.getVaccinationDetails()) {
                Optional<VaccineType> optionalVaccineType = this.vaccineTypeRepository.findById(detailRQ.getVaccineType());
                if (optionalVaccineType.isEmpty()) {
                    throw new Exception("Type of vaccine not found");
                }
                Vaccine vaccinationDetail = new Vaccine();
                vaccinationDetail.setEmployee(employee);
                vaccinationDetail.setVaccinationDate(detailRQ.getVaccinationDate());
                vaccinationDetail.setVaccinationDose(detailRQ.getVaccinationDose());
                vaccinationDetail.setVaccineType(optionalVaccineType.get());
                this.vaccinationDetailRepository.save(vaccinationDetail);
            }
        }
    }

    public List<Employee> findByDates(Date start, Date end) {
        List<Vaccine> details = this.vaccinationDetailRepository.findByVaccinationDateBetween(start, end);
        List<Employee> employees = new ArrayList<>();
        for (Vaccine detail : details) {
            if (!employees.contains(detail.getEmployee())) {
                employees.add(detail.getEmployee());
            }
        }
        return employees;
    }

}
