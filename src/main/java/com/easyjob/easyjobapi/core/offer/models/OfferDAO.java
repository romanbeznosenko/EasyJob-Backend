package com.easyjob.easyjobapi.core.offer.models;

import com.easyjob.easyjobapi.core.firm.models.FirmDAO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
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