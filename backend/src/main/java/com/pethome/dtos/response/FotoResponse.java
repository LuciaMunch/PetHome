package com.pethome.dtos.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FotoResponse {

    private Long id;
    private String url;
    private Long animalId;
}
