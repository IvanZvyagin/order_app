package org.example.order_app.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.example.order_app.dto.error.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;
/**
 * Глобальный обработчик исключений REST API.
 *
 * <p>Перехватывает исключения, возникающие в контроллерах,
 * и возвращает унифицированный JSON-ответ в формате {@link org.example.order_app.dto.error.ApiError}.
 *
 * <p>Обрабатывает:
 * <ul>
 *     <li>{@link MethodArgumentNotValidException} — ошибки валидации входных данных</li>
 *     <li>{@link UserIsAlreadyTakenException} — конфликт при регистрации пользователя</li>
 *     <li>{@link EntityNotFoundException} — отсутствие сущности в базе данных</li>
 *     <li>{@link AccessDeniedException} — ошибка доступа</li>
 *     <li>{@link Exception} — любые необработанные ошибки</li>
 * </ul>
 *
 * <p>Все ответы содержат:
 * <ul>
 *     <li>timestamp — время ошибки</li>
 *     <li>status — HTTP статус</li>
 *     <li>error — описание статуса</li>
 *     <li>message — сообщение ошибки</li>
 *     <li>path — путь запроса</li>
 * </ul>
 *
 * @see org.example.order_app.dto.error.ApiError
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обрабатывает ошибки валидации DTO (аннотации {@code @Valid}).
     *
     * <p>Формирует список нарушений по каждому полю запроса.
     *
     * @param ex      исключение валидации
     * @param request HTTP-запрос
     * @return ResponseEntity с телом {@link ApiError} и статусом 400
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex,
                                                     HttpServletRequest request) {

        List<ApiError.FieldViolation> violations = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> ApiError.FieldViolation.builder()
                        .field(fe.getField())
                        .message(fe.getDefaultMessage())
                        .build())
                .toList();

        ApiError body = ApiError.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("Request validation failed")
                .path(request.getRequestURI())
                .violations(violations)
                .build();

        return ResponseEntity.badRequest().body(body);
    }
    /**
     * Обрабатывает ситуацию, когда имя пользователя уже занято.
     *
     * @param ex      исключение {@link UserIsAlreadyTakenException}
     * @param request HTTP-запрос
     * @return ResponseEntity со статусом 409 (CONFLICT)
     */
    @ExceptionHandler(UserIsAlreadyTakenException.class)
    public ResponseEntity<ApiError> handleUserTaken(UserIsAlreadyTakenException ex,
                                                    HttpServletRequest request) {
        return build(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    /**
     * Обрабатывает ошибки отсутствия сущности в базе данных.
     *
     * @param ex      исключение {@link EntityNotFoundException}
     * @param request HTTP-запрос
     * @return ResponseEntity со статусом 404 (NOT FOUND)
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(EntityNotFoundException ex,
                                                   HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    /**
     * Обрабатывает ошибки доступа к ресурсам.
     *
     * @param ex      исключение {@link AccessDeniedException}
     * @param request HTTP-запрос
     * @return ResponseEntity со статусом 403 (FORBIDDEN)
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException ex,
                                                       HttpServletRequest request) {
        return build(HttpStatus.FORBIDDEN, ex.getMessage(), request);
    }
    /**
     * Обрабатывает все необработанные исключения.
     *
     * <p>Используется как fallback-обработчик.
     *
     * @param ex      любое исключение
     * @param request HTTP-запрос
     * @return ResponseEntity со статусом 500 (INTERNAL SERVER ERROR)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAny(Exception ex, HttpServletRequest request) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error", request);
    }

    /**
     * Универсальный метод построения объекта {@link ApiError}.
     *
     * @param status  HTTP статус
     * @param message сообщение ошибки
     * @param request HTTP-запрос
     * @return ResponseEntity с заполненным ApiError
     */
    private ResponseEntity<ApiError> build(HttpStatus status, String message, HttpServletRequest request) {
        ApiError body = ApiError.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(status).body(body);
    }
}
