package com.sds.hospapp.web;

import java.util.List;

import com.sds.hospapp.domain.Hospital;
import com.sds.hospapp.domain.HospitalRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor    //final 객체의 생성자 만들기
@Controller
public class HospitalController {
    
    private final HospitalRepository hospitalRepository;

    @GetMapping("/")
    public String home(){
        return "home";  // resources/templates/home.mustache 찾음
    }

    //기본 도,시,군 가져오기
    @GetMapping("/search")
    public String search(Model model){

            model.addAttribute("sidoCdNm", hospitalRepository.mFindSidoCdNm());
            model.addAttribute("sgguCdNm", hospitalRepository.mFindSgguCdNm("강원")); //위의 콤보박스에서 첫인덱스 값
            return "search";  // resources/templates/search.mustache 찾음
    }
    
    //군,구 가져오기
    @GetMapping("/api/sggucdnm") //응답 json
    public @ResponseBody List<String> sggucdnm(String sidoCdNm){
        return hospitalRepository.mFindSgguCdNm(sidoCdNm);
    }

    //시군구 에 해당하는 병원목록 가져오기
    @GetMapping("/api/hospital") //응답 json
    public @ResponseBody List<Hospital> sggucdnm(String sidoCdNm, String suggCdnm){
        return hospitalRepository.mFindHospital(sidoCdNm, suggCdnm);
    }    
}
