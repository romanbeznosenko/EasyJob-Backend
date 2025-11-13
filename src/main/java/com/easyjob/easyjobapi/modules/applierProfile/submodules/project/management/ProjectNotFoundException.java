package com.easyjob.easyjobapi.modules.applierProfile.submodules.project.management;

public class ProjectNotFoundException extends RuntimeException {
    private final static String DEFAULT_MESSAGE = "Project not found";
    public ProjectNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}
