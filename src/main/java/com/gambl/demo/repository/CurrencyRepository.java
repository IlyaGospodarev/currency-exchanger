package com.gambl.demo.repository;

import com.gambl.demo.domain.entity.CurrencyEntity;
import com.gambl.demo.domain.entity.CurrencyId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends JpaRepository<CurrencyEntity, CurrencyId> {
}
