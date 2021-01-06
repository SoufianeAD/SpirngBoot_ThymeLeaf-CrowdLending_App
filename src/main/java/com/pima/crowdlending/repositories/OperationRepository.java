package com.pima.crowdlending.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pima.crowdlending.entities.Operation;

public interface OperationRepository extends JpaRepository<Operation, Integer> {
	
}
