package it.polimi.db2.marketing.ejb.entities;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "log", schema = "db_marketing")
public class Log implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Temporal(TemporalType.DATE)
    private Date datetime;


    private Integer user_id;

    public Log() {}

    public Log(User user) {
        this.datetime = new Date();
        this.user_id = user.getId();
    }
    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }


}
