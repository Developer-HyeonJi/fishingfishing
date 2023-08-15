package com.hanium.fishing.api.domain.repository;

import com.hanium.fishing.api.domain.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
