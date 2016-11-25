package it.tabasoft.fragmentstate;

/**
 * Created by valfer on 25/11/16.
 */

public class MyData {

    private String str = "";

    private static MyData ref;

    public static synchronized MyData sharedInstance() {
        if (ref == null)
            ref = new MyData();
        return ref;
    }

    @SuppressWarnings("CloneDoesntCallSuperClone")
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }
}
