package uk.gov.hmcts.reform.demo.models;

public class FriendDTO {

    private String username;
    private String profilePicture;

    public FriendDTO() {
    }

    public FriendDTO(String username, String profilePicture) {
        this.username = username;
        this.profilePicture = profilePicture;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
