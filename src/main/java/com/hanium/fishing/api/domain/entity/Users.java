package com.hanium.fishing.api.domain.entity;

import com.hanium.fishing.api.domain.entity.Board;
import com.hanium.fishing.api.domain.entity.Likes;
import com.hanium.fishing.common.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Users extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "users_id")
    private Long id;
    private String nickName;
    private String userId;
    private String password;
    private String profileImageUrl;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Likes> likes = new ArrayList<>();

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
