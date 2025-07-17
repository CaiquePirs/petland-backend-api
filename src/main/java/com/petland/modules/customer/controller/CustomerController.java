package com.petland.modules.customer.controller;

import com.petland.modules.customer.dto.CustomerRequestDTO;
import com.petland.modules.customer.dto.CustomerResponseDTO;
import com.petland.modules.customer.mappers.CustomerMapper;
import com.petland.modules.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {




}
