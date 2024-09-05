package study.datajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import study.datajpa.repository.MemberJpaRepository;
import study.datajpa.repository.MemberRepository;

import java.util.Optional;
import java.util.UUID;

@EnableJpaAuditing
@SpringBootApplication
public class DataJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataJpaApplication.class, args);
    }

    @Bean
    public AuditorAware<String> auditorProvider(){
        //실무에서는 세션 정보나, 스프링 시큐리티 로그인 정보에서 ID를 받음
        return () -> Optional.of(UUID.randomUUID().toString());
    }
}
