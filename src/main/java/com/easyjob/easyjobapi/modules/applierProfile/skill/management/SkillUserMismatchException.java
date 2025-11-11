package com.easyjob.easyjobapi.modules.applierProfile.skill.management;

public class SkillUserMismatchException extends RuntimeException {
    private final static String DEFAULT_MESSAGE = "Skill user mismatch.";
    public SkillUserMismatchException() {
        super(DEFAULT_MESSAGE);
    }
}
