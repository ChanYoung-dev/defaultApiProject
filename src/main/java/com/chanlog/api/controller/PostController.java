package com.chanlog.api.controller;

import com.chanlog.api.exception.InvalidRequest;
import com.chanlog.api.request.PostCreate;
import com.chanlog.api.request.PostCreateByBody;
import com.chanlog.api.request.PostEdit;
import com.chanlog.api.response.PostResponse;
import com.chanlog.api.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/test/posts")
    public String get(){
        return "Hello World";
    }

    @PostMapping("/v1/posts")
    public String v1Post(@RequestParam String title, @RequestParam String content){
        log.info("title = {}, content = {}", title, content);
        return "Hello World";
    }

    @PostMapping("/v2/posts")
    public String v2Post(@RequestParam Map<String, String> params){
        log.info("params = {}", params);
        //String title = params.get("title");
        return "Hello World";
    }

    @PostMapping("/v3/posts")
    public String v3Post(@ModelAttribute PostCreate params){ // @ModelAttribute 생략가능
        log.info("params = {}", params.toString());
        //String title = params.get("title");
        return "Hello World";
    }

    @PostMapping("/v4/posts")
    public Map<String, String> v4Post(@RequestBody PostCreateByBody params){
        log.info("params = {}", params.toString());
        //String title = params.get("title");
        return Map.of();
    }


    @PostMapping("/posts/valid")
    public String postsValid(@Valid @RequestBody PostCreateByBody params){
        log.info("params = {}", params.toString());

        String title = params.getTitle();
        if( title == null || title.equals("")){
            throw new RuntimeException("타이틀 값이 없습니다!");
        }
        return "Hello World";
    }

    /**
     * {@link BindingResult}를 파라미터에 넣는순간 @Valid의 Exception은 터지지않는다
     */
    @PostMapping("/posts/valid/bindingResult")
    public Map<String, String> postsValid(@Valid @RequestBody PostCreateByBody params, BindingResult result){
        if(result.hasErrors()){
            List<FieldError> fieldErrors = result.getFieldErrors();
            FieldError firstFieldError = fieldErrors.get(0);
            String fieldName = firstFieldError.getField(); //title
            String errorMessage = firstFieldError.getDefaultMessage(); // 에러메세지

            Map<String, String> error = new HashMap<>();
            error.put(fieldName, errorMessage);
            return error;
        }

        return Map.of();
    }

    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreateByBody request){
        request.validate();

        postService.write(request);
    }

    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable(name = "postId") Long id){
        return postService.get(id);
    }

    @GetMapping("/posts")
    public List<PostResponse> getList(Pageable page){
        return postService.getList(page);
    }

    @GetMapping("/posts/noPageable")
    public List<PostResponse> getListNoPageable(){
        return postService.getList();
    }

    @PatchMapping("/posts/{postId}")
    public PostResponse edit(@PathVariable Long postId, @RequestBody @Valid PostEdit request){
        return postService.edit(postId, request);
        // SSR인 경우 PGR 패턴 적용
        // return "redirect:/posts/{postId}"
    }

    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable Long postId){

        postService.delete(postId);
        // SSR인 경우 PGR 패턴 적용
        // return "redirect:/posts/{postId}"
    }


}
