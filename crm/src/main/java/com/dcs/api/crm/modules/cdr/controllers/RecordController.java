package com.dcs.api.crm.modules.cdr.controllers;

import com.dcs.api.crm.customs.CustomPage;
import com.dcs.api.crm.modules.cdr.dtos.RecordDto;
import com.dcs.api.crm.modules.cdr.resources.RecordResource;
import com.dcs.api.crm.modules.cdr.services.RecordService;
import com.dcs.api.crm.schemas.ChargeDataRecord;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/api/records")
@Slf4j
public class RecordController {

    private final RecordService recordService;
    private final ModelMapper modelMapper;

    public RecordController(RecordService recordService, ModelMapper modelMapper) {
        this.recordService = recordService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("{id}")
    public ResponseEntity<RecordResource> getRecord(@PathVariable int id) {
        ChargeDataRecord cdr = recordService.getRecordById(id);
        RecordResource recordResource = modelMapper.map(cdr, RecordResource.class);
        log.info("Successfully fetched Charging Record Data with id: {}", cdr.getSessionId());
        return ResponseEntity.status(HttpStatus.OK).body(recordResource);
    }

    @PutMapping("{id}")
    public ResponseEntity<HttpStatus> updateRecord(@RequestBody RecordDto recordDto,
                                                 @PathVariable int id) {

        log.info("Updated CDR info with CDR id: {}", id);
        recordService.updateRecord(recordDto, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Integer> createRecord(@RequestBody RecordDto recordDto) {
        log.info("Parsed successfully from request body: {}", recordDto.toString());
        int cdrId = recordService.saveRecord(recordDto);
        log.info("New CDR created successfully with CDR id: {}", cdrId);
        return ResponseEntity.status(HttpStatus.CREATED).body(cdrId);
    }


    @GetMapping
    public CustomPage<RecordResource> getRecordsWithPaginationAndSorting(Pageable pageable){

        Page<RecordResource> cdrResourcesPaginated = recordService.allRecordsWithPageable(pageable)
                .map(cdr -> modelMapper.map(cdr, RecordResource.class));
        return new CustomPage<>(cdrResourcesPaginated);
    }

}
