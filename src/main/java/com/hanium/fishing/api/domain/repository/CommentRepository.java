package com.hanium.fishing.api.domain.repository;

import com.hanium.fishing.api.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // Add custom query methods if needed...
}
