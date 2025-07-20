package com.example.socialmediaapi.repository;

import com.example.socialmediaapi.model.Post;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {
    Page<Post> findByTitleContainingOrTextContainingOrTagsContaining(String title,String text,String tags,Pageable pageable);
    @Query("{'$text':{'$search': ?0}}")
    Page<Post>searchByText(String searchTerm,Pageable pageable);

    @Query("{'_id':?0}")
    @Update("{'$inc': {'likes': 1}}")
    void incrementLikes(String id);
}
