package com.sds.hospapp.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@AllArgsConstructor
@NoArgsConstructor  //이거 기본생성자 꼭 입력하자
@ToString
@Getter
@Entity
public class Hospital {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; //auto_increment

    private String addr;    //병원주소
    private Integer mgtStaDd;   //운영시작일자
    private String pcrPsblYn;   //pcr검사여부
    private String ratPsblYn;   //호흡기전담클리닉여부
    private Integer recuClCd;   //요양종별코드
    private String sgguCdNm;    //시군구명
    private String sidoCdNm;    //시도명
    private Double xPosWgs84;   //x좌표(위도)
    private String yPosWgs84;   //y좌표(경도)
    private String yadmNm;  //요양기관명
    private String ykihoEnc;    //암호화된 요양기호
    private Integer xPos;   //x좌표(좌표)
    private Integer yPos;   //y좌표(좌표)

}
