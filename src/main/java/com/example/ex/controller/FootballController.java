package com.example.ex.controller;

import com.example.ex.dto.FootBallDto;
import com.example.ex.mapper.FootballMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class FootballController {
    // branch1추가
    // branch1추가
    // branch1추가

    // master 작성1:43
    // master 작성1:45

    @Autowired
    private FootballMapper footballMapper;

    @RequestMapping("/list")
    public String list(/*HttpServletRequest request,*/ Model model,
                                                       @RequestParam(value="page", defaultValue="1") int page,
                                                       @RequestParam(value="whatColumn", required=false) String whatColumn,
                                                       @RequestParam(value="keyword", required=false) String keyword) {

        int limit = 3;  // 한 페이지당 3개 출력
        int offset = (page - 1) * limit;
        System.out.println("offset:" + offset);

        Map<String,Object> params = new HashMap<>();
        params.put("whatColumn", whatColumn);
        params.put("keyword", keyword);
        params.put("offset", offset);
        params.put("limit", limit);

        //List<FootBallDto> list = footballMapper.selectAll();
        List<FootBallDto> list = footballMapper.selectAll(params);
        int totalCount = footballMapper.getCount(params); // 검색한 전체 레코드 수 구하기

        int totalPage = (int)Math.ceil((double)totalCount / limit);
        if (totalPage == 0) { // 레코드가 없을때 1페이지 옆에 0페이지가 뜨는데 0페이지 없애려면 여기 if문이 필요하다.
            totalPage = 1; // 최소 1페이지는 보장
        }
        model.addAttribute("list", list);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("page", page);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("whatColumn", whatColumn);
        model.addAttribute("keyword", keyword);

        System.out.println("list.get(0).getRound16():"+list.get(0).getRound16());
        System.out.println("list.get(0).getRound16AsString() :"+list.get(0).getRound16AsString());

        model.addAttribute("list", list);
        return "list";
    }

    @RequestMapping("/write_form")
    public String write_view2(Model model, HttpServletRequest request,
                              @ModelAttribute("fDto") @Valid FootBallDto fdto, BindingResult result) {

        System.out.println("write_form()");
        model.addAttribute("fDto", new FootBallDto());  // 처음에도 fDto이름으로 빈 객체라도 넣어줘야 에러 안뜸
    //  fDto는 content_view.html의 th:object="${fDto}"와 같아야 한다.

/*
        System.out.println("write_form result.hasErrors() : " + result.hasErrors());
        if(result.hasErrors()) {
            System.out.println("에러발생");
            return "write_form";
        }*/


        return "write_form";
    }



    @RequestMapping("/writePro")
    public String write2(Model model, HttpServletRequest request,
                         @Valid @ModelAttribute("fDto")  FootBallDto fdto, BindingResult result) {

        System.out.println("@RequestMapping(\"/writePro\")  ");
        System.out.println("fdto.getId():"+fdto.getId());
        System.out.println("fdto.getRound16():"+fdto.getRound16()); // [독일, 브라질]

       // request.setAttribute("fDto", fdto); // 없어도될듯함
        System.out.println(0);
        System.out.println("result.hasErrors() : " + result.hasErrors());
        //Model : 뷰에 전달될 객체 정보를 담고 있는 Model 객체
        //System.out.println("write()");
        //System.out.println("result.hasErrors():" + result.hasErrors());
        //		ModelAndView mav = new ModelAndView();
        if(result.hasErrors()) {
            System.out.println("에러발생");
            return "write_form";
        }

        footballMapper.insertFootball(fdto);
        String page = "result";
        System.out.println(1);
        /*if(result.hasErrors()) {
            page = "write_form";
            return page; // 기존 폼 뷰 이름

        }*/


        // 선택한 서점 출력
//        if (fdto.getProduct() != null) {
//            System.out.println("getProduct 선택: " + String.join(", ", mb.getProduct()));
//        }
        //page = false ? "write_form" : "list";

        System.out.println(2);
        System.out.println("page :" + page);
        System.out.println(3);
        return "redirect:/list";
    }

    @RequestMapping(value="/content_view") // 뒤에 , method=RequestMethod.GET 생략가능
    public String content_view(HttpServletRequest request,
                               @RequestParam("num") int num,
                               @RequestParam("page") int page,
                               @RequestParam(value="whatColumn", required=false) String whatColumn,
                               @RequestParam(value="keyword", required=false) String keyword,
                               Model model) {

        System.out.println("content_view()");
        System.out.println("넘어온 num:"+request.getParameter("num"));


        // num으로 데이터 조회
        FootBallDto content = footballMapper.findByNum(num);

        // 조회한 데이터를 모델에 담아서 Thymeleaf에 전달
        model.addAttribute("fDto", content);
        model.addAttribute("nationList", List.of("한국", "미국", "독일", "스페인"));
        List<String> round16List = List.of("한국", "멕시코", "독일", "브라질", "스위스", "잉글랜드");
        model.addAttribute("round16List", round16List);

        model.addAttribute("page", page);
        model.addAttribute("whatColumn", whatColumn);
        model.addAttribute("keyword", keyword);

        // content_view.html 또는 content_view.jsp (설정에 따라 다름)
        return "content_view";

        // 아래는 왜 안되지?
        //return "redirect:content_view.jsp"; // http://localhost:9090/ex/content_view.jsp
        // //redirect는 클라이언트에게 명령하는 것이다. 클라이언트는 list_page.do로 다시 요청


    }

    @RequestMapping(value="/modify" )
    public String modify(HttpServletRequest request, Model model,
                         @ModelAttribute("fDto") @Valid FootBallDto fdto,
                         BindingResult result,
                         @RequestParam(value="page", defaultValue="1") int page,
                         @RequestParam(value="whatColumn", required=false) String whatColumn,
                         @RequestParam(value="keyword", required=false) String keyword){

        System.out.println("modify()");
        System.out.println("fdto.getId() : " + fdto.getId());
        System.out.println("whatColumn:"+whatColumn);
        System.out.println("keyword:"+keyword);

        model.addAttribute("nationList", List.of("한국", "미국", "독일", "스페인"));
        List<String> round16List = List.of("한국", "멕시코", "독일", "브라질", "스위스", "잉글랜드");
        model.addAttribute("round16List", round16List);

        model.addAttribute("whatColumn", whatColumn);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);

        if(result.hasErrors()) {

            return "content_view";
        }

        footballMapper.updateFootball(fdto);

        /*
        우승국가 한국 검색 후 마지막 3페이지에 하나 남은 레코드를 수정하는데
        수정 화면에서 한국 대신 다른 나라를 선택하면
        이제 한국 검색어로는 3페이지가 필요없으니까 아래 코드로 이전 페이지인 2페이지로 이동해야 한다.
        아래 코드가 없으면 에러가 난다.
        */

        // 2. 전체 데이터 개수 조회
        Map<String,Object> params = new HashMap<>();
        params.put("whatColumn", whatColumn);
        params.put("keyword", keyword);

        //int totalCount = travelMapper.getCount(whatColumn, keyword);
        int totalCount = footballMapper.getCount(params);
        System.out.println("totalCount:" + totalCount);

        // 3. 전체 페이지 수 계산 (예: 한 페이지당 10개)
        int pageSize = 3;
        int totalPage = (int) Math.ceil((double) totalCount / pageSize);
        System.out.println("totalPage:" + totalPage);

        // 4. 현재 페이지가 전체 페이지보다 크면 조정
        if (page > totalPage) {
            page = totalPage > 0 ? totalPage : 1; // 게시글 다 삭제된 경우 1페이지로
        }


        //return "redirect:list";
        String encodedKeyword = keyword != null ? URLEncoder.encode(keyword, StandardCharsets.UTF_8) : "";

        return "redirect:/list?page=" + page
                + (whatColumn != null ? "&whatColumn=" + whatColumn : "")
                + (!encodedKeyword.isEmpty() ? "&keyword=" + encodedKeyword : "");

    }

    @GetMapping("/delete")
    public String delete(@RequestParam("num") int num,
                         @RequestParam("page") int page,
                         @RequestParam(value="whatColumn", required=false) String whatColumn,
                         @RequestParam(value="keyword", required=false) String keyword ) {

        footballMapper.deleteFootball(num); // insertPerson이 mapper의 id인가??

        //return "redirect:/list"; // 입력 후 목록으로 이동

        // 2. 전체 데이터 개수 조회
        Map<String,Object> params = new HashMap<>();
        params.put("whatColumn", whatColumn);
        params.put("keyword", keyword);

        //int totalCount = travelMapper.getCount(whatColumn, keyword);
        int totalCount = footballMapper.getCount(params);
        System.out.println("totalCount:" + totalCount);
        // 3. 전체 페이지 수 계산 (예: 한 페이지당 10개)
        int pageSize = 3;
        int totalPage = (int) Math.ceil((double) totalCount / pageSize);
        System.out.println("totalPage:" + totalPage);

        // 4. 현재 페이지가 전체 페이지보다 크면 조정
        if (page > totalPage) {
            page = totalPage > 0 ? totalPage : 1; // 게시글 다 삭제된 경우 1페이지로
        }
        String encodedKeyword = keyword != null ? URLEncoder.encode(keyword, StandardCharsets.UTF_8) : "";

        return "redirect:/list?page=" + page
                + (whatColumn != null ? "&whatColumn=" + whatColumn : "")
                + (!encodedKeyword.isEmpty() ? "&keyword=" + encodedKeyword : "");

    }

}
