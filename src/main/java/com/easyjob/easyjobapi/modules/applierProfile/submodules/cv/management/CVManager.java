package com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.management;

import com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.models.CVDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CVManager {
    private final CVRepository cvRepository;

    public CVDAO saveToDatabase(CVDAO cv) {
        return cvRepository.save(cv);
    }

    public Page<CVDAO> findAll(Specification<CVDAO> specification, Pageable pageable) {
        return cvRepository.findAll(specification, pageable);
    }
}