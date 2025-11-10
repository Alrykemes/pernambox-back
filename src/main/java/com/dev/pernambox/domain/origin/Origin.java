package com.dev.pernambox.domain.origin;

import com.dev.pernambox.domain.origin.dtos.OriginRequestDto;
import com.dev.pernambox.domain.origin.dtos.OriginUpdateDto;
import com.dev.pernambox.domain.origin.enums.DocumentType;
import com.dev.pernambox.domain.origin.enums.OriginType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Entity(name = "Origin")
@Table(name = "origin", schema = "public")
public class Origin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer id;

    @Column(name = "cpf_cnpj_origin", nullable = false, unique = true)
    private String cpf_cnpj_origin;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", columnDefinition = "document_type", nullable = false)
    private DocumentType document;

    @Column(name = "date", nullable = false, updatable = false)
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    @Column(name = "origin_type", columnDefinition = "origin_type", nullable = false)
    private OriginType origin;

    @Column(name = "SEI_process")
    private Integer SEI_process;

    public Origin (OriginRequestDto dto) {
        this.cpf_cnpj_origin = dto.cpf_cnpj_origin();
        this.document = dto.document();
        this.date = dto.date();
        this.origin = dto.origin();
        this.SEI_process = dto.SEI_process();
    }

    public Origin (OriginUpdateDto dto) {
        this.id = dto.id();
        this.cpf_cnpj_origin = dto.cpf_cnpj_origin();
        this.document = dto.document();
        this.date = dto.date();
        this.origin = dto.origin();
        this.SEI_process = dto.SEI_process();
    }

    public Origin (){}
}
