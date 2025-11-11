package com.easyjob.easyjobapi.modules.applierProfile.skill.management;

public class SkillNotFoundException extends RuntimeException {
    private final static String DEFAULT_MESSAGE = "Skill not found";
    public SkillNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}
