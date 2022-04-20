//첫번째 도,시 드롭앤드랍 선택 변경하면
let sidoCdNmDom = document.querySelector("#sidoCdNm");
sidoCdNmDom.addEventListener("change", (e) => {
  console.log(e.target.value);

  let sidoCdNm = e.target.value;

  getSuggucdnm(sidoCdNm);
});

//두번째 시,군,구 가져오기
let getSuggucdnm = async (sidoCdNm) => {
  //백틱 숫자 1옆에있는 `사용하면 자바스크립트 변수값 바인딩 가능
  let response = await fetch(
    `http://13.125.172.102:8080/api/sggucdnm?sidoCdNm=${sidoCdNm}`
  );
  let responsePasing = await response.json();
  // console.log(responsePasing);
  setSuggcdnm(responsePasing);
};

//가져온 동,읍,면 데이터를 드롭앤드랍 박스에 넣기
let setSuggcdnm = (responsePasing) => {
  let suggcdnmDom = document.querySelector("#sgguCdNm");
  suggcdnmDom.innerHTML = "";

  //리턴이 필요없으니(그리고 끝) forEach로
  responsePasing.forEach((e) => {
    let optionEL = document.createElement("option");
    optionEL.text = e;
    suggcdnmDom.append(optionEL);
  });
};

//조회하기 버튼 누르면
document.querySelector("#btn-submit").addEventListener("click", (e) => {
  let sidoCdNm = document.querySelector("#sidoCdNm").value;
  let suggcdnm = document.querySelector("#sgguCdNm").value;

  getHospital(sidoCdNm, suggcdnm);
});

//도,시군구 정보로 병원목록 가져오기
let getHospital = async (sidoCdNm, suggCdnm) => {
  let response = await fetch(
    `http://13.125.172.102:8080/api/hospital?sidoCdNm=${sidoCdNm}&suggCdnm=${suggCdnm}`
  );
  let responsePasing = await response.json();
  // console.log(responsePasing);
  setHospital(responsePasing);
};

//병원목록 뿌리기
setHospital = (responsePasing) => {
  let tbodyHospitalDom = document.querySelector("#tbody-hospital");
  tbodyHospitalDom.innerHTML = "";

  //반복하면서 한행씩 그려준다.
  responsePasing.forEach((e) => {
    let trEl = document.createElement("tr");
    let tdEL1 = document.createElement("td");
    let tdEL2 = document.createElement("td");
    let tdEL3 = document.createElement("td");

    tdEL1.innerHTML = e.yadmNm;
    tdEL2.innerHTML = e.pcrPsblYn;
    tdEL3.innerHTML = e.addr;

    trEl.append(tdEL1);
    trEl.append(tdEL2);
    trEl.append(tdEL3);

    tbodyHospitalDom.append(trEl);
  });
};
