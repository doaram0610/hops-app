package com.sds.hospapp.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

//JPA 형식 : public <T, Id>
public interface HospitalRepository extends JpaRepository<Hospital, Integer>{  
    
    @Query(value = "SELECT distinct sidoCdNm FROM hospital order by sidoCdNm", nativeQuery = true)
    public List<String> mFindSidoCdNm();    

    @Query(value = "SELECT distinct sgguCdNm FROM hospital where sidoCdNm = :sidoCdNm order by sgguCdNm", nativeQuery = true)
    public List<String> mFindSgguCdNm(@Param("sidoCdNm") String sidoCdNm); 
    
    @Query(value = "SELECT * FROM hospital WHERE sidoCdNm = :sidoCdNm AND sgguCdNm = :sgguCdNm AND pcrPsblYn = 'Y'", nativeQuery = true)
    public List<Hospital> mFindHospital(@Param("sidoCdNm") String sidoCdNm, @Param("sgguCdNm") String sgguCdNm);
    
}
