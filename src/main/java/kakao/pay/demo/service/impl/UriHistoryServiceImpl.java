package kakao.pay.demo.service.impl;

import kakao.pay.demo.model.Host;
import kakao.pay.demo.model.VisitHistory;
import kakao.pay.demo.repository.HostRepository;
import kakao.pay.demo.repository.VisitHistoryRepository;
import kakao.pay.demo.service.UriHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UriHistoryServiceImpl implements UriHistoryService {
    @Autowired
    HostRepository hostRepository;

    @Autowired
    VisitHistoryRepository visitHistoryRepository;

    @PersistenceContext
    EntityManager em;

    @Override
    @Transactional
    public VisitHistory saveHistory(String uri) throws URISyntaxException {
        URI uriObj = new URI(uri);
        if( uriObj.getHost() == null ){
            throw new URISyntaxException(uri,"invalid uriObj syntax");
        }
        Host host = hostRepository.findByHostName(uriObj.getHost());
        if ( host == null ){
            host = hostRepository.save(new Host(uriObj.getHost()));
        }
        return visitHistoryRepository.save(new VisitHistory(host, uri));
    }

    @Override
    @Transactional
    public Page<VisitHistory> getRecentVisited(Pageable pageable) {
        return visitHistoryRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public List<Host> getMostFrequentlyVisited() {
        List<Host>  result = null;
        List<Object[]> queryResult = em.createNativeQuery(
                "SELECT T.HOST_ID, T.HOST_NAME, COUNT(HISTORY_ID) AS VISITED_COUNT "+
                        "FROM T_VISIT_HISTORY V " +
                        "LEFT JOIN T_HOST T ON V.HOST_ID = T.HOST_ID "+
                        "GROUP BY T.HOST_ID "+
                        "ORDER BY VISITED_COUNT DESC "+
                        "LIMIT 5").getResultList();

        for ( int i = 0 ; i < queryResult.size() ; i++ ) {
            if (result == null) {
                result = new ArrayList<>();
            }
            Object[] objArray = queryResult.get(i);
            result.add(new Host(new Integer(i+1), (String) objArray[1], (BigInteger) objArray[2]));
        }

        return result;
    }

    @Override
    @Transactional
    public List<Host> getLeastRecentlyVisited() {
        List<Host>  result = null;
        List<Object[]> queryResult = em.createNativeQuery(
                "SELECT T.HOST_ID, T.HOST_NAME, MAX(VISIT_DATE) AS LAST_VISITED_DATE "+
                        "FROM T_VISIT_HISTORY V " +
                        "LEFT JOIN T_HOST T ON V.HOST_ID = T.HOST_ID "+
                        "GROUP BY T.HOST_ID "+
                        "ORDER BY LAST_VISITED_DATE ASC "+
                        "LIMIT 5").getResultList();

        for ( int i = 0 ; i < queryResult.size() ; i++ ) {
            if (result == null) {
                result = new ArrayList<>();
            }
            Object[] objArray = queryResult.get(i);
            result.add(new Host(new Integer(i+1), (String) objArray[1], (Date) objArray[2]));
        }

        return result;
    }
}
