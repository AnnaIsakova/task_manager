package annanas_manager.DTO;


import java.util.Date;

public class CommentForProjectDTO extends CommentDTO{

    public CommentForProjectDTO(long id, String text, CustomUserDTO userFrom, Date createDate) {
        super(id, text, userFrom, createDate);
    }

    public CommentForProjectDTO() {
    }
}
