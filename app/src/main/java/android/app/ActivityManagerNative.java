package android.app;

/**
 * Created by sunyingying on 2015/8/18.
 * override method packagename must be same of source method packagename
 */
public abstract class ActivityManagerNative {
    public static IActivityManager getDefault(){
        return null;
    }
}
