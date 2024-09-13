package com.company.credit.evaluation.service;

import com.company.credit.evaluation.service.client.BehavioralAnalysisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BehavioralAnalysisService {

    private final BehavioralAnalysisClient behavioralAnalysisClient;

    @Autowired
    public BehavioralAnalysisService(BehavioralAnalysisClient behavioralAnalysisClient) {
        this.behavioralAnalysisClient = behavioralAnalysisClient;
    }

    public int analyzeBehavior(String accountStatementFilePath) {
        // Get behavior score from the client
        int score = behavioralAnalysisClient.analyzeBehavior(accountStatementFilePath);

        return Math.max(20, Math.min(75, score));
    }
}
