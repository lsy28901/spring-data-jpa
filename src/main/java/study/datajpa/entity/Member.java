package study.datajpa.entity;

import jakarta.persistence.*;

import lombok.*;

@NamedEntityGraph(name = "Member.all",attributeNodes = @NamedAttributeNode("team"))
@Entity
@NamedQuery( //NamedQuery 정의하기
        name = "Member.findByUsername",
        query = "select m from Member m where m.username = :username")
@Getter @Setter //실무에선 가급적 Setter 피하기
@NoArgsConstructor(access = AccessLevel.PROTECTED)//기본 생성자 막고싶지만 JPA 스펙상 PROTECTED로 열어둠
@ToString(of = {"id","username","age"}) //가급적 내부 필드만 (연관관계 없는 필드)
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    //연관관계의 주인 (다대일)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public Member(String username) {
        this(username,0);
    }
    public Member(String username, int age){
        this(username, age, null);
    }

    public Member(String username, int age, Team team){
        this.username = username;
        this.age = age;
        if (team != null){
            changeTeam(team);
        }
    }
    //양방향 연관관계를 한번에 처리해주는 연관관계 편의 메소드
    public void changeTeam(Team team){
        this.team = team;
        team.getMembers().add(this);
    }
}
