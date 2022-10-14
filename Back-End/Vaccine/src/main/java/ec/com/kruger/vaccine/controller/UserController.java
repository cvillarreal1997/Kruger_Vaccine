/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.kruger.vaccine.controller;

import ec.com.kruger.vaccine.dto.LoginRQ;
import ec.com.kruger.vaccine.dto.LoginRS;
import ec.com.kruger.vaccine.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
/**
 *
 * @author Carlos
 */
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/login")
@Slf4j
@Api(tags = "User")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    @ApiOperation(value = "Login",
            notes = "Login to be able to use the other endpoints")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Ok - Successful Login"),
        @ApiResponse(code = 400, message = "Bad Request - Invalid data"),
        @ApiResponse(code = 404, message = "Not Found - User not found"),
        @ApiResponse(code = 500, message = "Internal Server Error - Server error during process")})
    public ResponseEntity login(@RequestBody LoginRQ loginRequest) {
        try {
            LoginRS loginRS = this.userService.login(loginRequest);
            return ResponseEntity.ok(loginRS);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity checkToken() {
        try {
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
