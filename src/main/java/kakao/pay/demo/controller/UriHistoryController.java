package kakao.pay.demo.controller;

import kakao.pay.demo.model.UiDataModel;
import kakao.pay.demo.model.VisitHistory;
import kakao.pay.demo.service.UriHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URISyntaxException;
import java.security.InvalidParameterException;

@Controller
public class UriHistoryController {
    @Autowired
    UriHistoryService uriHistoryService;

    @RequestMapping("/")
    public String home(Model model, @PageableDefault(size = 10) Pageable pageable) {
        UiDataModel dataModel = new UiDataModel();
        Page<VisitHistory> page = uriHistoryService.getRecentVisited(pageable);
        dataModel.setPage(pageable.getPageNumber());
        if ( page.getTotalPages() > 0 )
            dataModel.setTotalPages(page.getTotalPages());
        model.addAttribute("dataModel", dataModel);
        model.addAttribute("recentlyVisited", page.getContent()) ;
        model.addAttribute("leastRecentlyVisited", uriHistoryService.getLeastRecentlyVisited());
        model.addAttribute("mostFrequentlyVisited", uriHistoryService.getMostFrequentlyVisited());
        return "home";
    }

    @PostMapping(value = "/")
    public String input(@ModelAttribute UiDataModel dataModel) throws URISyntaxException {
        if( dataModel == null || dataModel.getUri() == null || dataModel.getUri().isEmpty()){
            throw new InvalidParameterException();
        }
        uriHistoryService.saveHistory(dataModel.getUri());
        return "redirect:/?page="+dataModel.getPage();
    }
}
