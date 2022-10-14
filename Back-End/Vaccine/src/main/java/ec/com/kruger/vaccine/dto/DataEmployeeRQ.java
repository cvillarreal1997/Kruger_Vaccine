/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.kruger.vaccine.dto;

import java.util.Date;
import java.util.List;
import lombok.Data;
/**
 *
 * @author Carlos
 */
@Data
public class DataEmployeeRQ {
    
    private String names;
    private String surnames;
    private String email;
    private Date birthday;
    private String address;
    private String phone;
    private Boolean vaccinationStatus;
    private List<VaccineRQ> vaccinationDetails;
}
