import java.io.IOException;
import java.util.List;

/**
 *proba
 * @author Jovan Simic
 * Petar Tisic
 */


public abstract class MyExporter {

    protected String fileName;


    public MyExporter() {
    }

    public abstract String initStorage(String path) throws IOException;

    public abstract void save(String sourceFilePath, String destinationFilePath) throws IOException;

    public abstract void createFolder(String path) throws IOException;

    public abstract void createFile(String path) throws IOException;

    public abstract void deleteFolder(String path) throws IOException;

    public abstract void deleteFile(String path) throws IOException;

    public abstract void read(String path) throws IOException;

    public abstract void read2(String path) throws IOException;

    public abstract void rename(String path,String ime) throws IOException;

    public abstract void containStr(String path, String string) throws IOException;

    public abstract void containFile(String path, String ime) throws IOException;

    public abstract void doesFolderContainFile(String path, List<String> imena) throws IOException;

    public abstract void sort(String path, String vrsta, String redosled) throws IOException;

    public abstract void moveFile(String sourceFilePath, String destinationFilePath) throws IOException;

    public abstract void lastModified(String path) throws IOException;

    public abstract boolean downloadFile(String sourceFilePath) throws IOException;

    public abstract boolean connect(String path) throws IOException;

    public abstract void konfiguracioniFajl(String path, Long velicina, String imeFoldera, Integer brojFajlova, String zabrana);

    public abstract void setStorageSize(Long velicina);

    public abstract void setRestrictedxtensions(String restrictions);

    public abstract void setFolderRestriction(String folderName, Integer broj);

    public abstract void returnFilesWithExtension(String path, String extension);

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
