package com.agile.shipmanagement.ShipManagement.model;

import com.agile.shipmanagement.ShipManagement.enums.ShipStatus;
import com.agile.shipmanagement.ShipManagement.enums.ShipType;
import com.agile.shipmanagement.ShipManagement.utils.ShipStatusConverter;
import com.agile.shipmanagement.ShipManagement.utils.ShipTypeConverter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Ship extends BaseEntityModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int shipId;

    @NotBlank(message = "Name must not be blank.")
    @Size(min = 5,message = "Name must contains at least 5 characters.")
    private String name;

    @Convert(converter = ShipTypeConverter.class) // Save enum as string in DB
    @NotNull(message = "Type must not be blank.")
    private ShipType type;

    @NotNull(message = "Capacity must not be blank.")
    @Min(value = 1,message = "Capacity must be greater than 0.")
    private double capacity;

    @Convert(converter = ShipStatusConverter.class) // Save enum as string in DB
    @NotNull(message = "Status must not be blank.")
    private ShipStatus status;

    @OneToMany(mappedBy = "ship", cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonBackReference ///  Stop Getting recurring values inside Ship crew list
    private List<Crew> crewList = new ArrayList<>();
}
