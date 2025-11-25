package com.easyjob.easyjobapi.core.firm.models;

import com.easyjob.easyjobapi.core.user.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Firm {
    private FirmId firmId;
    private String name;
    private User user;
    private String description;
    private String location;
    private String logo;

    private Instant createdAt;
    private Instant updatedAt;

    private Boolean isArchived;
    private Instant archivedAt;
}
