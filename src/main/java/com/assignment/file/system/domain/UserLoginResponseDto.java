package com.assignment.file.system.domain;

public class UserLoginResponseDto {
    private String tokenId;

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public UserLoginResponseDto() {
    }

    public UserLoginResponseDto(String tokenId) {
        this.tokenId = tokenId;
    }
}
