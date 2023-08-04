package com.example.whatsappclone.Models;

public class MessageModel {
    String messageId,message,messageUserId;
    Long timestamp;

    public MessageModel(String messageId, String message, Long timestamp,String messageUserId) {
        this.messageId = messageId;
        this.message = message;
        this.timestamp = timestamp;

        this.messageUserId = messageUserId;
    }

    public MessageModel(String messageId, String message) {
        this.messageId = messageId;
        this.message = message;
    }
    public MessageModel(){}

    public String getMessageUserId() {
        return messageUserId;
    }

    public void setMessageUserId(String messageUserId) {
        this.messageUserId = messageUserId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
