package com.easyjob.easyjobapi.core.firm.management;

import com.easyjob.easyjobapi.core.firm.models.FirmDAO;
import com.easyjob.easyjobapi.core.user.models.UserDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FirmManager {
    private final FirmRepository firmRepository;

    public FirmDAO saveToDatabase(FirmDAO firm) {
        return firmRepository.save(firm);
    }

    public Optional<FirmDAO> findFirmByUser(UserDAO user) {
        return firmRepository.findFirmByUser(user);
    }

    public Page<FirmDAO> findAll(Specification<FirmDAO> spec, Pageable pageable) {
        return firmRepository.findAll(spec, pageable);
    }
}