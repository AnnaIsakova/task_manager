package annanas_manager.DTO;


import java.util.Date;

public class CommentForProjectDTO extends CommentDTO{

    public CommentForProjectDTO(long id, String text, CustomUserDTO userFrom, Date createDate, Date lastModefied) {
        super(id, text, userFrom, createDate, lastModefied);
    }

    public CommentForProjectDTO() {
    }
}
