package com.easyjob.easyjobapi.core.firm.services;

import com.easyjob.easyjobapi.core.firm.management.FirmManager;
import com.easyjob.easyjobapi.core.firm.management.FirmNotFoundException;
import com.easyjob.easyjobapi.core.firm.models.FirmDAO;
import com.easyjob.easyjobapi.core.user.management.UserNotRecruiterException;
import com.easyjob.easyjobapi.core.user.models.UserDAO;
import com.easyjob.easyjobapi.files.storage.services.StorageService;
import com.easyjob.easyjobapi.utils.enums.UserTypeEnum;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class FirmUploadLogoService {
    private final HttpServletRequest request;
    private final StorageService storageService;
    private final FirmManager firmManager;

    public void uploadLogo(MultipartFile file) throws IOException {
        log.info("Uploading logo for recruiter's firm");

        UserDAO userDAO = (UserDAO) request.getAttribute("user");
        if (!userDAO.getUserType().equals(UserTypeEnum.RECRUITER)) {
            throw new UserNotRecruiterException();
        }

        FirmDAO firmDAO = firmManager.findFirmByUser(userDAO)
                .orElseThrow(FirmNotFoundException::new);

        if (firmDAO.getLogo() != null && !file.isEmpty()){
            storageService.deleteFile(firmDAO.getLogo());
        }

        String storageKey = generateStorageKey(firmDAO, file);
        firmDAO.setLogo(storageKey);
        firmManager.saveToDatabase(firmDAO);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(file.getBytes());
        storageService.uploadFile(storageKey, file.getContentType(),  outputStream);

        log.info("Uploaded logo for recruiter's firm");
    }

    private String generateStorageKey(FirmDAO firmDAO, MultipartFile file) {
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        return "%s/logo/%s.%s".formatted(firmDAO.getId(), UUID.randomUUID(), fileExtension);
    }
}
