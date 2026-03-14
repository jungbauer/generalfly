package com.jungbauer.generalfly.service;

import com.jungbauer.generalfly.domain.DumpLog;
import com.jungbauer.generalfly.repository.DumpLogRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class DumpLogService {

    private final DumpLogRepository dumpLogRepository;

    public DumpLogService(DumpLogRepository dumpLogRepository) {
        this.dumpLogRepository = dumpLogRepository;
    }

    @Async
    public CompletableFuture<DumpLog> logMessage(String file, String method, String message) {
        DumpLog savedLog = dumpLogRepository.save(new DumpLog(file, method, message));
        return CompletableFuture.completedFuture(savedLog);
    }
}
