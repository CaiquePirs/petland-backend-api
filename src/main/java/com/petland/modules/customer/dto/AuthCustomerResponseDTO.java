package com.petland.modules.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AuthCustomerResponseDTO {

    private String access_token;
    private Long expire_in;
}
