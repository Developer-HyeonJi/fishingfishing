package com.hanium.fishing.api.domain.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private Users users;

    @Column(name = "comment_content", nullable = false)
    private String commentContent;

    @Column(name = "commentcreated_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date commentCreatedDate;

    @Column(name = "commentmodified_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date commentModifiedDate;

    @Builder
    public Comment(Board board, Users users, String commentContent, Date commentCreatedDate, Date commentModifiedDate) {
        this.board = board;
        this.users = users;
        this.commentContent = commentContent;
        this.commentCreatedDate = commentCreatedDate;
        this.commentModifiedDate = commentModifiedDate;
    }

}