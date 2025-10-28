package com.examly.springapp.dto;

import com.examly.springapp.model.User;

public class AuthorDTO {
    private Long id;
    private String username;
    private String profilePicture;
    private String bio;
    private String role;

    public AuthorDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.profilePicture = user.getProfilePicture();
        this.bio = user.getBio();
        this.role = user.getRole().name();
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getProfilePicture() { return profilePicture; }
    public String getBio() { return bio; }
    public String getRole() { return role; }
}
