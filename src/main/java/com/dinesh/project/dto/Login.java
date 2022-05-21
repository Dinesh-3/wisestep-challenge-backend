package com.dinesh.project.dto;

public class Login {
    private String email;
    private String otp;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    @Override
    public String toString() {
        return "{\"Login\":{"
                + "\"email\":\"" + email + "\""
                + ", \"otp\":\"" + otp + "\""
                + "}}";
    }
}
