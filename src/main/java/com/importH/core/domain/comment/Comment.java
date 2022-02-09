package com.importH.core.domain.comment;

import com.importH.core.domain.account.Account;
import com.importH.core.domain.base.BaseTimeEntity;
import com.importH.core.domain.post.Post;
import com.importH.core.dto.post.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    public void updateComment(CommentDto.Request commentDto) {
        this.content = commentDto.getContent();
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
