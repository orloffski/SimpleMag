package utils;

public class SelectedObject {
    private static Object object;

    public static Object getObject() {
        return object;
    }

    public static void setObject(Object object) {
        SelectedObject.object = object;
    }
}
