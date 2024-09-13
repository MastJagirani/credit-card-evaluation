package com.company.credit.evaluation.repository;


import com.company.credit.evaluation.model.CreditCardRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CreditCardRequestRepository extends JpaRepository<CreditCardRequest, UUID> {

    Optional<CreditCardRequest> findByEmiratesId(String emiratesId);
}
