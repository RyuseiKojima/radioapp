package com.example.radioapp.repository;

import com.example.radioapp.domain.Impression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 感想データ処理
 */
public interface ImpressionRepository extends JpaRepository<Impression, Long> {
    
    /**
     * 番組IDから感想一覧を取得
     * @param programId 番組ID
     * @return 感想一覧
     */
    @Query("SELECT i FROM Impression i WHERE i.episode.program.id = :programId ORDER BY i.id DESC")
    List<Impression> findByProgramId(@Param("programId") Long programId);
}