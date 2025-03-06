package ru.ispras.wtprac.dealership.model;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;
import ru.ispras.wtprac.dealership.utility.JsonConverter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "model")
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class Model implements IEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "id")
    private Long id;

    @Column(nullable = false, name = "name")
    @NonNull
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "brand_id")
    @ToString.Exclude
    private Brand brand;

    @Column(name = "year")
    private Integer year;

    @Column(columnDefinition = "jsonb", name = "additional_info")
    @Convert(converter = JsonConverter.class)
    private JsonNode additionalInfo;
}
