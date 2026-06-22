package com.example.quanlychitieu.model;

public class PasswordResetOtp {
    private int id;
    private Integer userId;
    private String contact;
    private String otpCodeHash;
    private String purpose;
    private String expiresAt;
    private String verifiedAt;
    private String usedAt;
    private int attemptCount;
    private String status;
    private String token;
    private String createdAt;

    public PasswordResetOtp() {
    }

    public PasswordResetOtp(int id, Integer userId, String contact, String otpCodeHash,
                            String purpose, String expiresAt, String verifiedAt,
                            String usedAt, int attemptCount, String status,
                            String token, String createdAt) {
        this.id = id;
        this.userId = userId;
        this.contact = contact;
        this.otpCodeHash = otpCodeHash;
        this.purpose = purpose;
        this.expiresAt = expiresAt;
        this.verifiedAt = verifiedAt;
        this.usedAt = usedAt;
        this.attemptCount = attemptCount;
        this.status = status;
        this.token = token;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getOtpCodeHash() {
        return otpCodeHash;
    }

    public void setOtpCodeHash(String otpCodeHash) {
        this.otpCodeHash = otpCodeHash;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getVerifiedAt() {
        return verifiedAt;
    }

    public void setVerifiedAt(String verifiedAt) {
        this.verifiedAt = verifiedAt;
    }

    public String getUsedAt() {
        return usedAt;
    }

    public void setUsedAt(String usedAt) {
        this.usedAt = usedAt;
    }

    public int getAttemptCount() {
        return attemptCount;
    }

    public void setAttemptCount(int attemptCount) {
        this.attemptCount = attemptCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}