package ru.ispras.wtprac.dealership.model;

import lombok.*;
import ru.ispras.wtprac.dealership.utility.JsonConverter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "test_drive_schedule")
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class TestDriveSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id", nullable = false)
    @ToString.Exclude
    private Car car;

    @Enumerated(EnumType.STRING)
    @Column(name = "test_drive_status", nullable = false)
    private TestDriveStatus testDriveStatus = TestDriveStatus.InProcessing;

    @Column(name = "date_time_start", nullable = false)
    private LocalDateTime dateTimeStart;

    @Column(name = "date_time_end", nullable = false)
    private LocalDateTime dateTimeEnd;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manager_id", nullable = false)
    @ToString.Exclude
    private Manager manager;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", nullable = false)
    @ToString.Exclude
    private Client client;

}
