package com.chanlog.api.response;

import com.chanlog.api.domain.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostResponse {

    private final Long id;
    private final String title;
    private final String content;

    public PostResponse(Post post){
        this.id = post.getId();
        this.title = post.getTitle().substring(0, Math.min(post.getTitle().length(),10));
        this.content = post.getContent();
    }

    public static PostResponse from(Post post){
        return new PostResponse(post);
    }

}
