package ru.ispras.wtprac.dealership.model;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;
import ru.ispras.wtprac.dealership.utility.JsonConverter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "vehicle_configuration")
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class VehicleConfiguration implements IEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "id")
    private Long id;

    @Column(nullable = false, name = "name")
    @NonNull
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "model_id")
    @ToString.Exclude
    private Model model;

    @Column(name = "engine_volume", precision = 4, scale = 2)
    private BigDecimal engineVolume;

    @Column(name = "engine_power")
    private Integer enginePower;

    @Column(name = "fuel_consumption", precision = 4, scale = 2)
    private BigDecimal fuelConsumption;

    @Column(name = "doors_count")
    private Integer doorsCount;

    @Column(name = "seats_count")
    private Integer seatsCount;

    @Column(name = "trunk_capacity", precision = 6, scale = 2)
    private BigDecimal trunkCapacity;

    @Column(name = "transmission", length = 50)
    private String transmission;

    @Column(name = "has_cruise_control")
    private Boolean hasCruiseControl;

    @Column(name = "base_price", precision = 10, scale = 2)
    private BigDecimal basePrice;

    @Column(name = "discount_amount", precision = 10, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "octane_number")
    private Integer octaneNumber;

    @Column(columnDefinition = "jsonb", name = "additional_info")
    @Convert(converter = JsonConverter.class)
    private String additionalInfo;

    @Column(name = "is_on_sale")
    private Boolean isOnSale = true;
}
