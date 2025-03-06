package ru.ispras.wtprac.dealership.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "client")
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class Client implements IEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "id")
    private Long id;

    @Column(nullable = false, name = "name")
    @NonNull
    private String name;

    @Column(nullable = false, name = "email")
    @NonNull
    private String email;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phone;

    @Column(name = "passport")
    private String passport;

    @Column(name = "driving_license")
    private String drivingLicense;

    @Column(nullable = false, name = "password_hash")
    @NonNull
    private String passwordHash;
}
