package kakao.pay.demo.service;

import kakao.pay.demo.model.Host;
import kakao.pay.demo.model.VisitHistory;
import kakao.pay.demo.repository.HostRepository;
import kakao.pay.demo.repository.VisitHistoryRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@Rollback
@Transactional
public class UriHistoryServiceTest {

    @Autowired
    UriHistoryService historyService;
    @Autowired
    VisitHistoryRepository visitHistoryRepository;
    @Autowired
    HostRepository hostRepository;

    private LinkedHashMap<String,List<String>> uriMap;
    private String[] hosts = null;
    private int getTotalEliment(){
        int totalElement = 0;
        for ( List<String> list : uriMap.values() ){
            totalElement += list.size();
        }
        return totalElement;
    }

    @Before
    public void createMap(){
        visitHistoryRepository.deleteAll();
        hostRepository.deleteAll();
        uriMap = new LinkedHashMap<>();
        hosts = new String[]{"www.naver.com","www.google.com","www.daum.com","mail.google.com","docs.google.com"};


        for ( int i = 0 ; i < hosts.length ; i++ ){
            List<String> list = new ArrayList<>();
            for( int j = 0 ; j <= i ; j++ ){
                list.add("http://"+hosts[i]+"/"+j);
            }
            uriMap.put(hosts[i], list);
        }
    }

    @Test
    public void saveHistory() throws Exception{

        for (Map.Entry<String, List<String>> entry : uriMap.entrySet() ){
            for ( String uri : entry.getValue()){
                VisitHistory history = historyService.saveHistory(uri);
                Assert.assertNotNull(history);
                Assert.assertNotNull(history.getHistoryId());
                Assert.assertNotNull(history.getUri());
                Assert.assertEquals(history.getUri(), uri);
                Assert.assertNotNull(history.getVisitDate());
                Assert.assertNotNull(history.getHost());
                Assert.assertNotNull(history.getHost().getHostId());
                Assert.assertNotNull(history.getHost().getHostName());
                Assert.assertEquals(history.getHost().getHostName(), entry.getKey());
            }
        }
    }

    @Test(expected = URISyntaxException.class)
    public void nonProtocolUriSyntexExceptionTest() throws URISyntaxException {
        historyService.saveHistory("localhost");
    }
    
    @Test
    public void getRecentVisited() throws Exception{
        setDummy();
        Pageable pageable = new PageRequest(0, Integer.MAX_VALUE);
        Page<VisitHistory> histories = historyService.getRecentVisited(pageable);
        Assert.assertEquals(histories.getTotalPages(),1);
        Assert.assertEquals(histories.getTotalElements(), getTotalEliment());
    }

    @Test
    public void pagenation() throws Exception{
        setDummy();
        int size = 5;
        int totalPages = 1;
        int totalElements = 0;
        for ( int i = 0; i < totalPages ; i++ ){
            Pageable pageable = new PageRequest(i, size);
            Page<VisitHistory> histories = historyService.getRecentVisited(pageable);
            totalPages = histories.getTotalPages();
            int contentSize = histories.getContent().size();
            totalElements += contentSize;
            Assert.assertEquals(contentSize, size);
        }
        Assert.assertEquals(totalElements, getTotalEliment());
    }

    @Test
    public void getMostFrequentlyVisited() throws Exception{
        setDummy();
        List<Host> hosts = historyService.getMostFrequentlyVisited();
        Assert.assertEquals(hosts.size(), 5);
        for ( int i = 0; i < hosts.size(); i++ ){
            Assert.assertEquals(hosts.get(i).getHostName(), this.hosts[this.hosts.length-1-i]);
        }
    }

    @Test
    public void getLeastRecentlyVisited() throws Exception{
        setDummy();
        List<Host> hosts = historyService.getLeastRecentlyVisited();
        Assert.assertEquals(hosts.size(), 5);

        for ( int i = 0; i < hosts.size(); i++ ){
            Assert.assertEquals(hosts.get(i).getHostName(), this.hosts[i]);
        }

    }


    private void setDummy() throws URISyntaxException {
        for (Map.Entry<String, List<String>> entry : uriMap.entrySet() ){
            for ( String uri : entry.getValue()){
                VisitHistory history = historyService.saveHistory(uri);
            }
        }
    }

}