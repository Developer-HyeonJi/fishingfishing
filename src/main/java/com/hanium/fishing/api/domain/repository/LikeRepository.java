package com.hanium.fishing.api.domain.repository;

import com.hanium.fishing.api.domain.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Likes, Long> {
}
