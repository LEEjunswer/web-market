package com.webmarket.mapper;

import com.webmarket.dto.request.member.InsertMemberRequestDTO;
import com.webmarket.dto.request.member.InsertSocialMemberRequestDTO;
import com.webmarket.entitiy.Member;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-01T15:43:12+0900",
    comments = "version: 1.5.3.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.14.2.jar, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class MemberMapperImpl implements MemberMapper {

    @Override
    public Member toEntityFromInsertDTO(InsertMemberRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Member member = new Member();

        member.setEmail( dto.getEmail() );
        member.setNick( dto.getNick() );
        member.setPassword( dto.getPassword() );
        member.setName( dto.getName() );
        member.setBirth( dto.getBirth() );
        member.setPhone( dto.getPhone() );
        member.setRole( dto.getRole() );
        member.setGrade( dto.getGrade() );
        member.setAddress( dto.getAddress() );
        member.setProfile( dto.getProfile() );

        return member;
    }

    @Override
    public Member toEntityFromInsertSocialDTO(InsertSocialMemberRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Member member = new Member();

        member.setEmail( dto.getEmail() );
        member.setNick( dto.getNick() );
        member.setName( dto.getName() );
        member.setBirth( dto.getBirth() );
        member.setPhone( dto.getPhone() );
        member.setRole( dto.getRole() );
        member.setGrade( dto.getGrade() );
        member.setAddress( dto.getAddress() );
        member.setProfile( dto.getProfile() );

        return member;
    }
}
