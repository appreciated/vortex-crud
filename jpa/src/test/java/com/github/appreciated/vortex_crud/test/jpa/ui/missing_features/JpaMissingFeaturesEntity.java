package com.github.appreciated.vortex_crud.test.jpa.ui.missing_features;

import com.github.appreciated.vortex_crud.core.file_provider.LocalFileResourceProvider;
import com.github.appreciated.vortex_crud.core.file_provider.LocalPdfResourceProvider;
import com.github.appreciated.vortex_crud.core.file_provider.LocalVideoResourceProvider;
import com.github.appreciated.vortex_crud.jpa.service.annoations.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "missing_features_test")
public class JpaMissingFeaturesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @TextField
    @Column(name = "name")
    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "missing_features_test_tags", joinColumns = @JoinColumn(name = "entity_id"))
    @Column(name = "tag_value")
    @MultiSelectValueField("tags")
    private Set<String> tags;

    @PdfField(LocalPdfResourceProvider.class)
    @Column(name = "pdf_doc")
    private String pdfDoc;

    @Column(name = "notes", columnDefinition = "TEXT")
    @TextAreaField
    private String notes;

    @ManyToOne
    @JoinColumn(name = "referenced_id")
    @ReferenceField(value = "name", fields = {"name"})
    private JpaMissingFeaturesReferencedEntity referencedEntity;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "missing_features_test_relations",
        joinColumns = @JoinColumn(name = "test_id"),
        inverseJoinColumns = @JoinColumn(name = "referenced_id")
    )
    @MultiSelectField(value = "name", fields = {"name"})
    private Set<JpaMissingFeaturesReferencedEntity> multiSelectEntities;

    @Column(name = "markdown_content", columnDefinition = "TEXT")
    @MarkDownField
    private String markdownContent;

    @Column(name = "file_attachment")
    @FileField(LocalFileResourceProvider.class)
    private String fileAttachment;

    @Column(name = "price", precision = 10, scale = 2)
    @BigDecimalNumberField
    private BigDecimal price;

    @Column(name = "video_url")
    @VideoField(LocalVideoResourceProvider.class)
    private String videoUrl;
}
