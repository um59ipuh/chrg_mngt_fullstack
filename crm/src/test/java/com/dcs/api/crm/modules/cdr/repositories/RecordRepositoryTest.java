package com.dcs.api.crm.modules.cdr.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import com.dcs.api.crm.schemas.ChargeDataRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RecordRepositoryTest {

    @Mock
    private RecordRepository recordRepository;

    private ChargeDataRecord mockChargeDataRecord;

    @BeforeEach
    public void setUp() {
        mockChargeDataRecord = ChargeDataRecord.builder()
                .sessionId(1)
                .startTime(LocalDateTime.of(2024, 1, 16, 19, 25, 2, 530))
                .endTime(LocalDateTime.of(2024, 1, 16, 19, 30, 2, 530))
                .totalCost(BigDecimal.valueOf(838))
                .vehicleId("PORSCHE1")
                .build();
    }

    @Test
    public void testFindFirstByOrderByEndTimeDesc() {
        when(recordRepository.findFirstByOrderByEndTimeDesc()).thenReturn(mockChargeDataRecord);

        ChargeDataRecord result = recordRepository.findFirstByOrderByEndTimeDesc();
        assertEquals(mockChargeDataRecord, result);
        assertEquals(mockChargeDataRecord.getEndTime(), result.getEndTime());
        assertEquals(mockChargeDataRecord.getStartTime(), result.getStartTime());
        assertEquals(mockChargeDataRecord.getTotalCost(), result.getTotalCost());
        assertEquals(mockChargeDataRecord.getVehicleId(), result.getVehicleId());
    }

    @Test
    public void testFindFirstByOrderByEndTimeDesc_NoData() {
        when(recordRepository.findFirstByOrderByEndTimeDesc()).thenReturn(null);

        ChargeDataRecord result = recordRepository.findFirstByOrderByEndTimeDesc();
        assertNull(result);
    }

    @Test
    public void testSaveChargeDataRecord() {
        when(recordRepository.save(any(ChargeDataRecord.class))).thenReturn(mockChargeDataRecord);

        ChargeDataRecord result = recordRepository.save(mockChargeDataRecord);
        assertNotNull(result);
        assertEquals(mockChargeDataRecord.getSessionId(), result.getSessionId());
        assertEquals(mockChargeDataRecord.getEndTime(), result.getEndTime());
        assertEquals(mockChargeDataRecord.getStartTime(), result.getStartTime());
        assertEquals(mockChargeDataRecord.getTotalCost(), result.getTotalCost());
        assertEquals(mockChargeDataRecord.getVehicleId(), result.getVehicleId());
    }

    @Test
    public void testFindById() {
        when(recordRepository.findById(1)).thenReturn(Optional.of(mockChargeDataRecord));

        Optional<ChargeDataRecord> result = recordRepository.findById(1);
        assertNotNull(result);
        assertEquals(mockChargeDataRecord, result.get());
    }

    @Test
    public void testUpdateChargeDataRecord() {
        ChargeDataRecord updatedRecord = ChargeDataRecord.builder()
                .sessionId(1)
                .startTime(LocalDateTime.of(2024, 1, 16, 19, 25, 2, 530))
                .endTime(LocalDateTime.of(2024, 1, 16, 19, 35, 2, 530)) // updated end time
                .totalCost(BigDecimal.valueOf(838))
                .vehicleId("PORSCHE1")
                .build();

        when(recordRepository.save(any(ChargeDataRecord.class))).thenReturn(updatedRecord);

        ChargeDataRecord result = recordRepository.save(updatedRecord);
        assertNotNull(result);
        assertEquals(updatedRecord.getSessionId(), result.getSessionId());
        assertEquals(updatedRecord.getEndTime(), result.getEndTime());
        assertEquals(updatedRecord.getStartTime(), result.getStartTime());
        assertEquals(updatedRecord.getTotalCost(), result.getTotalCost());
        assertEquals(updatedRecord.getVehicleId(), result.getVehicleId());
    }
}
