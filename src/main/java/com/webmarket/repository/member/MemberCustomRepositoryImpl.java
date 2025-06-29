package com.webmarket.repository.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.webmarket.dto.request.member.UpdateMemberRequestDTO;
import com.webmarket.entitiy.QMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.webmarket.entitiy.QMember.member;

@Repository
@RequiredArgsConstructor
public class MemberCustomRepositoryImpl implements MemberCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public int updateMember(UpdateMemberRequestDTO updateMemberRequestDTO) {
        QMember qMember = member;
        JPAUpdateClause update = jpaQueryFactory.update(member);
        if(updateMemberRequestDTO.getPhone() != null){
            update.set(member.phone,updateMemberRequestDTO.getPhone());
        }
        if(updateMemberRequestDTO.getProfile() != null){
            update.set(member.profile,updateMemberRequestDTO.getProfile());
        }
        if(updateMemberRequestDTO.getAddress() != null){
            update.set(member.address,updateMemberRequestDTO.getAddress());
        }
        return (int) update.where(member.email.eq(updateMemberRequestDTO.getEmail())).execute();
    }
}
