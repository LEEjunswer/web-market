package com.webmarket.mapper;

import com.webmarket.dto.request.member.InsertMemberRequestDTO;
import com.webmarket.dto.request.member.InsertSocialMemberRequestDTO;
import com.webmarket.entitiy.Member;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface MemberMapper {

    Member toEntityFromInsertDTO(InsertMemberRequestDTO dto);

    Member toEntityFromInsertSocialDTO(InsertSocialMemberRequestDTO dto);

}
