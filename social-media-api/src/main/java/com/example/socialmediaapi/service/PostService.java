package com.example.socialmediaapi.service;

import com.example.socialmediaapi.config.AIClient;
import com.example.socialmediaapi.config.AWSConfig;
import com.example.socialmediaapi.model.*;
import com.example.socialmediaapi.repository.PostRepository;
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final S3Client s3Client;
    private final S3PresignedUriService s3PresignedUriService;
    private final AIClient aiClient;
    public Post createPost(String title, String text, List<String> tags, MultipartFile mediaFile
    ,String userId,String name,String email) throws IOException {
        String fileName = storeFileInS3(mediaFile);
        MediaType mediaType=mediaFile.getContentType().startsWith("video/")?MediaType.VIDEO:
                mediaFile.getContentType().startsWith("image/")?MediaType.IMAGE:null;
        Post post=new Post();
        post.setTitle(title);
        post.setText(text);
        post.setTags(tags);
        post.setMediaUri(fileName);
        post.setMediaType(mediaType);
        PostCreator creator=PostCreator.builder()
                .Id(userId)
                .name(name).
        build();
        post.setCreator(creator);
        post.setLikes(0);
        post.setCreatedAt(java.time.LocalDateTime.now());
        return postRepository.save(post);
    }

    private String storeFileInS3(MultipartFile mediaFile) throws IOException {
        String fileName= UUID.randomUUID().toString()+"-"+ mediaFile.getOriginalFilename();
        if(!mediaFile.isEmpty()){
            //Save the file to the S3 bucket
            //to upload a file in s3 we need to create one putObject
            PutObjectRequest putObjectRequest= PutObjectRequest.builder()
                    .bucket(AWSConfig.BUCKET_NAME)
                    .key(fileName)
                    .build();
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(mediaFile.getBytes()));

        }
        return fileName;
    }

    public Page<Post> getAllPost(int page, int size,String searchCriteria){
        Sort sort= Sort.by(Sort.Direction.DESC,"id");
        var postList=StringUtils.isEmpty(searchCriteria)?postRepository.findAll(PageRequest.of(page, size,sort)):postRepository.searchByText(searchCriteria,PageRequest.of(page,size,sort));
        postList.forEach(post -> {
            if(post.getMediaUri()!=null) post.setPresignedUri(s3PresignedUriService.generatePresignedUri(post.getMediaUri()));
            post.getCreator().setProfilePhoto(s3PresignedUriService.generatePresignedUri(post.getCreator().getId()+"-profile"));
        });

        return postList;
    }
    public Post getPostById(String id){
        var post=postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        if(post.getMediaUri()!=null) post.setPresignedUri(s3PresignedUriService.generatePresignedUri(post.getMediaUri()));
        post.getCreator().setProfilePhoto(s3PresignedUriService.generatePresignedUri(post.getCreator().getId()+"-profile"));
        return post;
    }
    public Post updatePost(String id, String title, String text, List<String>tags, MultipartFile mediaFile) throws IOException {
        Post post=getPostById(id);
        if(post.getMediaUri()!=null){
            s3Client.deleteObject(builder -> builder.bucket(AWSConfig.BUCKET_NAME).key(post.getMediaUri()));
        }

        String fileName = storeFileInS3(mediaFile);
        post.setMediaUri(fileName);

        MediaType mediaType=mediaFile.getContentType().startsWith("video/")?MediaType.VIDEO:
                mediaFile.getContentType().startsWith("image/")?MediaType.IMAGE:null;
        post.setMediaType(mediaType);

        post.setTitle(title);
        post.setText(text);

        post.setTags(tags);
        return postRepository.save(post);
    }
    public void deletePost(String id){
        postRepository.deleteById(id);
    }

    public void likePost(String id) {
        postRepository.incrementLikes(id);
    }

    public AIResponse suggestPostContent(String description) {
        AIRequest request = new AIRequest();
        request.setDescription(description);
        return aiClient.generate(request);
    }

}
