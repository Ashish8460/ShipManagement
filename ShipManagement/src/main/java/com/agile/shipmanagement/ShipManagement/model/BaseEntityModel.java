package com.agile.shipmanagement.ShipManagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class) // 🔸 Important for auditing
public class BaseEntityModel {
    @CreatedDate// 🔹 Auto-set when the document is first created
    @Column(updatable = false)
    private Date createdAt;

    @Column(updatable = true)
    @LastModifiedDate  // 🔹 Auto-updated whenever the document is modified
    private Date updatedAt;

}
