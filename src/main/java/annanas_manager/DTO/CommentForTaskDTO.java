package annanas_manager.DTO;


import java.util.Date;

public class CommentForTaskDTO extends CommentDTO{

    public CommentForTaskDTO(long id, String text, CustomUserDTO userFrom, Date createDate, Date lastModefied) {
        super(id, text, userFrom, createDate, lastModefied);
    }

    public CommentForTaskDTO() {
    }
}
