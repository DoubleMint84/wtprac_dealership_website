package ru.ispras.wtprac.dealership.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "order")
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "order_datetime", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime orderDatetime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", nullable = false)
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
    private OrderStatus orderStatus = OrderStatus.InProcessing;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id", nullable = false)
    @ToString.Exclude
    private Car car;

}
