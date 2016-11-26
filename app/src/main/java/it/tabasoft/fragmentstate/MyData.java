package it.tabasoft.fragmentstate;

/**
 * Created by valfer on 25/11/16. Tabasoft Srls
 */

class MyData {

    private String str = "";

    private static MyData ref;

    static synchronized MyData sharedInstance() {
        if (ref == null)
            ref = new MyData();
        return ref;
    }

    @SuppressWarnings("CloneDoesntCallSuperClone")
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    String getStr() {
        return str;
    }

    void setStr(String str) {
        this.str = str;
    }
}
