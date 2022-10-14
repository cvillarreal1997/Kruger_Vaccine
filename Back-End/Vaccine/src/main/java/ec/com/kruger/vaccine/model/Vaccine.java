/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.kruger.vaccine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Carlos
 */
@Entity
@Table(name = "vaccine")
@Data
@NoArgsConstructor
public class Vaccine implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_detail", nullable = false)
    private Integer id;

    @Column(name = "vaccination_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date vaccinationDate;

    @Column(name = "vaccination_dose", nullable = false)
    private Integer vaccinationDose;

    @JoinColumn(name = "cod_vaccine", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private VaccineType vaccineType;

    @JoinColumn(name = "cod_employee", nullable = false)
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
    @JsonIgnore
    private Employee employee;

}
