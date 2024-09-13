package com.company.credit.evaluation.service.client.impl;

import com.company.credit.evaluation.service.client.BehavioralAnalysisClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BehavioralAnalysisClientImpl implements BehavioralAnalysisClient {

    @Override
    public int analyzeBehavior(String accountStatementFilePath) {
        // Mock implementation to return a score between 0 and 100
        // In a real scenario, this would involve uploading the file and analyzing the data
        log.info("Analyzing behavior for file: {}", accountStatementFilePath);

        // mock of analysis result
        // Generating a random score between 0 and 100
        return (int) (Math.random() * 101); // Random score between 0 and 100
    }
}
