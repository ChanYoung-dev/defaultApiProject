package com.chanlog.api.service;

import com.chanlog.api.domain.Post;
import com.chanlog.api.exception.PostNotFound;
import com.chanlog.api.repository.PostRepository;
import com.chanlog.api.request.PostCreateByBody;
import com.chanlog.api.request.PostEdit;
import com.chanlog.api.response.PostResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean(){
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void servicePost(){
        //given
        PostCreateByBody postCreate = PostCreateByBody.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        //when
        postService.write(postCreate);

        //then
        Assertions.assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test2(){

        //given
        Post requestPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(requestPost);

        //when
        PostResponse post = postService.get(requestPost.getId());

        //then
        assertNotNull(post);
        assertEquals(1L,postRepository.count());
        assertEquals("foo", post.getTitle());
        assertEquals("bar", post.getContent());

    }

    @Test
    @DisplayName("글 1개 조회 실패")
    void test2_1(){

        //given
        Post requestPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(requestPost);

        //when

        //then
        assertThrows(PostNotFound.class, () -> {
            postService.get(requestPost.getId() + 1L);
        });

    }

    @Test
    @DisplayName("글 여러개 조회")
    void test3(){

        //given
        postRepository.saveAll(List.of(
                Post.builder()
                        .title("foo1")
                        .content("bar1")
                        .build(),
                Post.builder()
                        .title("foo1")
                        .content("bar1")
                        .build()
        ));


        //when
        List<PostResponse> posts = postService.getList();

        //then
        assertEquals(2L,posts.size());

    }

    @Test
    @DisplayName("글 1페이지 조회")
    void test4(){

        //given
        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> {
                    return Post.builder()
                            .title("호들맨 제목 " + i)
                            .content("반포자이 " + i)
                            .build();
                })
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);


        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC,"id"));

        //when
        List<PostResponse> posts = postService.getList(pageable);

        //then
        assertEquals(5,posts.size());
        assertEquals("호들맨 제목 30", posts.get(0).getTitle());
        assertEquals("호들맨 제목 26", posts.get(4).getTitle());
    }


    @Test
    @DisplayName("글 제목 수정")
    void test5(){

        //given
        Post post = Post.builder()
                .title("호들맨")
                .content("반포자이")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("호들걸")
                .build();


        //when
        postService.edit(post.getId(), postEdit);

        //then
        Post changedPost = postRepository.findById(post.getId())
                        .orElseThrow(() -> new RuntimeException("글이 존재하지않습니다. id=" + post.getId()));

        assertEquals("호들걸", changedPost.getTitle());
        assertEquals("반포자이", changedPost.getContent());

    }

    @Test
    @DisplayName("글 삭제")
    void test6(){

        //given
        Post post = Post.builder()
                .title("호들맨")
                .content("반포자이")
                .build();
        postRepository.save(post);

        //when
        postService.delete(post.getId());

        //then
        assertEquals(0, postRepository.count());

    }
}