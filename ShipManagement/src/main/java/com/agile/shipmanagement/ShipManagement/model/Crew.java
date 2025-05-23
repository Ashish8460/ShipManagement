package com.agile.shipmanagement.ShipManagement.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Crew extends  BaseEntityModel{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int crewId;

    @NotBlank(message = "Name must not be blank.")
    @Size(min = 3,message = "Name must contains at least 3 characters.")
    private String name;

    @NotBlank(message = "Position must not be blank.", groups = PositionUpdateGroup.class)
    @Size(min = 3,message = "Position must contains at least 3 characters.")
    private String position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ship_id") // foreign key column in Crew table
    @JsonBackReference // ✅ This side is skipped during serialization
    private Ship ship;
}


