package com.example.datajpastudy.repository;

import com.example.datajpastudy.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
//@Rollback(value = false) 실무에서는 데이터가 쌓이면 안되기에 test시에는 rollback 하면 안됨
class MemberJPARepositoryTest {

    @Autowired MemberJPARepository memberJPARepository;

    @Test
    public void testMember(){
        Member member = new Member("memberA");
        Member savedMember = memberJPARepository.save(member);

        Member findMember = memberJPARepository.find(savedMember.getId());

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member); //이게 findmember==member와 같음. 원래는 다르지만 jpa는 영속성 컨텍스트에서 가져오기에 같음
    }

    @Test
    public void basicCRUD(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberJPARepository.save(member1);
        memberJPARepository.save(member2);

        Member findMember1 = memberJPARepository.findById(member1.getId()).get();
        Member findMember2 = memberJPARepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        List<Member> all = memberJPARepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        long count = memberJPARepository.count();
        assertThat(count).isEqualTo(2);

        memberJPARepository.delete(member1);
        memberJPARepository.delete(member2);

        long deletedCount = memberJPARepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThen(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberJPARepository.save(m1);
        memberJPARepository.save(m2);

        List<Member> result = memberJPARepository.findByUserNameAndAgeGreaterThen("AAA", 15);
        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void paging(){
        memberJPARepository.save(new Member("member1", 10));
        memberJPARepository.save(new Member("member2", 10));
        memberJPARepository.save(new Member("member3", 10));
        memberJPARepository.save(new Member("member4", 10));
        memberJPARepository.save(new Member("member5", 10));

        int age = 10;
        int offset = 0;
        int limit = 3;

        List<Member> members = memberJPARepository.findByPage(age, offset, limit);
        long totalCount = memberJPARepository.totalCount(age);

        assertThat(members.size()).isEqualTo(3);
        assertThat(totalCount).isEqualTo(5);
    }

}