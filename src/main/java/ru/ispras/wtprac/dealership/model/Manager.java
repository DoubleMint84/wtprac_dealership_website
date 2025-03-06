package ru.ispras.wtprac.dealership.model;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "manager")
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class Manager implements IEntity<Long> {

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

    @Column(name = "phone_number")
    private String phone;

    @Column(nullable = false, name = "password_hash")
    @NonNull
    private String passwordHash;
}
