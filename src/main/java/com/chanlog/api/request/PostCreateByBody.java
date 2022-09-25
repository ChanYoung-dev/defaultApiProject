package com.chanlog.api.request;

import com.chanlog.api.exception.InvalidRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class PostCreateByBody {

    @NotBlank(message = "타이틀을 입력해주세요.")
    public final String title;

    @NotBlank
    public final String content;


    @Override
    public String toString() {
        return "PostCreate{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    @Builder
    public PostCreateByBody(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public PostCreateByBody changeTitle(String title){
        return PostCreateByBody.builder()
                .title(title)
                .content(content)
                .build();
    }

    public void validate() {
        if (title.contains("바보")) {
            throw new InvalidRequest("title","제목에 바보를 포함할 수 없습니다.");
        }
    }
}
