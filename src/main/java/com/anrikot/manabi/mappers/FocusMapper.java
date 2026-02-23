package com.anrikot.manabi.mappers;

import com.anrikot.manabi.domain.Focus;
import com.anrikot.manabi.domain.User;
import com.anrikot.manabi.dto.FocusDTO;

public class FocusMapper {
    public static Focus toEntity(FocusDTO dto, User user, Focus parent) {
        Focus f = new Focus();
        f.setId(dto.id());
        f.setName(dto.name());
        f.setParent(parent);
        f.setUser(user);
        return f;
    }

    public static FocusDTO toDTO(Focus entity) {
        Integer parentId = null;
        if (entity.getParent() != null) parentId = entity.getParent().getId();
        return new FocusDTO(
            entity.getId(),
            entity.getName(),
            parentId
        );
    }
}