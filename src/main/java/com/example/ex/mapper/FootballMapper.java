package com.example.ex.mapper;

import com.example.ex.dto.FootBallDto;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

//@Configuration
@Mapper
@MapperScan("com.example.ex.mapper") // 꼭 필요한가?
public interface FootballMapper {
    //List<FootBallDto> selectAll();
    List<FootBallDto> selectAll(Map<String,Object> params);
    void insertFootball(FootBallDto fdto);
    FootBallDto findByNum(int num);
    void updateFootball(FootBallDto fdto);
    void deleteFootball(int numm);
    int getCount(Map<String, Object> params);
}

/*
MyBatis + Spring Boot 환경에서는 PersonMapper 같은 Mapper 인터페이스는 꼭 필요합니다.
→ 컨트롤러와 XML만으로는 작동하지 않습니다.
XML 매퍼만으로는 Spring Boot에서 @Autowired로 주입받는 방식이 불가능
PersonMapper 인터페이스는 반드시 있어야 함
@Mapper 또는 @MapperScan으로 등록해야 Spring이 매퍼 구현체를 자동 생성해 줌
Spring이 자동으로 인터페이스 구현체를 생성해 DI 해주는데 그 뜻은..
우리가 interface만 만들어 놓으면,
Spring + MyBatis가 그 인터페이스에 맞는 클래스를 자동으로 생성하고,
그걸 우리가 쓸 수 있게 @Autowired로 **넣어준다(DI)**는 뜻입니다.

Spring + MyBatis가 자동으로 해주는 일
👉 @Mapper 를 보면 Spring이:
인터페이스 이름과 메서드 이름을 보고
PersonMapper.xml 안에 있는 SQL 구문들을 찾아서
자동으로 해당 인터페이스에 맞는 프록시 객체(가짜 클래스) 를 만들어냅니다.
PersonMapper personMapper = new ProxyClass(); // 자동 생성된 클래스
이 ProxyClass는 우리가 findByNum(1) 이런 메서드를 부르면,
자동으로 PersonMapper.xml 안에 있는 findByNum SQL을 실행해주는 역할을 합니다.
DI (Dependency Injection) 는 말 그대로 “의존성 주입”입니다.
즉, 우리가 컨트롤러에서 이런 식으로
@Autowired
private PersonMapper personMapper;
이라고 하면, Spring이 위에서 만든 프록시 객체를 자동으로 넣어주는 것을 말해요.

[한 줄 요약]
Spring + MyBatis는 @Mapper가 붙은 인터페이스를 보면,
그에 맞는 SQL 실행 코드가 들어있는 구현체(프록시 클래스) 를 자동으로 만들어서,
우리가 직접 만들지 않아도 @Autowired로 주입해 사용할 수 있게 해줍니다.

[실행원리를 보면]
내가 findByNum()를 호출하면
PersonMapper의 가짜 구현체인 MapperProxy객체가 생성되고
MapperProxy객체가 findByNum() 메서드 호출을 가로채고
그 다음 MapperProxy의 invoke()가 호출되면서 (invoke뜻:호출하다,부르다)ㄴ
<select id="findByNum">를 찾아 SQL을 가져와서 실행한다.
*/
