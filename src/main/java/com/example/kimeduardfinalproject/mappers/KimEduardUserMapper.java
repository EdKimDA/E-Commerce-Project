package com.example.kimeduardfinalproject.mappers;

import com.example.kimeduardfinalproject.dto.responses.KimEduardUserResponseDTO;
import com.example.kimeduardfinalproject.entities.KimEduardUser;
import org.springframework.stereotype.Component;

@Component
public class KimEduardUserMapper {
    public KimEduardUserResponseDTO toResponse(KimEduardUser user) {
        return new KimEduardUserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getActive(),
                user.getDeleted(),
                user.getCreatedAt()
        );
    }
}