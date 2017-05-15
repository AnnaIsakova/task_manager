package annanas_manager.entities;


import annanas_manager.DTO.DeveloperDTO;
import annanas_manager.DTO.ProjectDTO;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name= "increment", strategy= "increment")
    @Column(name = "id", length = 6)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "details")
    private String details;

    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private CustomUser createdBy;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name="projects_developers", joinColumns=@JoinColumn(name="project_id"), inverseJoinColumns=@JoinColumn(name="developer_id"))
    private List<Developer> developers = new ArrayList<>();

    @Column(nullable = false)
    @Type(type="timestamp")
    private Date createDate;

    @Column(name = "deadline", nullable = false)
    private Calendar deadline;

    public Project() {
    }

    public Project(String name, String description, String details, Date createDate, Calendar deadline) {
        this.name = name;
        this.description = description;
        this.details = details;
        this.createDate = createDate;
        this.deadline = deadline;
    }

    public ProjectDTO toDTO() {
        List<DeveloperDTO> developersDTO = new ArrayList<>();
        for (Developer dev : this.developers) {
            developersDTO.add(dev.toDTO());
        }
        return new ProjectDTO(id, name, description, details, createdBy.toDTO(), createDate, deadline, developersDTO);
    }

    public static Project fromDTO(ProjectDTO dto) {
        return new Project(
                dto.getName(),
                dto.getDescription(),
                dto.getDetails(),
                dto.getCreateDate(),
                dto.getDeadline());
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public CustomUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CustomUser createdBy) {
        this.createdBy = createdBy;
    }

    public List<Developer> getDevelopers() {
        return developers;
    }

    public void setDevelopers(List<Developer> developers) {
        this.developers = developers;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Calendar getDeadline() {
        return deadline;
    }

    public void setDeadline(Calendar deadline) {
        this.deadline = deadline;
    }
}
