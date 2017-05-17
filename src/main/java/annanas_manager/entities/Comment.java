package annanas_manager.entities;


import annanas_manager.DTO.CommentDTO;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "comments")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "comment_role")
public abstract class Comment {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name= "increment", strategy= "increment")
    @Column(name = "id")
    protected long id;

    @Column(name = "text", nullable = false)
    protected String text;

    @ManyToOne
    @JoinColumn(name = "user_from_id")
    protected CustomUser userFrom;

    @Column(nullable = false)
    @Type(type="timestamp")
    protected Date createDate;

    public Comment() {
    }

    public Comment(String text) {
        this.text = text;
    }

    public CommentDTO toDTO() {
        return new CommentDTO(id, text, userFrom.toDTO(), createDate);
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public CustomUser getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(CustomUser userFrom) {
        this.userFrom = userFrom;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
