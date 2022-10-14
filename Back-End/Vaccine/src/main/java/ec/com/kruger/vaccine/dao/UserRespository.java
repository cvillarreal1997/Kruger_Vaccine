/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.kruger.vaccine.dao;

import ec.com.kruger.vaccine.model.Employee;
import ec.com.kruger.vaccine.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 *
 * @author Carlos
 */

public interface UserRespository extends JpaRepository<User, Integer>{
    
    Optional<User> findByUsername (String username);
    Optional<User> findByEmployee (Employee employee);
}
