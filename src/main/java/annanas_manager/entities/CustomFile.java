package annanas_manager.entities;


import annanas_manager.DTO.CustomFileDTO;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.File;

@Entity
@Table(name = "files")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "file_role")
public abstract class CustomFile {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name= "increment", strategy= "increment")
    @Column(name = "id")
    protected long id;

    @Column(name = "name", nullable = false)
    protected String name;

    @Column(name = "currentTime", nullable = false)
    protected long currentTime;

    @Transient
    protected File file;

    public CustomFile(String name, File file) {
        this.name = name;
        this.file = file;
    }

    public CustomFile() {
    }

    public CustomFileDTO toDTO() {
        return new CustomFileDTO(id, name);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }
}