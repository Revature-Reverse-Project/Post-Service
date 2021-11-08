package com.reverse.postservice.controllers;

import com.reverse.postservice.models.Post;
import com.reverse.postservice.models.Like;
import com.reverse.postservice.models.dto.FullPost;
import com.reverse.postservice.models.dto.PostCreationDto;
import com.reverse.postservice.services.PostDtoService;
import com.reverse.postservice.services.PostService;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
* TODO:
*  When front-end retrieves a post, we need to return:
*       -A list of user IDs who liked the post.
*       -All comments on the post.
*       -All replies to each comment.
*
* TODO:
*  Add partial loading when retrieving a feed of posts.
*       -We should have it load in increments of 5, and continue to load in increments of 5
*        when a "load more" button is pressed.
* */

@RestController
@RequestMapping(path = "/posts") //This being posts syncs with the current implementation of the gateway
public class PostController {

    PostService postService;
    PostDtoService postDtoService;

    @Autowired
    public PostController(PostService postService, PostDtoService postDtoService) {
        this.postService = postService;
        this.postDtoService = postDtoService;
    }

    @PostMapping(value = "/create")
    public ResponseEntity createPost(@RequestBody PostCreationDto post) {
        try {
            postDtoService.createPost(post);
            return new ResponseEntity(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<FullPost> getPost(@PathVariable int id) {
        FullPost post = this.postDtoService.getPostById(id);

        if(post != null) {
            return ResponseEntity.ok().body(post);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/like")
    public ResponseEntity likePost(@RequestBody Like like) {
        try {
            postService.likePost(like);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @PatchMapping(value = "/edit")
    public ResponseEntity editPost(@RequestBody PostCreationDto post) {
        try {
            postDtoService.updatePost(post);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity deletePost(@PathVariable int id) {
        try {
            postService.deletePost(id);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping()
    public ResponseEntity<List<Post>> getAllPosts(){
        List<Post> posts;

        try {
            posts = postService.getAllPosts();
            return ResponseEntity.ok().body(posts);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
}
