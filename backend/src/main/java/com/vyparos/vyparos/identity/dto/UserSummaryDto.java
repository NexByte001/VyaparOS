package com.vyparos.vyparos.identity.dto;

import com.vyparos.vyparos.identity.entity.User;

public class UserSummaryDto {

    private Long id;
    private String email;
    private String fullName;
    private String phone;

    public UserSummaryDto() {}

    public UserSummaryDto(Long id, String email, String fullName, String phone) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.phone = phone;
    }

    public static UserSummaryDto fromEntity(User user) {
        return new UserSummaryDto(user.getId(), user.getEmail(), user.getFullName(), user.getPhone());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
