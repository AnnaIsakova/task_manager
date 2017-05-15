package annanas_manager.entities;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "T")
public class FileForTask extends CustomFile {
}
