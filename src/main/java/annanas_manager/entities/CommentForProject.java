package annanas_manager.entities;


import annanas_manager.DTO.CommentDTO;
import annanas_manager.DTO.CommentForProjectDTO;
import annanas_manager.DTO.DeveloperDTO;
import annanas_manager.DTO.TeamleadDTO;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue(value = "P")
public class CommentForProject extends Comment{

    @ManyToOne
    @JoinColumn(name = "project_id")
    protected Project project;

    public CommentForProject() {
    }

    public CommentForProject(String text) {
        super(text);
    }

    @Override
    public CommentForProjectDTO toDTO() {
        return new CommentForProjectDTO(id, text, userFrom.toDTO(), createDate, lastModified);
    }

    public static CommentForProject fromDTO(CommentForProjectDTO dto){
        return new CommentForProject(dto.getText());
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
