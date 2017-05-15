package annanas_manager.DTO;


import java.io.File;

public class CustomFileDTO {

    protected long id;
    protected String name;
//    protected File file;

    public CustomFileDTO(long id, String name) {
        this.id = id;
        this.name = name;
//        this.file = file;
    }

    public CustomFileDTO() {
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

//    public File getFile() {
//        return file;
//    }
//
//    public void setFile(File file) {
//        this.file = file;
//    }
}
