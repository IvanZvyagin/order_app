package org.example.order_app.dto.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@Schema(description = "Единый формат ошибок API")
public class ApiError {
    @Schema(example = "2026-02-01T14:50:43.520Z")
    private Instant timestamp;

    @Schema(example = "400")
    private int status;

    @Schema(example = "Bad Request")
    private String error;

    @Schema(example = "Request validation failed")
    private String message;

    @Schema(example = "/api/orders")
    private String path;

    private List<FieldViolation> violations;

    @Data
    @Builder
    @Schema(description = "Ошибка валидации поля")
    public static class FieldViolation {
        @Schema(example = "password")
        private String field;

        @Schema(example = "Password must be at least 8 characters")
        private String message;
    }
}
