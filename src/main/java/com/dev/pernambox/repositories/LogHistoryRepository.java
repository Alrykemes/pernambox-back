package com.dev.pernambox.repositories;

import com.dev.pernambox.domain.loghistory.LogHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LogHistoryRepository extends JpaRepository<LogHistory, Long> {
    @Query("SELECT o FROM LogHistory o")
    Page<LogHistory> getAll(Pageable pageable);

    <S  extends LogHistory> S save(S operation);
}
