package kakao.pay.demo.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UiDataModel {
    private  String uri;
    private int page = 0;
    private int totalPages = 1;
}
