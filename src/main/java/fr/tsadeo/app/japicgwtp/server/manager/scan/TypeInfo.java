package fr.tsadeo.app.japicgwtp.server.manager.scan;

import org.apache.commons.lang3.StringUtils;
public class TypeInfo {
    private String name;
    private String packageName;
    private String declaration;
    private String path;
    private String sinceVersion;

    public String getSinceVersion() {
        return sinceVersion;
    }

    public void setSinceVersion(String sinceVersion) {
        this.sinceVersion = sinceVersion;
    }
    private FileType fileType;

    public boolean isFunctionalInterface() {
        return StringUtils.contains(declaration, "@FunctionalInterface");
    }

    public String getDeclaration() {
        return declaration;
    }

    public void setDeclaration(String declaration) {
        this.declaration = declaration;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getId() {
        String id = packageName + name;
        return StringUtils.remove(id, '.');
    }




    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String toString(){
    	return this.name + " - path: " + this.path + " - packageName: " + this.packageName + " [" + this.declaration + "]";
    }

}