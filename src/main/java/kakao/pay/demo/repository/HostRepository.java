package kakao.pay.demo.repository;

import kakao.pay.demo.model.Host;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface HostRepository extends PagingAndSortingRepository<Host, Integer>{
    Host findByHostName(String name);
}
