package com.chanlog.api.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.util.StringUtils;

@Getter
public class PostEditor {
    private String title;
    private String content;

    @Builder
    public PostEditor(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static class PostEditorBuilder {
        private String title;
        private String content;

        PostEditorBuilder() {
        }

        public PostEditor.PostEditorBuilder title(final String title) {
            if (content != null) { // 여기에서 null 체크
                this.title = title;
            }
            return this;
        }

        public PostEditor.PostEditorBuilder content(final String content) {
            if (content != null) { // 여기에서 null 체크
                this.content = content;
            }
            return this;
        }

        public PostEditor build() {
            return new PostEditor(this.title, this.content);
        }

        public String toString() {
            return "PostEditor.PostEditorBuilder(title=" + this.title + ", content=" + this.content + ")";
        }
    }
}
