package org.surfsharing.reactionservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.surfsharing.reactionservice.dto.LikeDto;
import org.surfsharing.reactionservice.entity.Likes;
import org.surfsharing.reactionservice.services.ILikeservice;

import java.util.List;

@RestController
@RequestMapping("/api/v1/likes")
@RequiredArgsConstructor
public class LikeController {
    @Autowired
    private Environment environment;
    private final ILikeservice likeService;
    @GetMapping("/add/{idPost}/{idUser}")
    public ResponseEntity<LikeDto> addLike(@PathVariable("idPost") Long idPost,@PathVariable("idUser") Long idUser) {
        LikeDto likeDto = new LikeDto();
        likeDto.setIdPost(idPost);
        likeDto.setIdUser(idUser);
        Likes savedLike = likeService.addLike(likeDto);
        String port = environment.getProperty("local.server.port");
        System.out.println(port);
        return new ResponseEntity<>(likeDto, HttpStatus.CREATED);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Likes>> getLikesByUserId(@PathVariable Long userId) {
        List<Likes> likes = likeService.getLikesByUserId(userId);
        return new ResponseEntity<>(likes, HttpStatus.OK);
    }
    //needed
    @GetMapping("/post/{contentId}")
    public ResponseEntity<?> getLikesByContentId(@PathVariable Long contentId) {
        List<Likes> likes = likeService.getLikesByContentId(contentId);
        Integer likesCounter = likes.size();
        return new ResponseEntity<>(likesCounter, HttpStatus.OK);
    }
//    @GetMapping("/all")
//    public ResponseEntity<List<Likes>> getAllLikes() {
//        List<Likes> likes = likeService.getAllLikes();
//        return new ResponseEntity<>(likes, HttpStatus.OK);
//    }
    // Ajoutez d'autres méthodes en fonction des besoins
}
