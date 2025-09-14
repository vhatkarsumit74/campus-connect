package com.aura.connectcampus.colleges;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CollegeRepository extends JpaRepository<College, Long>, JpaSpecificationExecutor<College> {
}
