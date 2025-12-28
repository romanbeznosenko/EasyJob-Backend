package com.easyjob.easyjobapi.utils.pdf;

import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfileCVResponse;

public interface CVGenerationStrategy {
    byte[] generatePDF(ApplierProfileCVResponse cvData) throws Exception;

    String getStyleName();
}