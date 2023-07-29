package com.inet.juchamsi.domain.user.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
public class TenantResponse {

    private Long id;
    private String villaIdNumber; // 빌라 식별번호
    private String phoneNumber;
    private String name;
    private String carNumber;
    private int villaNumber;

    @Builder
    public TenantResponse(Long id, String villaIdNumber, String phoneNumber, String name, String carNumber, int villaNumber) {
        this.id = id;
        this.villaIdNumber = villaIdNumber;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.carNumber = carNumber;
        this.villaNumber = villaNumber;
    }
}