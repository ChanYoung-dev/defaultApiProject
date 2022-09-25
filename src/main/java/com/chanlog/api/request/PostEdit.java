package com.chanlog.api.request;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class PostEdit {

    public final String title;

    public final String content;


    @Override
    public String toString() {
        return "PostEdit{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    @Builder
    public PostEdit(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public PostCreateByBody changeTitle(String title){
        return PostCreateByBody.builder()
                .title(title)
                .content(content)
                .build();
    }
}
