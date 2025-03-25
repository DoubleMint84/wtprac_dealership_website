package ru.ispras.wtprac.dealership.model;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "test_drive_schedule")
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@NoArgsConstructor
public class TestDriveSchedule implements IEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id", nullable = false)
    @NonNull
    @ToString.Exclude
    private Car car;

    @Enumerated(EnumType.STRING)
    @Column(name = "test_drive_status", nullable = false)
    @Type(type = "ru.ispras.wtprac.dealership.utility.EnumTypePostgreSQL")
    @NonNull
    private TestDriveStatus testDriveStatus = TestDriveStatus.InProcessing;

    @Column(name = "date_time_start", nullable = false)
    @NonNull
    private LocalDateTime dateTimeStart;

    @Column(name = "date_time_end", nullable = false)
    @NonNull
    private LocalDateTime dateTimeEnd;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manager_id", nullable = false)
    @NonNull
    @ToString.Exclude
    private Manager manager;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", nullable = false)
    @NonNull
    @ToString.Exclude
    private Client client;

}
