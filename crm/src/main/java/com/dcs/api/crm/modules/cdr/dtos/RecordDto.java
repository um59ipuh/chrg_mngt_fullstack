package com.dcs.api.crm.modules.cdr.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordDto {
    @NotBlank
    private String vehicleId;

    @NotBlank
    private LocalDateTime startTime;


    @NotBlank
    private LocalDateTime endTime;


    @NotBlank
    @Positive
    private BigDecimal totalCost;
}
