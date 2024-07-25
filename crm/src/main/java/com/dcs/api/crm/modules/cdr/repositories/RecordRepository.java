package com.dcs.api.crm.modules.cdr.repositories;

import com.dcs.api.crm.schemas.ChargeDataRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordRepository extends JpaRepository<ChargeDataRecord, Integer> {
    ChargeDataRecord findFirstByOrderByEndTimeDesc();
}
