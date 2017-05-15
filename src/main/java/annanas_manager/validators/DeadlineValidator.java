package annanas_manager.validators;


import java.util.Calendar;

public class DeadlineValidator {

    public static boolean isDeadlineValid(Calendar deadline){
        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);
        System.out.println("deadline: " + deadline.getTime());
        System.out.println("now: " + now.getTime());
        if (!now.after(deadline)){
            return true;
        }
        return false;
    }
}
