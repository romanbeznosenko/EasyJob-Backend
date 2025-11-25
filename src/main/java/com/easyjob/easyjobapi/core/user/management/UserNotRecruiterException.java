package com.easyjob.easyjobapi.core.user.management;

public class UserNotRecruiterException extends RuntimeException {
    private final static String DEFAULT_MESSAGE = "Logged user is not recruiter.";
    public UserNotRecruiterException() {
        super(DEFAULT_MESSAGE);
    }
}
