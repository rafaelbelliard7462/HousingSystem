package com.example.individualproject.persistance.entity;


import com.example.individualproject.domain.enums.PropertyType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "property")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropertyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private AddressEntity address;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "property_type")
    private PropertyType propertyType;

    @NotNull
    @Column(name = "price")
    private double price;
    @NotNull
    @Column(name = "size")
    private int size;

    @NotNull
    @Min(1)
    @Column(name = "room")
    private int room;

    @NotNull
    @Column(name = "available")
    private Date available;

    @NotBlank
    @Length(min = 2, max = 200)
    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "rented")
    private boolean rented;


    @Column(name = "rented_date")
    private Date rentedDate;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "property")
    private List<ApplicationEntity> applications = new ArrayList<>();

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "property")
    private List<PropertyImageEntity> propertyImages = new ArrayList<>();
}
