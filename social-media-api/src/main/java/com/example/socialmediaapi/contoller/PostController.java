package com.example.socialmediaapi.contoller;

import com.example.socialmediaapi.model.AIResponse;
import com.example.socialmediaapi.model.Post;
import com.example.socialmediaapi.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/posts")
@AllArgsConstructor
public class PostController {
    private final PostService postService;
    @PostMapping
    public Post createPost(@RequestParam String title,
                           @RequestParam String text,
                           @RequestParam List<String> tags,
                           @RequestParam(value = "mediaFile",required=false)MultipartFile mediaFile,
                           @RequestHeader("X-User-Id") String userId,
                           @RequestHeader("X-User-Name") String name,
                           @RequestHeader("X-User-Email")String email) throws IOException {
        return postService.createPost(title,text,tags,mediaFile,userId,name,email);
    }
    @GetMapping
    public Page<Post> getAllPosts(
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "10")int size,
            @RequestParam(defaultValue = "")String searchCriteria
    ){
        return postService.getAllPost(page,size,searchCriteria);
    }
    @GetMapping("/{id}")
    public Post getPostById(@PathVariable String id){
        return postService.getPostById(id);
    }
    @PutMapping("/{id}")
    public Post updatePost(@RequestParam String title,
                           @RequestParam String text,
                           @RequestParam List<String>tags, @PathVariable String id,
                           @RequestParam(value = "mediaFile",required=false)MultipartFile mediaFile) throws IOException {
        return postService.updatePost(id,title,text,tags,mediaFile);
    }
    @DeleteMapping("/{id}/delete")
    public void deletePost(@PathVariable String id){
        postService.deletePost(id);
    }
    @PostMapping("/{id}/like")
    public void likePost(@PathVariable String id){
        postService.likePost(id);
    }

    @GetMapping("/test")
    public String test(){
        return "Hello from post-service";
    }

    @PostMapping("/generatePostIdea")
    public AIResponse generatePostIdea(@RequestParam String description) {
        return postService.suggestPostContent(description);
    }
}
