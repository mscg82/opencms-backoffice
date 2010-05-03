// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package util;

public class Debug
{
    public static void message(String msg)
    {
        System.out.println(msg);
    }
    
    public static void warning(String msg)
    {
        System.out.println("Warning: " + msg);
    }
    
    public static void error(String msg)
    {
        System.out.println("Error: " + msg);
    }
    
    public static void assert2(boolean test, String msg)
    {
        if (!test)
            System.out.println("Assert: " + msg);
    }
}
