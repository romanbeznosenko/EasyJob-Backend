package com.easyjob.easyjobapi.modules.applierProfile.submodules.project.management;

public class ProjectUserMismatchException extends RuntimeException {
    private final static String DEFAULT_MESSAGE = "Project User mismatch";
    public ProjectUserMismatchException() {
        super(DEFAULT_MESSAGE);
    }
}
