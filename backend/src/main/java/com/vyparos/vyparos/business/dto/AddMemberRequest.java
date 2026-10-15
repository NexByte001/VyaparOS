package com.vyparos.vyparos.business.dto;

import com.vyparos.vyparos.business.entity.BusinessRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AddMemberRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Valid email is required")
    private String email;

    @NotNull(message = "Role is required (OWNER, ADMIN, STAFF)")
    private BusinessRole role;

    private String fullName;

    private String phone;

    private String jobTitle;

    private String department;

    public AddMemberRequest() {}

    public AddMemberRequest(String email, BusinessRole role) {
        this.email = email;
        this.role = role;
    }

    public AddMemberRequest(String email, BusinessRole role, String fullName, String phone) {
        this.email = email;
        this.role = role;
        this.fullName = fullName;
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BusinessRole getRole() {
        return role;
    }

    public void setRole(BusinessRole role) {
        this.role = role;
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

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
