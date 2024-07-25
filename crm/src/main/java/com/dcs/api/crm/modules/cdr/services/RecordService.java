package com.dcs.api.crm.modules.cdr.services;

import com.dcs.api.crm.exceptions.BadRequestException;
import com.dcs.api.crm.exceptions.NotFoundException;
import com.dcs.api.crm.modules.cdr.dtos.RecordDto;
import com.dcs.api.crm.modules.cdr.repositories.RecordRepository;
import com.dcs.api.crm.schemas.ChargeDataRecord;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import java.math.BigDecimal;

@Service
@Slf4j
public class RecordService {

    private final RecordRepository recordRepository;
    private final ModelMapper modelMapper;

    public RecordService(RecordRepository recordRepository, ModelMapper modelMapper)
    {
        this.recordRepository = recordRepository;
        this.modelMapper = modelMapper;
    }


    /**
     * Retrieves a Charge Data Record by its ID.
     *
     * @param int id the ID of the CDR to retrieve
     * @return ChargeDataRecord the CDR with the specified ID
     * @throws NotFoundException if no CDR is found with the specified ID
     */
    public ChargeDataRecord getRecordById(int id) {
        return recordRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Charging Record doesn't exist with id={}", id);
                    return new NotFoundException("cdr.not_found");
                });
    }

    /**
     * Saves a new Charge Data Record.
     *
     * @param RecordDto the DTO representing the CDR to save
     * @return the ID of the saved CDR
     * @throws BadRequestException if the provided DTO is invalid
     */
    public int saveRecord(RecordDto recordDTO) {
        validateRecordDto(recordDTO);
        ChargeDataRecord cdr = modelMapper.map(recordDTO, ChargeDataRecord.class);
        int cdrID = recordRepository.save(cdr).getSessionId();
        return cdrID;
    }


    /**
     * Updates an existing Charge Data Record.
     *
     * @param RecordDto the DTO representing the updated CDR
     * @param int id    the ID of the CDR to update
     * @return the updated CDR
     * @throws NotFoundException if no CDR is found with the specified ID
     * @throws BadRequestException if the provided DTO is invalid
     */
    public ChargeDataRecord updateRecord(RecordDto updatedDto, int id) {
        validateRecordDto(updatedDto);
        return recordRepository.findById(id)
                .map(existingCDR -> {
                    modelMapper.map(updatedDto, existingCDR);
                    return recordRepository.save(existingCDR);
                }).orElseThrow(() -> {
                    log.warn("Charging Record doesn't exist with id={}", id);
                    return new NotFoundException("cdr.not_found");
                });
    }


    /**
     * Retrieves all Charge Data Records with pagination.
     *
     * @param Pageable pageable the pagination information
     * @return Page<ChargeDataRecord> a paginated list of CDRs
     */
    public Page<ChargeDataRecord> allRecordsWithPageable(Pageable pageable) {
        return recordRepository.findAll(pageable);
    }

    /**
     * Validates a Charge Data Record DTO.
     *
     * @param RecordDto the DTO to validate
     * @return true if the DTO is valid
     * @throws RestClientException if an error occurs during validation
     * @throws BadRequestException if the DTO is invalid
     */
    public Boolean validateRecordDto(RecordDto recordDTO) throws RestClientException {

        if (recordDTO.getEndTime().isBefore(recordDTO.getStartTime())) {
            throw new BadRequestException("endTime should later than startTime");
        }

        ChargeDataRecord topCDR = recordRepository.findFirstByOrderByEndTimeDesc();
        log.debug("Top record right now {}", topCDR.toString());
        if (recordDTO.getStartTime().isBefore(topCDR.getEndTime())) {
            throw new BadRequestException("Current startTime should greater than last endTime=" + topCDR.getEndTime());
        }

        if (recordDTO.getTotalCost().compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("totalCost should be greater than 0");
        }

        return true;
    }
}
