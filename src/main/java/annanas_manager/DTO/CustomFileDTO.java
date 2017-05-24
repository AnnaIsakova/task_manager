package annanas_manager.DTO;


import java.io.File;

public class CustomFileDTO {

    protected long id;
    protected String name;
    protected long currentTime;

    public CustomFileDTO(long id, String name, long currentTime) {
        this.id = id;
        this.name = name;
        this.currentTime = currentTime;
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

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

//    public File getFile() {
//        return file;
//    }
//
//    public void setFile(File file) {
//        this.file = file;
//    }
}
