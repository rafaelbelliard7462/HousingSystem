package com.example.individualproject.persistance.entity;


import com.example.individualproject.domain.enums.PropertyType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "preference")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PreferenceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( name = "id")
    private  Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @NotBlank
    @Length(min = 2, max = 50)
    @Column(name = "city")
    private String city;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "property_type")
    private PropertyType propertyType;

    @NotNull
    @Column(name = "price")
    private  double price;

    @NotNull
    @Min(1)
    @Column(name = "room")
    private  int room;
}
