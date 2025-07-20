package com.example.socialmediaapi.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostCreator {
    private String Id;
    private String name;
    private String profilePhoto;
}
