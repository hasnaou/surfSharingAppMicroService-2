package org.surfsharing.postservice.controller;


import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.surfsharing.postservice.LikeClient.LikeClient;
import org.surfsharing.postservice.dto.LikeDto;
import org.surfsharing.postservice.dto.PostDto;
import org.surfsharing.postservice.service.impl.IpostService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostController {

    @Autowired
    private IpostService postService;
    @Autowired
    private LikeClient likeClient;
    @PostMapping
    public ResponseEntity<PostDto> addPost(@RequestBody PostDto postDto, HttpServletRequest request){
        Long adminid = Long.parseLong(request.getHeader("hid"));
        postDto.setUserId(adminid);
        PostDto post = postService.addPost(postDto);
        return new ResponseEntity<>(post, HttpStatus.CREATED);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id,HttpServletRequest request){
       try{
           Long adminid = Long.parseLong(request.getHeader("hid"));
           postService.deletePost(id,adminid);
           return ResponseEntity.ok("Post with id " + id + " was deleted successfully");
       }catch (Exception e){
           return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
       }
    }
    @GetMapping("allmyposts")
    public ResponseEntity<List<PostDto>> getAllMyPosts(HttpServletRequest request){
        Long adminid = Long.parseLong(request.getHeader("hid"));
        List<PostDto> posts = postService.getAllPosts(adminid);
        return new ResponseEntity<>(posts,HttpStatus.OK);
    }
    @GetMapping("feed")
    public ResponseEntity<List<PostDto>> feed(HttpServletRequest request){
        Long adminid = Long.parseLong(request.getHeader("hid"));
        List<PostDto> posts = postService.Feed(adminid);
        return new ResponseEntity<>(posts,HttpStatus.OK);
    }
    @CircuitBreaker(name = "likeservice",fallbackMethod = "likePostFallback")
    @GetMapping("likepost/{idPost}")
    public ResponseEntity<LikeDto> likePost(@PathVariable("idPost") Long idPost,HttpServletRequest request){
        Long adminid = Long.parseLong(request.getHeader("hid"));
        LikeDto likeDto = likeClient.addLike(idPost,adminid);
        return new ResponseEntity<>(likeDto,HttpStatus.OK);
    }

    public ResponseEntity<String> likePostFallback(Exception e){
        return new ResponseEntity<>("Reaction Service is down",HttpStatus.OK);
    }

}
