package com.jungbauer.generalfly.repository;

import com.jungbauer.generalfly.domain.DumpLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DumpLogRepository extends JpaRepository<DumpLog, Long> {

    List<DumpLog> findAllByFile(String file);
    List<DumpLog> findAllByMethod(String method);
}
