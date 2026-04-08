package com.example.ex.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
//import org.hibernate.validator.constraints.NotEmpty; // 이것은 안되나? 사용하려고 보면 밑줄이 보이는데..

import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FootBallDto {
    private int num;

    @NotEmpty(message = "id 입력 누락")
    private String id ;

    @NotEmpty(message = "pw 입력 누락")
    private String pw ;

    @NotNull(message = "우승예상국가 입력 누락")
    private String win ;

    //@NotNull(message = "16강예상국가 입력 누락")
    @NotEmpty(message = "16강예상국가 입력 누락")
    //private String round16 ;
    private List<String> round16; // 반드시 List<String>로 선언되어 있어야 Thymeleaf에서 th:field="*{round16}"가 체크박스들을 제대로 처리합니다.
    /*String[] round16으로 해도 어차피 DB에 배열을 DB에 넣을수 없어서
    아래와 같이 하나의 문자열로 만드는 작업을 해야한다.*/

    private String round16AsString; // DB에서 받아온 문자열 ("한국,미국" 등)
    /*JSP에서 체크박스는 List<String> round16과 연결되어 있음
    하지만 MyBatis가 직접 round16 필드에 값을 넣지 않고, round16AsString만 세팅하면
    → round16은 null인 상태임
    → 따라서 fDto.round16.contains("독일") 같은 조건이 NullPointerException 또는 false 발생
    DTO에 round16AsString의 값을 round16으로 변환하는 로직이 제대로 작동하고 있는지 확인


    폼에서 체크박스 등으로 받은 List<String> round16은 getRound16()이 호출되어 사용됩니다.
    round16AsString은 DB와의 데이터 교환용으로 변환해 주는 역할이에요.
     */
    public FootBallDto() {
        super();
        System.out.println("FootBallDto()");
    }
    public FootBallDto(int num, String id, String pw, String win, List<String> round16) {
        super();
        this.num = num;
        this.id = id;
        this.pw = pw;
        this.win = win;
        this.round16 = round16;
    }
    public int getNum() {
        return num;
    }
    public void setNum(int num) {
        this.num = num;
    }
    public String getId() {
        System.out.println("getId()");
        return id;
    }
    public void setId(String id) {
        System.out.println("setId() " + id);
        this.id = id;
    }
//    아래처럼 setter에 @NotBlank붙이면
//    컨트롤러에서 command 객체 생성시 아래와 같이 유효성 검사 수행을 못한다.
//    PostDto 객체를 생성
//    필드에 값을 바인딩 (setXxx 호출)
//    DTO 전체에 대해 Validator를 통해 유효성 검사 수행

//    public void setTitle(@NotBlank(message = "제목 누락@NotBlank") String title) {
//        this.title = title;
//    }

    public String getPw() {
        return pw;
    }
    public void setPw(String pw) {
        this.pw = pw;
    }
    public String getWin() {
        return win;
    }
    public void setWin(String win) {
        this.win = win;
    }

    public List<String> getRound16() {
        System.out.println("getRound16()"); // write_form을 요청했는데 이 메서드가 15번 호출되는 이유는?
       /* => <input type="checkbox" th:field="*{round16}" value="한국" />한국에서
        th:field="*{round16}"를 처리할 때
        1.모델 객체(fDto)의 getRound16() 호출
        2.List<String> round16이 null이 아닌지 확인
        3.round16.contains("한국")인지 확인 (체크 여부 결정)
        위 과정을 모든 체크박스에 대해 1개씩 반복
        → 이때마다 getRound16()이 호출됨
        그러니까..
        각 <input> 렌더링 시 getRound16()을 2~3번씩 호출할 수 있습니다:
        값 가져오기
        체크 여부 확인 (contains)
        에러 메시지 렌더링 여부 확인*/
        return round16;
    }

    public void setRound16(List<String> round16) {
        System.out.println("setRound16()");
        this.round16 = round16;
    }

    // DB에 저장하기 위해 리스트를 문자열로 변환
    // 매퍼.xml의 insert id="insertFootball"의 #{round16AsString}에서 호출됨
    // 매퍼.xml의 insert id="updateFootball"의 #{round16AsString}에서 호출됨

    public String getRound16AsString() { // insert할 때 호출됨
        System.out.println("getRound16AsString()");
        return (round16 != null) ? String.join(",", round16) : null;
        // round16 List를 콤마(,)로 연결한 문자열을 리턴한다.
        // 콤마로 연결한 문자열로 DB table에 insert된다.
    }


    // 매퍼.xml의 resultMap부분 <result property="round16AsString" column="round16"/>에서
    //    setRound16AsString()가 호출된다.
    public void setRound16AsString(String value) {
        System.out.println("setRound16AsString()");
        System.out.println("value:"+value); // DB에 들어있는 round16칼럼값 => value:한국,멕시코,잉글랜드
        this.round16 = Arrays.asList(value.split(",")); // 수정폼하기전까지는 이코드를 사용했는데 잘됐음
        // 수정폼만들 때 DB에서 데이터(한국,독일,브라질)를 읽어와서 round16에 넣어준다.그러면 뷰에서 checkbox 자동 체크됨
       /* 1.value.split(","):
        문자열 value를 쉼표(,) 기준으로 나눈 배열을 만듭니다.
        예:
        "한국,독일,브라질" → ["한국", "독일", "브라질"]

        2.Arrays.asList(...)
        위에서 나온 배열을 List<String>으로 변환합니다.
            결과: ["한국", "독일", "브라질"] → List<String> 형태로 저장

        3. this.round16 = ...
        변환된 List를 DTO의 round16 필드에 저장합니다.
        */
        /*
        DB에서 select로 가져온 문자열을 resultMap에서  result property="round16AsString"을 사용하면서 setRound16AsString()를 통해 자동으로 List로 바꿔준다.
        */

      /*  this.round16AsString = round16AsString;

        // 이 부분이 핵심!!
        if (round16AsString != null && !round16AsString.isEmpty()) {
            this.round16 = Arrays.asList(round16AsString.split(","));
        } else {
            this.round16 = new ArrayList<>();
        }*/
    }

    // 저장 시에는 round16 → round16AsString으로 가는 메서드 필요
    public void updateRound16AsStringFromList() {
        /*System.out.println("updateRound16AsStringFromList()");
        if (round16 != null && !round16.isEmpty()) {
            this.round16AsString = String.join(",", round16);
        } else {
            this.round16AsString = "";
        }*/
        // 윗줄을 아랫줄처럼 간단하게 써도 된다.
        this.round16AsString = String.join(",", round16);
    }
}

/*
write_form 요청시 getRound16()호출
입력폼이 나왔을 때 입력 누락후 submit 클릭하면 getRound16()setRound16() 호출
=>BindingResult에 검증 오류를 기록하고 폼에 다시 바인딩하기 위해
setRound16()이 호출되어 값이 주입되고,
getRound16()이 호출되어 렌더링됩니다.
즉, @ModelAttribute + @Valid에 의해
Spring은 폼 데이터를 FootBallDto에 바인딩 (setXXX 호출)
즉, "폼 필드가 없을 경우에도 setter는 호출됨"
round16이 누락되는 안되든
<input type="checkbox" th:field="*{round16}" ...> 가 렌더링될 때,
그 값이 선택되어야 하는지 판단하기 위해 getRound16() 값을 확인하기 때문에 getRound16()을 호출한다.

getRound16() 호출하는 경우
1. 사용자가 /write_form 처음 요청
컨트롤러에서 new FootBallDto() 생성
round16은 null → getRound16() 호출됨
write_form.html 렌더링: 체크박스 전부 미선택 상태
write_form.html으로 갈때 체크박스 전부 미선택 상태이면 getRound16()이 호출된다.
Thymeleaf 입장에서는 반드시 getRound16()을 호출해야 체크 여부를 결정할 수 있기 때문에
값이 비어 있더라도 호출됩니다.체크박스 갯수만큼 호출됩니다.
getRound16()은 체크박스가 렌더링될 때 값 유무와 관계없이 무조건 호출됩니다.
즉, round16이 비었어도, 미선택이어도, 상관없이 호출됨

2. 사용자가 일부 항목 입력하고 submit (/write2)
round16 값을 빼먹었음
@Valid 검증 실패 → 다시 "write_form"으로 리턴
이전에 입력했던 id, pw 등은 BindingResult와 함께 모델에 유지됨
→ 이때 Thymeleaf가 다시 getRound16() 호출해서 checkbox 상태 유지

3. 사용자가 모든 항목 입력하고 submit
@Valid 성공 → redirect:/list 로 이동
write_form.html로 다시 가지 않음
→ 따라서 이 경우엔 getRound16()이 호출되지 않음
 */

