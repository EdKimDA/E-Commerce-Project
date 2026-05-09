package com.example.kimeduardfinalproject.dto.responses;

import com.example.kimeduardfinalproject.enums.KimEduardRole;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KimEduardLoginResponseDTO {

    private String token;
    private String username;
    private KimEduardRole role;
}