package kakao.pay.demo.repository;

import kakao.pay.demo.model.VisitHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface VisitHistoryRepository extends PagingAndSortingRepository<VisitHistory, Integer>{
    Page<VisitHistory> findAll(Pageable pageable);
}
