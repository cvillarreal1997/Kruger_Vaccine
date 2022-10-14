/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.kruger.vaccine.dao;

import ec.com.kruger.vaccine.model.Employee;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 *
 * @author Carlos
 */
public interface EmployeeRepository extends JpaRepository<Employee, Integer>{
    
    List<Employee> findByIdentification (String identification);
    
    List<Employee> findByEmail (String email);
    
    List<Employee> findByVaccinationStatus (boolean status);
    
}
