package annanas_manager.validators;


import java.util.Calendar;

public class DeadlineValidator {

    public static boolean isDeadlineValid(Calendar deadline){
        Calendar now = Calendar.getInstance();
        now.clear(Calendar.HOUR_OF_DAY);
        now.clear(Calendar.MINUTE);
        now.clear(Calendar.SECOND);
        now.clear(Calendar.MILLISECOND);
        if (!now.after(deadline)){
            return true;
        }
        return false;
    }
}
