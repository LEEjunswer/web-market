package com.webmarket.mapper;

import com.webmarket.dto.request.member.InsertMemberRequestDTO;
import com.webmarket.dto.request.member.InsertSocialMemberRequestDTO;
import com.webmarket.entitiy.Member;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberMapper {

    Member toEntityFromInsertDTO(InsertMemberRequestDTO dto);

    Member toEntityFromInsertSocialDTO(InsertSocialMemberRequestDTO dto);

}
