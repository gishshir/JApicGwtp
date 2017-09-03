package fr.tsadeo.app.japicgwtp.server.manager.scan;

public class PathInfo {

    private final String path;
    private final String packageName;
    private final String classname;
    private final FileType fileType;
    
    //----------------------------------- constuctor
    public PathInfo(String path, String packageName, String classname, FileType fileType) {
        this.path = path;
        this.packageName = packageName;
        this.classname = classname;
        this.fileType = fileType;
    }

    //-------------------------------- accessors
    public FileType getFileType() {
        return fileType;
    }

    public String getPath() {
        return path;
    }

    public String getPackageName() {
        return packageName;
    }


    public String getClassname() {
        return classname;
    }

    public String toString() {
        return "PathInfo: " + path + " - " + this.packageName + " - " + this.classname;
    }
}
