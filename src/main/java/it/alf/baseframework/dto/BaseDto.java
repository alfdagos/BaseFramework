package it.alf.baseframework.dto;

/**
 * Base DTO used as a simple contract for transfer objects.
 * Application DTOs should extend this class.
 */
public class BaseDto {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
