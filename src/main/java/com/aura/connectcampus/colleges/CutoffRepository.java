package com.aura.connectcampus.colleges;

import com.aura.connectcampus.common.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface CutoffRepository extends JpaRepository<Cutoff, Long> {

    @Query("""
        select distinct c.program.college
        from Cutoff c
        where c.exam = :exam
          and c.year = :year
          and c.percentileCutoff <= :studentPercentile
    """)
    List<College> findEligible(@Param("exam") Exam exam,
                               @Param("year") short year,
                               @Param("studentPercentile") BigDecimal studentPercentile);
}
