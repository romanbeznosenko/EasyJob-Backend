package com.easyjob.easyjobapi.core.offer.models;

import com.easyjob.easyjobapi.core.firm.models.FirmDAO;
import com.easyjob.easyjobapi.utils.enums.EmploymentTypeEnum;
import com.easyjob.easyjobapi.utils.enums.ExperienceLevelEnum;
import com.easyjob.easyjobapi.utils.enums.WorkModeEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "offer")
public class OfferDAO {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "responsibilities", columnDefinition = "TEXT")
    private String responsibilities;

    @Column(name = "requirements", columnDefinition = "TEXT")
    private String requirements;

    @JoinColumn(name = "firm_id")
    @ManyToOne
    private FirmDAO firm;

    @Column(name = "is_salary_disclosed")
    private Boolean isSalaryDisclosed;

    @Column(name = "salary_bottom")
    private Long salaryBottom;

    @Column(name = "salary_top")
    private Long salaryTop;

    @Column(name = "employment_type")
    private EmploymentTypeEnum employmentType;

    @Column(name = "experience_level")
    private ExperienceLevelEnum experienceLevel;

    @Column(name = "work_mode")
    private WorkModeEnum workMode;

    @ElementCollection
    @CollectionTable(
            name = "offer_skills",
            joinColumns = @JoinColumn(name = "offer_id")
    )
    @Column(name = "skill")
    private List<String> skills;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "archived_at")
    private Instant archivedAt;

    @Column(name = "is_archived")
    private Boolean isArchived = false;

    @Override
    public int hashCode() {
        return id != null ? Objects.hash(id) : getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        OfferDAO other = (OfferDAO) obj;

        if (id != null && other.id != null) {
            return Objects.equals(id, other.id);
        }

        return this == other;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [id=" + (id != null ? id : "null") + "]";
    }
}