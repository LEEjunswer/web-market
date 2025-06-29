package com.webmarket.entitiy;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Table(name = "board")
@Getter
@Setter
@NoArgsConstructor
public class Board {

    @Id
    @Comment("게시글 고유 id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id",nullable = false,updatable = false)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    
    @Comment("게시글 제목")
    @NotNull(message = "게시글 제목은 널이 들어올 수 없습니다")
    @NotBlank(message = "게시글 제목은 공백일 수 없습니다")
    @Size(min = 2, message = "게시글 제목은 최소 2글자 이상이어야 합니다")
    private String title;

    @Comment("게시글 내용")
    @NotNull(message = "게시글 내용은 널이 들어올 수 없습니다")
    @NotBlank(message = "게시글 내용은 공백일 수 없습니다")
    @Size(min = 2, message = "게시글 내용은 최소 2글자 이상이여야 합니다.")
    private String content;

    @Comment("게시글 등록일자")
    @Column(updatable = false)
    private LocalDateTime createTime;

    @Comment("게시글 삭제일자")
    private LocalDateTime deleteTime;

    @Comment("삭제를 했는가?")
    @Column(columnDefinition = "false")
    private boolean isDelete;
}
