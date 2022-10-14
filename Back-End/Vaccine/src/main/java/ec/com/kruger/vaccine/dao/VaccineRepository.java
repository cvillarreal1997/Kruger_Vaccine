/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.kruger.vaccine.dao;

import ec.com.kruger.vaccine.model.Vaccine;
import ec.com.kruger.vaccine.model.VaccineType;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 *
 * @author Carlos
 */
public interface VaccineRepository extends JpaRepository<Vaccine, Integer>{
    
    List<Vaccine> findByVaccineType (VaccineType type);
    
    List<Vaccine> findByVaccinationDateBetween (Date start, Date end);
    
}
