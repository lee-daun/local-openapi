package com.local.openapi.core.entity;


import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "keyword_log")
@Entity
@Getter
@Builder
public class KeywordLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String serviceName;

    @CreationTimestamp
    private LocalDateTime createdAt;

}
