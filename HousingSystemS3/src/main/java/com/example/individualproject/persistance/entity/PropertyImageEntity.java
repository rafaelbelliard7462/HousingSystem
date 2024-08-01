package com.example.individualproject.persistance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "property_image")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropertyImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "property_id", nullable = false)
    private PropertyEntity property;
    @NotBlank
    @Length(min = 2, max = 255)
    @Column(name = "image_url")
    private String imageUrl;

    @NotBlank
    @Length(min = 2, max = 255)
    @Column(name = "image_name")
    private String imageName;

    @NotBlank
    @Length(min = 2, max = 255)
    @Column(name = "file_name")
    private String fileName;
}
