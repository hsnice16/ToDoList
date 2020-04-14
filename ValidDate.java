package javaproject;

public class ValidDate {
    public static boolean isValid(int day, int month, int year){
        if ( (month < 13) && (month > 0) && (1000 < year) && ( year < 9999)  )
        {
            if ( month == 2)
            {
                if(year%4 == 0 && day <= 29)
                {
                    return true ;
                }
                else if(year%4 == 0 && day <= 28 )
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else if ( month%2 == 0 && day <= 30 )
            {
                return true;    
            }
            else if( month%2 != 0 && day <= 31)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false ;
        }
    }
}