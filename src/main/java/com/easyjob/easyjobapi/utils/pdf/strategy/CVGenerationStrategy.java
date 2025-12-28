package com.easyjob.easyjobapi.utils.pdf.strategy;

import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfileCVResponse;

public interface CVGenerationStrategy {
    byte[] generatePDF(ApplierProfileCVResponse cvData) throws Exception;

    String getStyleName();
}