package com.chanlog.api.service;

import com.chanlog.api.domain.Post;
import com.chanlog.api.domain.PostEditor;
import com.chanlog.api.exception.PostNotFound;
import com.chanlog.api.repository.PostRepository;
import com.chanlog.api.request.PostCreateByBody;
import com.chanlog.api.request.PostEdit;
import com.chanlog.api.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void write(PostCreateByBody postCreate){
        // Case 1. 지정한 데이터 Entity
        // Case 2. 지정한 데이터의 primary_id
        // Case 3. 응답 필요없음 -> 클라이언트에서 모든 POST(글) 데이터 context를 잘 관리함
        Post post = Post.from(postCreate);
        postRepository.save(post);
    }

    public PostResponse get(Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFound());

        return PostResponse.from(post);
    }

    public List<PostResponse> getList() {
        return postRepository.findAll().stream()
                .map(post -> PostResponse.from(post))
                .collect(Collectors.toList());
    }


    public List<PostResponse> getList(Pageable pageable) {
//        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC,"id"));

        return postRepository.findAll(pageable).stream()
                .map(post -> PostResponse.from(post))
                .collect(Collectors.toList());
    }


    public PostResponse edit(Long id, PostEdit postEdit){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFound());

        PostEditor.PostEditorBuilder editorBuilder = post.toEditor();

        PostEditor postEditor = editorBuilder
                .title(postEdit.getTitle())
                .content(postEdit.getContent())
                .build();

        post.edit(postEditor);


        return new PostResponse(post);
    }


    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFound());

        postRepository.delete(post);
    }
}
