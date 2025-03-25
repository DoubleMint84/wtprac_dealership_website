package ru.ispras.wtprac.dealership.model;


import lombok.*;
import org.hibernate.annotations.Type;
import ru.ispras.wtprac.dealership.utility.JsonConverter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Entity
@Getter
@Setter
@Table(name = "car")
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@NoArgsConstructor
public class Car implements IEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 255)
    @NonNull
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "config_id")
    @ToString.Exclude
    private VehicleConfiguration vehicleConfiguration;

    @Column(name = "date_of_creation", nullable = false)
    @NonNull
    private LocalDate dateOfCreation;

    @Column(name = "VIN", nullable = false, unique = true, length = 50)
    @NonNull
    private String vin;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    @NonNull
    private BigDecimal price;

    @Column(name = "mileage")
    private Integer mileage = 0;

    @Column(name = "date_last_TO")
    private LocalDate dateLastTO;

    @Column(name = "color", length = 50)
    private String color;

    @Column(name = "seat", length = 50)
    private String seat;

    @Column(name = "additional_client_characteristics")
    @Convert(converter = JsonConverter.class)
    private Map<String, String> additionalClientCharacteristics;

    @Column(name = "additional_car_characteristics")
    @Convert(converter = JsonConverter.class)
    private Map<String, String> additionalCarCharacteristics;

    @Enumerated(EnumType.STRING)
    @Type(type = "ru.ispras.wtprac.dealership.utility.EnumTypePostgreSQL")
    @Column(name = "car_status", nullable = false)
    @NonNull
    private CarStatus carStatus = CarStatus.InProcessing;

}
