package com.sds.hospapp.batch;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.sds.hospapp.domain.Hospital;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

//에러나는 참조클래스 패키지 자동 가져오기 : shift+Alt+o
public class DataDownloadBatchTest {

    @Test //원래 테스트모듈은 실제 소스 위치와 동일하게 만들어서 각 객체를 참조하여 테스트할수 있다. 임포트 필요없음
    public void start(){
        System.out.println("테스트시작");

        //1. 공공데이터 다운로드
        //그런데 인증키 받구 나서 1시간 이상 기다려야 된다니 기다렸다가 해보자... 내일 해보자
        try{
            //제공해준 서비스키는 인코딩한 값으로 호출해야 정상적으로 리턴해준다.
            //RestTemplate 과 HttpURLConnection 은 호출할때 보내는 URL 값의 TYPE가 서로 다르다.
            //RestTemplate-URI, HttpURLConnection-URL
            //RestTemplate는 URL 값이 String 타입이어두 되는 거 같은데, 아무래두 이전버전인거 같다.
            //String 으로 호출할땐 디코딩값으로 보내 내부에서 인코딩하게 하는 방식이다.

            // RestTemplate 방식
            // RestTemplate 에서 파라미터 타입을 String으로 하면 자체 인코딩을 하고(이때 에러남 : SERVICE KEY IS NOT REGISTERED ERROR )
            // URI 타입으로 보내면 그대로 호출해준다. 
            URI url = new URI("http://apis.data.go.kr/B551182/rprtHospService/getRprtHospService?serviceKey=uckte7atD89qizM9shto778tPY%2BfxDlfusHvquvwdfLwNp06DF8sv9s6ml8zYHirqesrQrY2RTlxHUYVS%2FwUtQ%3D%3D&pageNo=1&numOfRows=10&_type=json");
            RestTemplate rt = new RestTemplate();   //외부사이트 http접속
            // String jsonString = rt.getForObject(url, String.class);
            ResponseDto dto = rt.getForObject(url, ResponseDto.class); //여기 URI 객체값을 넣어야 된다. String 넣어서 개고생

            List<Item> hospitals = dto.getResponse().getBody().getItems().getItem(); //요거 에러나서 봤더니, 실제소스 위치랑 달라서 참조 못함
            for(Item item:hospitals){
                System.out.println(item.getYadmNm());
                System.out.println("PCR여부: "+item.getPcrPsblYn());
            }    
            
            // HttpURLConnection 방식
            // URL url = new URL("http://apis.data.go.kr/B551182/rprtHospService/getRprtHospService?serviceKey=uckte7atD89qizM9shto778tPY%2BfxDlfusHvquvwdfLwNp06DF8sv9s6ml8zYHirqesrQrY2RTlxHUYVS%2FwUtQ%3D%3D&pageNo=1&numOfRows=10&_type=json");                        
            // HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // conn.setRequestMethod("GET");
            // conn.setRequestProperty("Content-type", "application/json");

            // System.out.println("Response code: " + conn.getResponseCode());
            // BufferedReader rd;
            // if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            //     rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            // } else {
            //     rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            // }
            // StringBuilder sb = new StringBuilder();
            // String line;
            // while ((line = rd.readLine()) != null) {
            //     sb.append(line);
            // }
            // rd.close();
            // conn.disconnect();
            // System.out.println(sb.toString());            
                    
        }catch(Exception e){
            System.out.println(e); 
        }
        
    }

    @Test //공공API 데이터 받은거 테스트 컬렉션에 담기(전체데이터)
    public void download(){
        
        System.out.println("다운로드 시작");
         
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
            assertEquals(totalCount, items.size()); //junit 함수 : totalCount(예상 값)와 items.size()(실제 값)가 같으면 테스트 통과
        } catch (Exception e) {
            System.out.println(e);
        }
    }    
}
