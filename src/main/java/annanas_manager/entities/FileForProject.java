package annanas_manager.entities;


import annanas_manager.DTO.CustomFileDTO;
import annanas_manager.DTO.FileForProjectDTO;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.File;

@Entity
@DiscriminatorValue(value = "P")
public class FileForProject extends CustomFile {

    @ManyToOne
    @JoinColumn(name = "project_id")
    protected Project project;

    public FileForProject(String name, File file) {
        super(name);
    }

    public FileForProject() {
    }

//    public static FileForProject fromDTO(CustomFileDTO dto) {
//        return new FileForProject(dto.getName());
//    }

    @Override
    public FileForProjectDTO toDTO() {
        return new FileForProjectDTO(id, name, currentTime);
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
