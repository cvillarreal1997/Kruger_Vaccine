/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.kruger.vaccine.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 *
 * @author Carlos
 */


@Entity
@Table(name = "employee", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"identification", "email"})})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employee implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_employee", nullable = false)
    private Integer id;
    
    @Column(name = "identification", nullable = false)
    private String identification;
    
    @Column(name = "names", nullable = false, length = 50)
    private String names;
    
    @Column(name = "lastnames", nullable = false, length = 50)
    private String lastnames;
    
    @Column(name = "email", nullable = false, length = 50)
    private String email;
    
    @Column(name = "birthday", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date birthday;
    
    @Column(name = "address", nullable = true, length = 150)
    private String address;
    
    @Column(name = "phone", nullable = true, length = 10)
    private String phone;
    
    @Column(name = "vaccination_status", nullable = true)
    private boolean vaccinationStatus;
        
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee")
    @JsonManagedReference
    private List<Vaccine> vaccinationDetails;
    
}
