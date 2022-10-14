/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.kruger.vaccine.dto;

import java.util.Date;
import lombok.Data;
/**
 *
 * @author Carlos
 */
@Data
public class DatesRQ {
    
    private Date start;
    private Date end;
    
}
