package com.webmarket.repository.member;

import com.webmarket.dto.request.member.UpdateMemberRequestDTO;

public interface MemberCustomRepository {

    int updateMember(UpdateMemberRequestDTO updateMemberRequestDTO);
}
