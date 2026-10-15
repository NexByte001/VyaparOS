package com.vyparos.vyparos.business.dto;

import com.vyparos.vyparos.business.entity.BusinessRole;

public class UpdateMemberRequest {

    private BusinessRole role;
    private Boolean active;
    private String jobTitle;
    private String department;

    public UpdateMemberRequest() {}

    public UpdateMemberRequest(BusinessRole role, Boolean active) {
        this.role = role;
        this.active = active;
    }

    public BusinessRole getRole() {
        return role;
    }

    public void setRole(BusinessRole role) {
        this.role = role;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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
