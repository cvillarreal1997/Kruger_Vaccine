/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.kruger.vaccine.services;

import ec.com.kruger.vaccine.dao.UserRespository;
import ec.com.kruger.vaccine.dto.LoginRQ;
import ec.com.kruger.vaccine.dto.LoginRS;
import ec.com.kruger.vaccine.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;
/**
 *
 * @author Carlos
 */

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRespository userRespository;

    public LoginRS login(LoginRQ loginRequest) throws Exception {
        Optional<User> optionalUser = this.userRespository.findByUsername(loginRequest.getUsername());
        if (optionalUser.isEmpty()) {
            throw new Exception("User not found");
        }
        if (!optionalUser.get().getPassword().equals(loginRequest.getPassword())) {
            throw new Exception("The password is bad");
        }

        LoginRS loginResponse = LoginRS.builder()
                .username(optionalUser.get().getUsername())
                .role(optionalUser.get().getRole())
                .token(getJWTToken(optionalUser.get().getUsername(),optionalUser.get().getRole()))
                .id(optionalUser.get().getEmployee().getId())
                .build();
        return loginResponse;
    }

    private String getJWTToken(String username, String role) {
        String secretKey = "Pegelagarto";
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_"+role.toUpperCase());

        String token = Jwts
                .builder()
                .setId("softtekJWT")
                .setSubject(username)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(SignatureAlgorithm.HS512,
                        secretKey.getBytes()).compact();

        return "Bearer " + token;
    }
}
