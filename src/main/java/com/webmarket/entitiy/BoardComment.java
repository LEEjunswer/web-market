package com.webmarket.entitiy;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.springframework.context.annotation.Lazy;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name ="board_comment")
public class BoardComment {
    @Id
    @Column(name = "board_commwnt_id")
    private Long id;

    @Comment("게시글 번호 외래키")
    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "board_id",nullable = false)
    private Board board;

    @Comment("작성자, 외래키")
    @ManyToOne(fetch =  FetchType.LAZY)
    @Column(name = "member_id",nullable = false)
    private Member member;

    @Comment("댓글 내용")
    @NotNull(message = "댓글읜 널이 들어올 수 없습니다")
    @Size(min = 2,max = 100,message = "최소 글자는 2글자 최대글자는 100글자 입니다.")
    private String comment;

    @Comment("생성 시간")
    private LocalDateTime createTime;

    @Comment("삭제 유무 디폴트 false")
    private boolean IsDelete = false;

    @PrePersist
    private  void prePersist(){
        if(createTime == null){
           this.createTime = LocalDateTime.now();
        }
    }
}

