package com.project.bookapp.dto.order;

import com.project.bookapp.model.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateOrderStatusRequestDto {
    @NotNull
    private Status status;
}
