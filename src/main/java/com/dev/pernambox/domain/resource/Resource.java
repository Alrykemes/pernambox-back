package com.dev.pernambox.domain.resource;

import com.dev.pernambox.domain.resource.dtos.ResourceRequestDto;
import com.dev.pernambox.domain.resource.enums.CategoriesResources;
import com.dev.pernambox.domain.resource.enums.StatusType;
import com.dev.pernambox.domain.unit.Unit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Entity(name = "Resource")
@Table(name = "resource", schema = "public")
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, unique = true)
    private UUID id;

    @Column(name = "description", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "status_type", nullable = false)
    private StatusType status;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", columnDefinition = "categories_resources", nullable = false)
    private CategoriesResources category;

    @JoinColumn(name = "unit_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Unit unit_id;

    @Column(name = "qrcode", nullable = false)
    private String qrcode;

    public Resource(ResourceRequestDto dto) {
        this.description = dto.description();
        this.status = dto.status();
        this.quantity = dto.quantity();
        this.category = dto.category();
        this.unit_id = dto.unit_id();
        this.qrcode = dto.qrcode();
    }

    public Resource() {}
}
