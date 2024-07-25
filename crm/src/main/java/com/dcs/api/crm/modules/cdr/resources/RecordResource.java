package com.dcs.api.crm.modules.cdr.resources;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordResource {
    private int sessionId;
    private String vehicleId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal totalCost;
}
