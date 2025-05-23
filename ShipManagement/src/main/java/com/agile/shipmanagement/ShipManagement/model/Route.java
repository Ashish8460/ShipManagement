package com.agile.shipmanagement.ShipManagement.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int routeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ship_id")
    @JsonBackReference // ✅ This side is skipped during serialization
    private Ship ship;

    @ElementCollection
    @NotNull(message = "Ports list must not be null.")
    @Size(min = 1, message = "Ports list must contain at least one port.")
    private List<@NotBlank(message = "Port name must not be blank.") String> ports;

    @NotNull(message = "Distance is required.",groups = {PositionUpdateGroup.class})
    private Double distance;

    @NotNull(message = "Estimated Time must not be blank.",groups = {PositionUpdateGroup.class})
    private Double estimatedTime;

    @NotNull(message = "Departure Time must not be blank.",groups = {PositionUpdateGroup.class})
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime departureTime;

    @NotNull(message = "Arrival Time must not be blank.",groups = {PositionUpdateGroup.class})
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime arrivalTime;

}
