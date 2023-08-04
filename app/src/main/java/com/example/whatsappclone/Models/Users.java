package com.example.whatsappclone.Models;

public class Users {
    String profilePic, userName, userEmail, userPassword,userId, lastMessage,about;

    public Users(String profilePic, String userName, String userEmail, String userPassword, String userId, String lastMessage, String about) {
        this.profilePic = profilePic;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userId = userId;
        this.lastMessage = lastMessage;
        this.about = about;
    }
    public Users(){}

    //SignUp constructor
    public Users(String userName,String userEmail,String userPassword){
        this.userName = userName;
        this.userEmail  = userEmail;
        this.userPassword = userPassword;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }



    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
