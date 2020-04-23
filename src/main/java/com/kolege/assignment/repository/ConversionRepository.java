package com.kolege.assignment.repository;

import com.kolege.assignment.domain.Conversion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ConversionRepository extends JpaRepository<Conversion,Long>, PagingAndSortingRepository<Conversion, Long>,
        QuerydslPredicateExecutor<Conversion>{
}
