package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    /**
     * 스프링 데이터 JPA 페이징과 정렬
     * Pageable ( 내부에 Sort 포함)
     * Page : 추가 count 쿼리 결과를 포함하는 페이징
     * Slice : 추가 count 쿼리 없이 다음 페이지만 확인 가능 (내부적으로 limit+1 조회)
     * List 자바 컬렉션 : 추가 count 쿼리 없이 결과만 반환함
     * Page<Member> findByUsername(String name, Pageable pageable);  //count 쿼리 사용
     * Slice<Member> findByUsername(String name, Pageable pageable); //count 쿼리 사용 안함
     * List<Member> findByUsername(String name, Pageable pageable);  //count 쿼리 사용 안함
     * List<Member> findByUsername(String name, Sort sort);
     */
    Page<Member> findByAge(int age, Pageable pageable);

    //count 쿼리를 분리할 수 있다 -> 복잡한 sql에서 사용, 데이터는 left join, 카운트는 left join 안해도 된다.
    @Query(value = "select m from Member m",
            countQuery = "select count(m.username) from Member m")
    Page<Member> findMemberAllCountBy(Pageable pageable);

    //스프링 데이터 JPA 로 벌크성 수정 쿼리
    //벌크성 수정,삭제 쿼리는 @Modifying 사용함
    //사용하지 않으면 QueryExecutionRequestException 발생
    //벌크성 쿼리 실행하고 영속성 컨텍스트 초기화 방법 @Modifying(clearAutomatically = ture) 디폴트는 false임
    //clearAutomatically = true 옵션 없이 회원을 findById로 다시 조회하면 영속성 컨텍스트에 과거 값이 남아 문제가 될수 있다.
    //다시 조회 해야하면 영속성 컨텍스트를 초기화 하도록 하자.
    //참고로 벌크 연산은 영속성 컨텍스트를 무시하고 실행하기 때문에, 영속성 컨텍스트에 있는 엔티티 상태와 DB의 엔티티 상태가 다를 수 있음.
    //해결 방법
    //영속성 컨텍스트에 엔티티가 없는 상태에서 벌크 연산을 먼저 실행
    //부득이하게 영속성 컨텍스트에 엔티티가 있으면 벌크 연산 직후 영속성 컨텍스트를 초기화
    @Modifying
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);



}
