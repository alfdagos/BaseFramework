package it.alf.sample.dto;

import it.alf.baseframework.dto.BaseDto;

/**
 * Data Transfer Object (DTO) used by the sample service/controller.
 *
 * Why DTOs?
 * - DTOs decouple the external API model from the internal persistence
 *   model (the JPA entity). The `baseframework` encourages using DTOs
 *   and provides a `BaseDto` to hold common DTO attributes if needed.
 * - Mapping between entity and DTO is done by MapStruct mapper that
 *   extends `GenericMapper` (see `MyMapper`).
 */
public class MyDto extends BaseDto {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
