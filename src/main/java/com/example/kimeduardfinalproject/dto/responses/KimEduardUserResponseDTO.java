package com.example.kimeduardfinalproject.dto.responses;

import com.example.kimeduardfinalproject.enums.KimEduardRole;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KimEduardUserResponseDTO {

    private Long id;
    private String username;
    private String email;
    private KimEduardRole role;
    private Boolean active;
    private Boolean deleted;
    private LocalDateTime createdAt;
}