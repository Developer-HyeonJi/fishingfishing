package com.hanium.fishing.api.domain.repository;

import com.hanium.fishing.api.domain.entity.Mapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MappingRepository extends JpaRepository<Mapping, Long> {
}
