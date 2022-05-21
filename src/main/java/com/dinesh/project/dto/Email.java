package com.dinesh.project.dto;

public class Email {
    private String from;

    private String senderName;
    private String to;
    private String subject;
    private String body;

    public Email(String to, String subject, String body) {
        this.to = to;
        this.subject = subject;
        this.body = body;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    @Override
    public String toString() {
        return "{\"Email\":{"
                + "\"from\":\"" + from + "\""
                + ", \"senderName\":\"" + senderName + "\""
                + ", \"to\":\"" + to + "\""
                + ", \"subject\":\"" + subject + "\""
                + ", \"body\":\"" + body + "\""
                + "}}";
    }
}
