package ru.ispras.wtprac.dealership.model;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "order")
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@NoArgsConstructor
public class Order implements IEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "order_datetime", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime orderDatetime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", nullable = false)
    @NonNull
    @ToString.Exclude
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manager_id")
    @ToString.Exclude
    private Manager manager;

    @Column(name = "needs_pre_test_drive", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean needsPreTestDrive = false;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "test_drive_id")
    @ToString.Exclude
    private TestDriveSchedule testDrive;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    @Type(type = "ru.ispras.wtprac.dealership.utility.EnumTypePostgreSQL")
    @NonNull
    private OrderStatus orderStatus = OrderStatus.InProcessing;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id", nullable = false)
    @NonNull
    @ToString.Exclude
    private Car car;

}
