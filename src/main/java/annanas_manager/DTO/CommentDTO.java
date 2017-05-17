package annanas_manager.DTO;


import java.util.Date;

public class CommentDTO {
    protected long id;

    protected String text;
    protected CustomUserDTO userFrom;
    protected Date createDate;

    public CommentDTO(long id, String text, CustomUserDTO userFrom, Date createDate) {
        this.id = id;
        this.text = text;
        this.userFrom = userFrom;
        this.createDate = createDate;
    }

    public CommentDTO() {
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

    public CustomUserDTO getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(CustomUserDTO userFrom) {
        this.userFrom = userFrom;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
