package study.datajpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseEntity {
    //스프링 데이터 JPA Auditing
    //실무에서는 대부분의 엔티티가 등록시간, 수정 시간이 필요하지만 등록자, 수정자는 없을 수 도있다.
    //따라서 BaseTimeEntity 와 BaseEntity(등록자,수정자/extend BaseTimeEntity) 로 분리해 원하는 타입을 선택해 상속한다.
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
}
