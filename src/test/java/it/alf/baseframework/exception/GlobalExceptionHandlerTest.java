package it.alf.baseframework.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleCustomException_shouldReturnBadRequestProblemDetail() {
        ProblemDetail problem = handler.handleCustomException(new CustomException("Test exception"));

        assertThat(problem.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(problem.getDetail()).isEqualTo("Test exception");
        assertThat(problem.getTitle()).isEqualTo("Bad Request");
        assertThat(problem.getProperties()).containsKey("timestamp");
    }

    @Test
    void handleResourceNotFound_shouldReturnNotFoundProblemDetail() {
        ProblemDetail problem = handler.handleResourceNotFound(
                ResourceNotFoundException.of("MyEntity", 7L));

        assertThat(problem.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(problem.getDetail()).contains("MyEntity").contains("7");
        assertThat(problem.getTitle()).isEqualTo("Resource Not Found");
    }

    @Test
    void handleUnexpected_shouldReturnInternalServerError() {
        ProblemDetail problem = handler.handleUnexpected(new RuntimeException("boom"));

        assertThat(problem.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(problem.getTitle()).isEqualTo("Internal Server Error");
    }
}
