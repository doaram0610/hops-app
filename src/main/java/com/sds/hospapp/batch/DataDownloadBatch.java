package com.sds.hospapp.batch;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.sds.hospapp.domain.Hospital;
import com.sds.hospapp.domain.HospitalRepository;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;


//하루에 ..씩 다운로드 해서 DB에 변경해주기
//PCR검사기관이 추가될수 있으니까 
//가져올때마다 삭제추 다시 추가
@RequiredArgsConstructor
@Component
public class DataDownloadBatch {
    
    private final HospitalRepository hospitalRepository;
    
    //초 분 시 일 월 주 : 한칸씩 띄어쓰기 해야됨 예) "0 48 * * * *" -> 매시 48분마다 실행
    @Scheduled(cron = "0 52 * * * *", zone ="Asia/Seoul")   //매시 00분마다 실행
    public void startBatch(){

        // System.out.println("1분마다 실행됨");

        //담을 그릇 준비
        List<Hospital> hospitals = new ArrayList<>();
                
        //api 한번 호출해서 totalcount 확인
        RestTemplate rt = new RestTemplate();   //외부사이트 http접속
        int totalCount = 2; // 1로 하면 리턴값 중 item 이 컬렉션이 아닌값으로 리턴되서 파싱에러난다. 그래서 2로 바꿈 

        try {
            //호출할때 1건만 가져오기로 호출해도 전체카운드수를 리턴해준다.
            URI totalCountUrl = new URI("http://apis.data.go.kr/B551182/rprtHospService/getRprtHospService?serviceKey=uckte7atD89qizM9shto778tPY%2BfxDlfusHvquvwdfLwNp06DF8sv9s6ml8zYHirqesrQrY2RTlxHUYVS%2FwUtQ%3D%3D&pageNo=1&numOfRows="
            +totalCount+"&_type=json"); 

            // String totalCountDto = rt.getForObject(totalCountUrl, String.class);
            // System.out.println("가져온 데이터 totalCount : "+totalCountDto);

            ResponseDto totalCountDto = rt.getForObject(totalCountUrl, ResponseDto.class);
            totalCount = totalCountDto.getResponse().getBody().getTotalCount();
            System.out.println("가져온 데이터 totalCount : "+totalCount);

            // System.out.println("가져온 totalcount 값으로 전체데이터 추출 호출 시작 =============================");
            // //totalCount 만큼 한번에 가져오기     
            URI url = new URI("http://apis.data.go.kr/B551182/rprtHospService/getRprtHospService?serviceKey=uckte7atD89qizM9shto778tPY%2BfxDlfusHvquvwdfLwNp06DF8sv9s6ml8zYHirqesrQrY2RTlxHUYVS%2FwUtQ%3D%3D&pageNo=1&numOfRows="
            +totalCount+"&_type=json");
            ResponseDto dto = rt.getForObject(url, ResponseDto.class);

            List<Item> items = dto.getResponse().getBody().getItems().getItem();
            System.out.println("가져온 데이터 사이즈 : "+items.size());

            //컬렉션 복사 (서버에서 받은 컬렉션 items 를 내가 DB에 저장할려구 만든 hospitals 에 복사)
            //그런데 서버에서 받은 items 의 메소드와 내가 만든 hospitals의 메소드가 다르니까
            //필요한 것만 hospitals에 넣으면서 복사한다.
            hospitals = items.stream().map(
                (e)->{
                    return Hospital.builder()   //builder패턴은 생성자메소드보다 내가 선별해서 여러개를 생성할수 있어서 이걸 사용하는게 좋다
                        .addr(e.getAddr())
                        .mgtStaDd(e.getMgtStaDd())
                        .pcrPsblYn(e.getPcrPsblYn())
                        .ratPsblYn(e.getRatPsblYn())
                        .recuClCd(e.getRecuClCd())
                        .sgguCdNm(e.getSgguCdNm())
                        .sidoCdNm(e.getSidoCdNm())
                        .xPosWgs84(e.getXPosWgs84())
                        .yPosWgs84(e.getYPosWgs84())
                        .yadmNm(e.getYadmNm())
                        .ykihoEnc(e.getYkihoEnc())
                        .xPos(e.getXPos())
                        .yPos(e.getYPos())                        
                        .build();
                }
            ).collect(Collectors.toList()); //jdk8이상 스트림의 아이템들을 List 객체로 리턴

            //기존 데이터 다 삭제하고 다시 입력하자
            hospitalRepository.deleteAll();

            //이제 담은 그릇의 데이터를 저장하자(1시간마다 실행)
            hospitalRepository.saveAll(hospitals);

        } catch (Exception e) {
            System.out.println(e);
        }        
    }

}
