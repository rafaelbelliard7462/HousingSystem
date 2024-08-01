package com.example.individualproject.persistance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chat")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( name = "id")
    private  Long id;

    @ManyToOne
    @JoinColumn(name = "home_seeker_id")
    private UserEntity homeSeeker;

    @ManyToOne
    @JoinColumn(name = "homeowner_id")
    private UserEntity homeowner;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(fetch = FetchType.EAGER,mappedBy = "chat")
    private List<MessageEntity> messages = new ArrayList<>();
}
