package com.chanlog.api.controller;

import com.chanlog.api.domain.Post;
import com.chanlog.api.repository.PostRepository;
import com.chanlog.api.request.PostCreateByBody;
import com.chanlog.api.request.PostEdit;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void clean(){
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("/posts 요청시 Hello World를 출력한다")
    void testGet() throws Exception {
        //expected
        mockMvc.perform(MockMvcRequestBuilders.get("/test/posts"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Hello World"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("여러개의 post 조회 - pageable")
    void test7() throws Exception{
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



        //expected
        mockMvc.perform(MockMvcRequestBuilders.get("/posts?page=1&sort=id,desc")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", Matchers.is(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].title").value("호들맨 제목 30"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].content").value("반포자이 30"))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @DisplayName("/posts 요청시 Hello World를 출력한다")
    void testV1Post() throws Exception {
        //expected
        mockMvc.perform(
                    MockMvcRequestBuilders.post("/v1/posts")
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("title", "글 제목입니다")
                            .param("content", "글 내용입니다 하하")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Hello World"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("/posts 요청시 Hello World를 출력한다")
    void testV2Post() throws Exception {
        //expected
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/v2/posts")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("title", "글 제목입니다")
                                .param("content", "글 내용입니다 하하")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Hello World"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("/posts 요청시 Hello World를 출력한다")
    void testV3Post() throws Exception {
        //expected
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/v3/posts")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("title", "글 제목입니다")
                                .param("content", "글 내용입니다 하하")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Hello World"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("/posts 요청시 Hello World를 출력한다")
    void testV4Post() throws Exception {
        //given
        PostCreateByBody request = PostCreateByBody.builder()
                .title("제목입니다.")
                .content("내용입니다.").build();

        String json = objectMapper.writeValueAsString(request);

        System.out.println("json = " + json);


        //expected
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/v4/posts")
                                .contentType(MediaType.APPLICATION_JSON) //기본값
                                .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("{}"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("/posts 요청시 title은 비어있으면 안된다")
    void testValidRequestBody() throws Exception {
        //given
        PostCreateByBody request = PostCreateByBody.builder()
                .content("내용입니다.").build();

        String json = objectMapper.writeValueAsString(request);

        System.out.println("json = " + json);

        //expected
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/posts/valid")
                                .contentType(MediaType.APPLICATION_JSON) //기본값
                                .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("/posts 요청시 title은 비어있으면 안된다")
    void testValidRequestBodyBindingResult() throws Exception {
        //given
        PostCreateByBody request = PostCreateByBody.builder()
                .content("내용입니다.").build();

        String json = objectMapper.writeValueAsString(request);

        System.out.println("json = " + json);

        //expected
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/posts/valid/bindingResult")
                                .contentType(MediaType.APPLICATION_JSON) //기본값
                                .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("타이틀을 입력해주세요.")) //junit5 jsonPath
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("/posts 요청시 title은 비어있으면 안된다")
    void testValidRequestBodyHandler() throws Exception {
        //given
        PostCreateByBody request = PostCreateByBody.builder()
                .content("내용입니다.").build();

        String json = objectMapper.writeValueAsString(request);

        System.out.println("json = " + json);

        //expected
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/posts/valid")
                                .contentType(MediaType.APPLICATION_JSON) //기본값
                                .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("400")) //junit5 jsonPath
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("잘못된 요청입니다.")) //junit5 jsonPath
                .andExpect(MockMvcResultMatchers.jsonPath("$.validation[0].errorMessage").value("타이틀을 입력해주세요.")) //junit5 jsonPath
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("/posts 요청시 DB에 값이 저장된다")
    void test3() throws Exception{
        //given
        PostCreateByBody request = PostCreateByBody.builder()
                .title("제목입니다.")
                .content("내용입니다.").build();

        String json = objectMapper.writeValueAsString(request);

        System.out.println("json = " + json);

        //when
        mockMvc.perform(
                MockMvcRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        //then
        Assertions.assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("게시글 작성시 제목에 '바보'는 포함될수 없다.")
    void test3_1() throws Exception{
        //given
        PostCreateByBody request = PostCreateByBody.builder()
                .title("나는 바보입니다.")
                .content("내용입니다.").build();

        String json = objectMapper.writeValueAsString(request);

        System.out.println("json = " + json);

        //when
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());


    }

    @Test
    @DisplayName("10글자이상의 제목을 가진글 1개 조회")
    void test4() throws Exception{
        //given
        Post post = Post.builder()
                .title("123456789012345")
                .content("bar").build();
        postRepository.save(post);

        //expected
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/{postId}", post.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(post.getId())) //junit5 jsonPath
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("1234567890")) //junit5 jsonPath
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("bar")) //junit5 jsonPath
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @DisplayName("10 글자 이내 제목을 가진 글 1개 조회")
    void test5() throws Exception{
        //given
        Post post = Post.builder()
                .title("foo")
                .content("bar").build();
        postRepository.save(post);

        //expected
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/{postId}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(post.getId())) //junit5 jsonPath
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("foo")) //junit5 jsonPath
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("bar")) //junit5 jsonPath
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @DisplayName("존재하지않는 글 1개 조회")
    void test5_1() throws Exception{


        //expected
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/{postId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @DisplayName("여러개의 post 조회")
    void test6() throws Exception{
        //given
        Post post1 = Post.builder()
                .title("foo1")
                .content("bar1").build();
        postRepository.save(post1);

        Post post2 = Post.builder()
                .title("foo2")
                .content("bar2").build();
        postRepository.save(post2);

        //expected
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/noPageable")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(post1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].title").value("foo1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].content").value("bar1"))
                .andDo(MockMvcResultHandlers.print());

    }



    @Test
    @DisplayName("글 제목 수정")
    void test8() throws Exception {

        //given
        Post post = Post.builder()
                .title("호들맨")
                .content("반포자이")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("호들걸")
                .build();


        //expected
        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/{postId}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("호들걸"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("반포자이"))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @DisplayName("존재하지않는 글 1개 수정")
    void test8_1() throws Exception{

        PostEdit postEdit = PostEdit.builder()
                .title("호들걸")
                .content("반포자이")
                .build();


        //expected
        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/{postId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit))
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @DisplayName("게시글 삭제")
    void test9() throws Exception {

        //given
        Post post = Post.builder()
                .title("호들맨")
                .content("반포자이")
                .build();
        postRepository.save(post);


        //expected
        mockMvc.perform(MockMvcRequestBuilders.delete("/posts/{postId}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }



}