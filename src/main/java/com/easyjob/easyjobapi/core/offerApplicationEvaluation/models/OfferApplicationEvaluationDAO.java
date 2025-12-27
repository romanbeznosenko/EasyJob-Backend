package com.easyjob.easyjobapi.core.offerApplicationEvaluation.models;

import com.easyjob.easyjobapi.core.offer.models.OfferDAO;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfileDAO;
import com.easyjob.easyjobapi.utils.enums.ApplicationStatusEnum;
import com.easyjob.easyjobapi.utils.enums.RecommendationEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.mapstruct.EnumMapping;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "offer_application_evaluation")
public class OfferApplicationEvaluationDAO {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "offer_id")
    private OfferDAO offer;

    @ManyToOne
    @JoinColumn(name = "applier_profile_id")
    private ApplierProfileDAO applierProfile;

    @Column(name = "status")
    private ApplicationStatusEnum status;

    @Column(name = "overall_match_score")
    private Integer overAllMatchScore;

    @Column(name = "skills_score")
    private Integer skillsScore;

    @Column(name = "experience_score")
    private Integer experienceScore;

    @Column(name = "education_score")
    private Integer educationScore;

    @Column(name = "project_score")
    private Integer projectScore;

    @Column(name = "skills_analysis", columnDefinition = "TEXT")
    private String skillsAnalysis;

    @Column(name = "strengths", columnDefinition = "TEXT")
    private String strengths;

    @Column(name = "weaknesses", columnDefinition = "TEXT")
    private String weaknesses;

    @Column(name = "cultural_fit", columnDefinition = "TEXT")
    private String culturalFit;

    @Column(name = "growth_potential", columnDefinition = "TEXT")
    private String growthPotential;

    @Column(name = "interview_focus_area", columnDefinition = "TEXT")
    private String interviewFocusAreas;

    @Column(name = "detailed_summary", columnDefinition = "TEXT")
    private String detailedSummary;

    @Column(name = "recommendation")
    @Enumerated(EnumType.STRING)
    private RecommendationEnum recommendation;

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
        OfferApplicationEvaluationDAO other = (OfferApplicationEvaluationDAO) obj;

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