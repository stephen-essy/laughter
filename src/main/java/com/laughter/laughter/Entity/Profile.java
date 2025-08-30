package com.laughter.laughter.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="user_information")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Profile {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @OneToOne()
    @JoinColumn(name="user")
    private User user;
    @Column(name="Gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column(name="Phone_number")
    private String phoneNumber;
    @Column(name="University")
    private String university;
    @Column(name="Corse")
    private String corse;
    public Profile(User user, Gender gender, String phoneNumber, String university, String corse) {
        this.user = user;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.university = university;
        this.corse = corse;
    }
}
