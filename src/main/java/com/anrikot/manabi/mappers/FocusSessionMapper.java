package com.anrikot.manabi.mappers;

import com.anrikot.manabi.domain.Focus;
import com.anrikot.manabi.domain.FocusSession;
import com.anrikot.manabi.domain.User;
import com.anrikot.manabi.dto.FocusSessionDTO;

public class FocusSessionMapper {
    public static FocusSession toEntity(FocusSessionDTO dto, User user, Focus focus) {
        FocusSession f = new FocusSession();
        f.setId(dto.id());
        f.setUser(user);
        f.setFocus(focus);
        f.setStart(dto.start());
        f.setEnd(dto.end());

        return f;
    }

    public static FocusSessionDTO toDTO(FocusSession entity) {
        return new FocusSessionDTO(
            entity.getId(),
            entity.getFocus().getId(),
            entity.getStart(),
            entity.getEnd());
    }
}
