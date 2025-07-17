package com.petland.modules.customer.controller;

import com.petland.modules.customer.dto.AuthCustomerRequestDTO;
import com.petland.modules.customer.dto.AuthCustomerResponseDTO;
import com.petland.modules.customer.service.AuthCustomerUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth/login")
@RequiredArgsConstructor
public class AuthCustomerController {

    private final AuthCustomerUseCase authCustomerUseCase;

    @PostMapping
    public ResponseEntity<AuthCustomerResponseDTO> login(@RequestBody AuthCustomerRequestDTO authCustomer){
        AuthCustomerResponseDTO auth = authCustomerUseCase.execute(authCustomer);
        return ResponseEntity.ok().body(auth);
    }
}
