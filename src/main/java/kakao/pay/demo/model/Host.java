package kakao.pay.demo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "T_HOST")
@SequenceGenerator(name = "hostIdGenerator", sequenceName = "hostIdSequence", initialValue = 1, allocationSize = 1)
public class Host {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hostIdGenerator")
    private Integer hostId;

    @Column(unique = true)
    private String hostName;

    @Transient
    private BigInteger visitedCount;

    @Transient
    private Date lastVisitedDate;

    @Transient
    private Integer index;


    public Host(){}

    public Host(String hostName) {
        this.hostName = hostName;
    }

    public Host(Integer index, String hostName, BigInteger visitedCount) {
        this.index = index;
        this.hostName = hostName;
        this.visitedCount = visitedCount;
    }
    public Host(Integer index, String hostName, Date lastVisitedDate) {
        this.index = index;
        this.hostName = hostName;
        this.lastVisitedDate = lastVisitedDate;
    }
}
