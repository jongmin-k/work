package kakao.pay.demo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Getter
@Setter
@Entity
@Table(name = "T_VISIT_HISTORY")
@SequenceGenerator(name = "historyIdGenerator",sequenceName = "historyIdSequence", initialValue = 1, allocationSize = 1)
public class VisitHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "historyIdGenerator")
    private Integer historyId;

    private String uri;

    private Date visitDate;

    @ManyToOne(targetEntity=Host.class, fetch=FetchType.EAGER)
    @JoinColumn(name="HOST_ID")
    private Host host;

    public VisitHistory(){
    }

    public VisitHistory(Host host, String uri) {
        this.uri = uri;
        this.host = host;
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        this.visitDate =  calendar.getTime();
    }
}
