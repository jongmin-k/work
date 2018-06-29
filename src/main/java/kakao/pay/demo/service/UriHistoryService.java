package kakao.pay.demo.service;

import kakao.pay.demo.model.Host;
import kakao.pay.demo.model.VisitHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.URISyntaxException;
import java.util.List;

public interface UriHistoryService {
    VisitHistory saveHistory(String uri) throws URISyntaxException;
    Page<VisitHistory> getRecentVisited(Pageable pageable);
    List<Host> getMostFrequentlyVisited();
    List<Host> getLeastRecentlyVisited();
}
