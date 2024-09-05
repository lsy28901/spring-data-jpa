package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member,Long> {
    //스프링 데이터 JPA

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);
    //스프링 데이터 JPA는 메소드 이름을 분석해서 JPQL을 생성하고 실행

    //스프링 데이터 JPA로 Named 쿼리 사용하기 - 실무에선 네임드쿼리를 사용하는 일은 적다
    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);

    //스프링 데이터 JPA로 Named 쿼리 사용하기 (@Query 생략하고 메소드 이름만으로 호출)
//    List<Member> findByUsername(@Param("username") String username);

    //@Query로 레포지토리 메소드에 쿼리 정의하기
    @Query("select m from Member m where m.username= :username and m.age= :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    //단순히 값 하나 조회
    @Query("select m.username from Member m")
    List<String> findUsernameList();
    //Dto 로 직접 조회
    @Query("select new study.datajpa.repository.MemberDto(m.id, m.username, t.name) " +
            "from Member m join m.team t")
    List<MemberDto> findMemberDto();

    //파라미터 바인딩 - 코드 가독성과 유지보수를 위해 이름 기반 파라미터 바인딩을 사용하자
    @Query("select m from Member m where m.username = :name")
    Member findMembers(@Param("name") String username);

    //컬렉션 파라미터 바인딩 in 절 지원
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    /**
     * 유연한 반환 타입 지원
     *  List<Member> findByUsername(String name); //컬렉션
     * Member findByUsername(String name); //단건
     * Optional<Member> findByUsername(String name); //단건 Optional
     *
     * 컬렉션 조회시
     * 결과가 없으면 ? -> 빈 컬렉션 반환
     * 단건 조회시
     * 결과가 없으면 ? -> null 반환 (원래는 NoResultException 인데 이 예외 무시하고 null 반환) ,
     * 결과가 2건 이상 : NonUniqueResultException 예외 발생
     */


}
