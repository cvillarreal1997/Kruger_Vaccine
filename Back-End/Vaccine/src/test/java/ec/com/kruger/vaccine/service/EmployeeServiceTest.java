package ec.com.kruger.vaccine.service;

import ec.com.kruger.vaccine.dao.EmployeeRepository;
import ec.com.kruger.vaccine.dao.UserRespository;
import ec.com.kruger.vaccine.dao.VaccineRepository;
import ec.com.kruger.vaccine.dao.VaccineTypeRepository;
import ec.com.kruger.vaccine.dto.CreateEmployeeRQ;
import ec.com.kruger.vaccine.dto.DataEmployeeRQ;
import ec.com.kruger.vaccine.dto.LoginRQ;
import ec.com.kruger.vaccine.dto.VaccineRQ;
import ec.com.kruger.vaccine.model.Employee;
import ec.com.kruger.vaccine.model.Vaccine;
import ec.com.kruger.vaccine.model.VaccineType;
import ec.com.kruger.vaccine.services.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private UserRespository userRespository;
    @Mock
    private VaccineTypeRepository vaccineTypeRepository;
    @Mock
    private VaccineRepository vaccinationDetailRepository;

    @InjectMocks
    private EmployeeService employeeService;
    private Employee employee1;
    private Employee employee2;
    private List<Employee> employeeList;
    private CreateEmployeeRQ createEmployeeRQ;
    private DataEmployeeRQ updateEmployeeRQ;
    private VaccineType vaccineType;
    private Vaccine vaccinationDetail1;
    private Vaccine vaccinationDetail2;
    private Vaccine vaccinationDetail3;
    private List<Vaccine> vaccinationDetailList;

    @BeforeEach
    void setUp() {
        this.employee1 = Employee.builder()
                .id(1)
                .names("Carlos Ramiro")
                .lastnames("Villarreal Mendieta")
                .identification("1709620338")
                .email("terrycarlo@live.com")
                .build();
        this.employee2 = Employee.builder()
                .id(2)
                .names("Omar Ricardo")
                .lastnames("Mejia Callo")
                .identification("1709620338")
                .email("omarmejia@outlook.com")
                .build();

        this.vaccineType = new VaccineType();
        vaccineType.setId(1);
        vaccineType.setName("Sputnik");

        this.vaccinationDetail1 = new Vaccine();
        vaccinationDetail1.setEmployee(employee1);
        vaccinationDetail1.setVaccinationDate(new Date());
        vaccinationDetail1.setVaccineType(vaccineType);
        vaccinationDetail1.setId(1);
        vaccinationDetail1.setVaccinationDose(2);

        this.vaccinationDetail2 = new Vaccine();
        vaccinationDetail2.setEmployee(employee2);
        vaccinationDetail2.setVaccinationDate(new Date());
        vaccinationDetail2.setVaccineType(vaccineType);
        vaccinationDetail2.setId(1);
        vaccinationDetail2.setVaccinationDose(2);

        this.vaccinationDetail3 = new Vaccine();
        vaccinationDetail3.setEmployee(employee2);
        vaccinationDetail3.setVaccinationDate(new Date());
        vaccinationDetail3.setVaccineType(vaccineType);
        vaccinationDetail3.setId(1);
        vaccinationDetail3.setVaccinationDose(2);

        this.vaccinationDetailList = new ArrayList<>();
        vaccinationDetailList.add(vaccinationDetail1);
        vaccinationDetailList.add(vaccinationDetail2);
        vaccinationDetailList.add(vaccinationDetail3);

        this.updateEmployeeRQ = new DataEmployeeRQ();
        this.employeeList = new ArrayList<>();
        employeeList.add(employee1);
        employeeList.add(employee2);
        this.createEmployeeRQ = new CreateEmployeeRQ();
    }

    

    @Test
    void getAllEmployees() {
        try {
            when(employeeRepository.findAll()).thenReturn(employeeList);
            Assertions.assertEquals(employeeList,employeeService.getAllEmployees());
        } catch (Exception e){
            log.error("{}", e.getMessage());
        }
    }

    @Test
    void givenIdReturnEmployee() {
        try {
            when(employeeRepository.findById(any())).thenReturn(java.util.Optional.of(employee1));
            Assertions.assertEquals(employee1,employeeService.getEmployeeById(1));
        } catch (Exception e){
            log.error("{}", e.getMessage());
        }
    }

 
    @Test
    void givenIdDeleteEmployee() {
        try {
            when(employeeRepository.findById(any())).thenReturn(java.util.Optional.of(employee1));
            employeeService.deleteEmployeeById(1);
            verify(employeeRepository, times(1)).delete(employee1);
        } catch (Exception e){
            log.error("{}", e.getMessage());
        }
    }

    @Test
    void givenVaccinationStatusReturnEmployees() {
        try {
            List<Employee> testEmployees = new ArrayList<>();
            testEmployees.add(employee2);
            when(employeeRepository.findByVaccinationStatus(true)).thenReturn(testEmployees);
            Assertions.assertEquals(testEmployees,employeeService.findByVaccinationStatus(true));
        } catch (Exception e){
            log.error("{}", e.getMessage());
        }
    }

    @Test
    void givenVaccineTypeReturnEmployees() {
        try {
            when(vaccineTypeRepository.findByName(any())).thenReturn(java.util.Optional.of(vaccineType));
            when(vaccinationDetailRepository.findByVaccineType(vaccineType)).thenReturn(vaccinationDetailList);
            Assertions.assertEquals(employeeList,employeeService.findByVaccineType("Sputnik"));
        } catch (Exception e){
            log.error("{}", e.getMessage());
        }
    }

    @Test
    void givenDateRangeVaccinationReturnEmployees() {
        try {
            when(vaccinationDetailRepository.findByVaccinationDateBetween(any(),any())).thenReturn(vaccinationDetailList);
            Assertions.assertEquals(employeeList,employeeService.findByDates(new Date(), new Date()));
        } catch (Exception e){
            log.error("{}", e.getMessage());
        }
    }
     
    void givenUpdateEmployeeRQAndIdUpdateEmployee() {
        try {
            when(vaccineTypeRepository.findById(any())).thenReturn(java.util.Optional.of(vaccineType));
            when(employeeRepository.findById(any())).thenReturn(java.util.Optional.of(employee2));
            updateEmployeeRQ.setEmail("omarmejia@outlook.com");
            updateEmployeeRQ.setNames("Omar Ricardo");
            updateEmployeeRQ.setSurnames("Mejia Callo");
            updateEmployeeRQ.setAddress("Quito");
            updateEmployeeRQ.setBirthday(new Date());
            updateEmployeeRQ.setPhone("0959545998");
            updateEmployeeRQ.setVaccinationStatus(true);
            List<VaccineRQ> vaccinationDetailsRQ = new ArrayList<>();
            VaccineRQ vaccinationDetailRQ = new VaccineRQ ();
            vaccinationDetailRQ.setVaccineType(1);
            vaccinationDetailRQ.setVaccinationDate(new Date());
            vaccinationDetailRQ.setVaccinationDose(1);
            vaccinationDetailsRQ.add(vaccinationDetailRQ);
            updateEmployeeRQ.setVaccinationDetails(vaccinationDetailsRQ);
            employeeService.updateEmployeeById(2,updateEmployeeRQ);
            verify(employeeRepository, times(1)).save(employee2);
        } catch (Exception e){
            log.error("{}", e.getMessage());
        }
    }

   
    void givenEmployeeRegisterEmployee() {
        try {
            when(employeeRepository.findByIdentification(any())).thenReturn(new ArrayList<>());
            when(employeeRepository.findByEmail(any())).thenReturn(new ArrayList<>());
            when(userRespository.findByUsername(any())).thenReturn(Optional.empty());
            createEmployeeRQ.setEmail("terrycarlo@live.com");
            createEmployeeRQ.setIdentification("1709620338");
            createEmployeeRQ.setNames("Carlos Ramiro");
            createEmployeeRQ.setSurnames("Villarreal Mendieta");
            LoginRQ generatedCredentials = LoginRQ.builder()
                    .username("carlos")
                    .password("carlos123")
                    .build();
            Assertions.assertEquals(generatedCredentials, employeeService.registerEmployee(createEmployeeRQ));
        }catch (Exception e){
            log.error("{}", e.getMessage());
        }
    }
}