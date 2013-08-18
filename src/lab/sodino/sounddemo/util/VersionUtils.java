package lab.sodino.sounddemo.util;

import android.os.Build;

public final class VersionUtils
{
    private VersionUtils()
    {
    }
    
    /**
     * 版本是否在2.1之后（API 7）
     * @return
     */
    public static boolean isECLAIR_MR1(){
        return Build.VERSION.SDK_INT>=Build.VERSION_CODES.ECLAIR_MR1;
    }

    /**
     * 版本是否在2.2之后(API 8)
     * 
     * @return
     */
    public static boolean isrFroyo()
    {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }


}
