package com.hrms.backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Getter @Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable extends AuditableTimestamp  {
    @CreatedBy
    @Column(updatable = false)
    private UUID createdBy;

    @LastModifiedBy
    @Column(insertable = false)
    private UUID updatedBy;
}
