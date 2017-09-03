package fr.tsadeo.app.japicgwtp.server.manager.scan;

/**
 * @author Benjamin Winterberg
 */
public enum FileType {
    CLASS ("primary", "class".length()),
    INTERFACE ("info", "interface".length()),
    ENUM ("warning", "enum".length()),
    ANNOTATION ("danger", "annotation".length()),
    UNKNOWN ("", 0);
    
    private final String style;
    private final int length;
    private FileType(String style, int length) {
        this.style = style;
        this.length = length;
    }

    public String getStyle() {
        return style;
    }

    public int getLength() {
        return length;
    }
    

    public static FileType ofTitle(String title) {
        if (title.startsWith("class")) {
            return CLASS;
        }
        if (title.startsWith("interface")) {
            return INTERFACE;
        }
        if (title.startsWith("enum")) {
            return ENUM;
        }
        if (title.startsWith("annotation")){
            return ANNOTATION;
        }
        return UNKNOWN;
    }
}