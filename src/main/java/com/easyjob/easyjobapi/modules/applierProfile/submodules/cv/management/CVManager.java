package com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.management;

import com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.models.CVDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CVManager {
    private final CVRepository cvRepository;

    public CVDAO saveToDatabase(CVDAO cv) {
        return cvRepository.save(cv);
    }
}