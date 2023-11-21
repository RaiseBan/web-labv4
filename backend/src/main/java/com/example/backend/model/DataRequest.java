package com.example.backend.model;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataRequest {
    @NotNull(message = "X cannot be null")
    @DecimalMin(value = "-5", message = "X must be greater than or equal to -5")
    @DecimalMax(value = "5", message = "X must be less than or equal to 5")
    private Double x;

    @NotNull(message = "Y cannot be null")
    @DecimalMin(value = "-5", message = "Y must be greater than or equal to -5")
    @DecimalMax(value = "5", message = "Y must be less than or equal to 5")
    private Double y;

    @NotNull(message = "R cannot be null")
    @DecimalMin(value = "0", message = "R must be greater than 0")
    private Double r;
}
