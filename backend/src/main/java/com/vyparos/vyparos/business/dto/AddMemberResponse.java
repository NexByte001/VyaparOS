package com.vyparos.vyparos.business.dto;

public class AddMemberResponse {

    private String actionType; // "ADDED" or "INVITED"
    private String message;
    private MemberSummaryDto member;
    private StaffInvitationDto invitation;
    private String inviteUrl;

    public AddMemberResponse() {}

    public static AddMemberResponse added(MemberSummaryDto member, String message) {
        AddMemberResponse res = new AddMemberResponse();
        res.setActionType("ADDED");
        res.setMessage(message != null ? message : "Staff member added directly to workspace");
        res.setMember(member);
        return res;
    }

    public static AddMemberResponse invited(StaffInvitationDto invitation, String message) {
        AddMemberResponse res = new AddMemberResponse();
        res.setActionType("INVITED");
        res.setMessage(message != null ? message : "Staff invitation created successfully");
        res.setInvitation(invitation);
        res.setInviteUrl(invitation != null ? invitation.getInviteUrl() : null);
        return res;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MemberSummaryDto getMember() {
        return member;
    }

    public void setMember(MemberSummaryDto member) {
        this.member = member;
    }

    public StaffInvitationDto getInvitation() {
        return invitation;
    }

    public void setInvitation(StaffInvitationDto invitation) {
        this.invitation = invitation;
    }

    public String getInviteUrl() {
        return inviteUrl;
    }

    public void setInviteUrl(String inviteUrl) {
        this.inviteUrl = inviteUrl;
    }
}
