package com.dcs.api.crm.modules.cdr.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.dcs.api.crm.exceptions.BadRequestException;
import com.dcs.api.crm.exceptions.NotFoundException;
import com.dcs.api.crm.modules.cdr.dtos.RecordDto;
import com.dcs.api.crm.modules.cdr.repositories.RecordRepository;
import com.dcs.api.crm.schemas.ChargeDataRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;

@ExtendWith(MockitoExtension.class)
public class RecordServiceTest {

    @Mock
    private RecordRepository recordRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private RecordService recordService;

    private ChargeDataRecord mockChargeDataRecord;
    private RecordDto mockRecordDto;

    @BeforeEach
    public void setUp() {
        mockChargeDataRecord = ChargeDataRecord.builder()
                .sessionId(1)
                .startTime(LocalDateTime.of(2024, 1, 16, 19, 25, 2, 530))
                .endTime(LocalDateTime.of(2024, 1, 16, 19, 30, 2, 530))
                .totalCost(new BigDecimal(838))
                .vehicleId("PORSCHE1")
                .build();

        mockRecordDto = RecordDto.builder()
                .vehicleId("PORSCHE1")
                .startTime(LocalDateTime.of(2024, 1, 16, 19, 25, 2, 530))
                .endTime(LocalDateTime.of(2024, 1, 16, 19, 30, 2, 530))
                .totalCost(new BigDecimal(838))
                .build();
    }

    @Test
    public void testGetCDR() {
        when(recordRepository.findById(1)).thenReturn(Optional.of(mockChargeDataRecord));

        ChargeDataRecord result = recordService.getRecordById(1);
        assertNotNull(result);
        assertEquals(mockChargeDataRecord, result);
    }

    @Test
    public void testGetCDR_NotFound() {
        when(recordRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> recordService.getRecordById(1));
    }

    @Test
    public void testFindAllWithSorting() {
        Page<ChargeDataRecord> page = new PageImpl<>(List.of(mockChargeDataRecord));
        when(recordRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<ChargeDataRecord> result = recordService.allRecordsWithPageable(PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "endTime")));
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(mockChargeDataRecord, result.getContent().get(0));
    }

    @Test
    public void testIsCDRValid_InvalidEndTime() {
        RecordDto invalidRecordDto = RecordDto.builder()
                .vehicleId("PORSCHE1")
                .startTime(LocalDateTime.of(2024, 1, 16, 19, 35, 2, 530))
                .endTime(LocalDateTime.of(2024, 1, 16, 19, 30, 2, 530))
                .totalCost(new BigDecimal(838))
                .build();

        assertThrows(BadRequestException.class, () -> recordService.validateRecordDto(invalidRecordDto));
    }

    @Test
    public void testIsCDRValid_InvalidTotalCost() {
        RecordDto invalidRecordDto = RecordDto.builder()
                .vehicleId("PORSCHE1")
                .startTime(LocalDateTime.of(2024, 1, 16, 19, 25, 2, 530))
                .endTime(LocalDateTime.of(2024, 1, 16, 19, 30, 2, 530))
                .totalCost(new BigDecimal(-1))
                .build();

        assertThrows(NullPointerException.class, () -> recordService.validateRecordDto(invalidRecordDto));
    }
}
