package com.chanlog.api.domain;

import com.chanlog.api.request.PostCreateByBody;
import com.chanlog.api.request.PostEdit;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    @Builder
    private Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static Post from(PostCreateByBody postCreateByBody){
        Post post = Post.builder()
                .title(postCreateByBody.getTitle())
                .content(postCreateByBody.getContent())
                .build();

        return post;
    }

    public PostEditor.PostEditorBuilder toEditor(){
        return PostEditor.builder()
                .title(title)
                .content(content);
    }

    public void edit(PostEditor postEditor){
        title = postEditor.getTitle();
        content = postEditor.getContent();
    }

}
