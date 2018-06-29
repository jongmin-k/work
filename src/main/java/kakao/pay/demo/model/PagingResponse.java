package kakao.pay.demo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.Collection;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PagingResponse {
    private Object data;
    private Long totalCount;
    private Integer pageCount;
    private Integer dataCount;

    public PagingResponse(){

    }
    public PagingResponse(Page page) {
        if (page != null) {
            this.data = page.getContent();
            this.totalCount = page.getTotalElements();
            this.pageCount = page.getTotalPages();
            if (data != null && data instanceof Collection) {
                this.dataCount = ((Collection) data).size();
            }
        }
    }
}
