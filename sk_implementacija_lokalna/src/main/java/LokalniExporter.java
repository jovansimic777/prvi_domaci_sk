import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.*;
import java.util.function.Consumer;

public class LokalniExporter extends MyExporter{

    static {
        ExporterManager.registerExporter(new LokalniExporter());
    }

    public JSONArray useri = new JSONArray();
    public String nazivSkladista = new String();
    public Long velicinaSkladista = new Long(0);
    public Long unetaVelicina;
    public String pathKonfiguracioni;
    public List<String> zabrane = new ArrayList<>();
    public String zabraneJson;
    JSONArray imeFoldera1 = new JSONArray();
    JSONArray brFajlova = new JSONArray();


    private LokalniExporter() {

    }

    @Override
    public String initStorage(String path) throws IOException {

        File dir = new File(path);
        if(!dir.exists()){

            if(dir.mkdirs()){

                String paths[] = path.split("\\\\");
                nazivSkladista = paths[paths.length-1];
                velicinaSkladista = FileUtils.sizeOfDirectory(dir);
                return "new";

            }else {
                return "false";
            }
        }else{

            File[] files = dir.listFiles();
            boolean flag = false;
            for(File file : files){
                if(file.getName().contains("json")){
                    flag = true;
                    break;
                }
            }
            if(flag == false){
                return "confalse";
            }else{

                String paths[] = path.split("\\\\");
                nazivSkladista = paths[paths.length-1];
                velicinaSkladista = FileUtils.sizeOfDirectory(dir);

                pathKonfiguracioni = path + "\\konfiguracija.json";

                File file = new File(path + "\\konfiguracija.json");
                JSONObject object = new JSONObject();
                JSONParser jsonP = new JSONParser();


                try(FileReader reader = new FileReader(file)) {
                    Object obj = jsonP.parse(reader);
                    object = (JSONObject) obj;

                    unetaVelicina = (Long) object.get("velicina");
                    String zaSplit = (String) object.get("zabrana");
                    String[] split = zaSplit.split(",");
                    zabraneJson = (String) object.get("zabrana");
                    for(int i=0; i< split.length; i++){

                        zabrane.add(split[i]);

                    }
                    imeFoldera1 = (JSONArray) object.get("imeFoldera");
                    brFajlova = (JSONArray) object.get("brFajlova");

                } catch (ParseException | FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "connect";
            }
        }
    }



    @Override
    public void save(String sourceFilePath, String destinationFilePath) {

        File sourceFile = new File(sourceFilePath);
        File destinationFile = new File(destinationFilePath);
        if(!destinationFilePath.contains(nazivSkladista)){
            System.out.println("Niste izabrali skladiste!");
            return;
        }

        try {

            boolean flag2 = true;
            readKonfiguracioni(pathKonfiguracioni);

            String ime = "";
            String[] zaSplit = destinationFilePath.split("\\\\");
            for(int j=0 ; j<zaSplit.length-1 ; j++){

                ime = ime + "\\" + zaSplit[j];

            }

            File ime1 = new File(ime);

            for(int i=0; i<imeFoldera1.size();i++){

                Long b = (Long) brFajlova.get(i);

                if(ime1.getAbsolutePath().equals((String) imeFoldera1.get(i))){
                    if(b < ime1.listFiles().length + 1)
                    {
                        flag2 = false;
                    }

                }

            }
            if(unetaVelicina >= velicinaSkladista){
                if(flag2){
                    Files.copy(sourceFile.toPath(),destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Uspesno sacuvan!");
                }else{
                    System.out.println("Premasili ste ogranicen broj fajlova!");
                }
            }else{
                System.out.println("Nema mesta na skladistu!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createFolder(String path) {

        File dir = new File(path);
        if(!dir.exists()){
            try{

                boolean flag2 = true;
                readKonfiguracioni(pathKonfiguracioni);


                String ime = "";
                String[] zaSplit = path.split("\\\\");
                for(int j=0 ; j<zaSplit.length-1 ; j++){

                    ime = ime + "\\" + zaSplit[j];

                }

                File ime1 = new File(ime);

                for(int i=0; i<imeFoldera1.size();i++){

                    Long b = (Long) brFajlova.get(i);

                    if(ime1.getAbsolutePath().equals((String) imeFoldera1.get(i))){
                        if(b < ime1.listFiles().length + 1)
                        {
                            flag2 = false;
                        }

                    }

                }

                if(unetaVelicina >= velicinaSkladista){

                    if(flag2){
                        if(dir.mkdirs()){
                            System.out.println("Uspesno");
                        }else {
                            System.out.println("Neuspesno");
                        }
                    }else{
                        System.out.println("Prekoracili ste broj fajlova");
                    }

                }else{
                    System.out.println("Nema mesta na skladistu!");
                }
            }catch (Exception e) {
                System.out.println("Pogresna putanja!");
            }

        }else{
            System.out.println("Folder vec postoji");
        }
    }



    @Override
    public void createFile(String path) {


        if(path.contains(nazivSkladista)){
            File file = new File(path);
            if(!file.exists()){
                try {
                    boolean flag = true;
                    boolean flag2 = true;
                    readKonfiguracioni(pathKonfiguracioni);
                    String ext1 = FilenameUtils.getExtension(path);
                    for(String ext: zabrane){


                        if(ext.equals(ext1)){
                            flag = false;
                        }

                    }
                    String ime = "";
                    String[] zaSplit = path.split("\\\\");
                    for(int j=0 ; j<zaSplit.length-1 ; j++){

                        ime = ime + "\\" + zaSplit[j];

                    }

                    File ime1 = new File(ime);

                    for(int i=0; i<imeFoldera1.size();i++){

                        Long b = (Long) brFajlova.get(i);

                        if(ime1.getAbsolutePath().equals((String) imeFoldera1.get(i))){
                            if(b < ime1.listFiles().length + 1)
                            {
                                flag2 = false;
                            }

                        }

                    }

                    if(unetaVelicina >= velicinaSkladista){
                        if(flag){
                            if(flag2){
                                if(file.createNewFile()){

                                    System.out.println("Uspesno kreiran file");
                                    System.out.println(FilenameUtils.getExtension(file.getName()));
                                }
                            }else{
                                System.out.println("Prekoracili ste broj fajlova");
                            }

                        }else{
                            System.out.println("Uneli ste zabranjenu ekstenziju");
                        }

                    }else{
                        System.out.println("Nema mesta na skladistu!");
                    }
                } catch (IOException e) {
                    System.out.println("Pogresna putanja!");
                }
            }else{
                System.out.println("Fajl vec postoji !");
            }
        }else{
            System.out.println("Izabrali ste putanju izvan skladista!");
        }

    }


    @Override
    public void deleteFolder(String path) {

        File file = new File(path);
        deleteFolder2(file);

    }

    private void deleteFolder2(File file){

        if(file.isDirectory()){

            if(file.list().length == 0){

                for(int i=0; i<imeFoldera1.size(); i++){

                    if(file.getAbsolutePath().equals(imeFoldera1.get(i))) {
                        imeFoldera1.remove(i);
                        brFajlova.remove(i);
                    }

                }


                File file2 = new File(pathKonfiguracioni);


                JSONObject object = new JSONObject();
                object.put("velicina",unetaVelicina);
                object.put("zabrana",zabraneJson);
                object.put("imeFoldera", imeFoldera1);
                object.put("brFajlova", brFajlova);

                try {
                    FileWriter writer = new FileWriter(file2,false);
                    writer.write(object.toJSONString());
                    writer.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }


                file.delete();
                System.out.println("Directory is deleted : " + file.getAbsolutePath());
            }else{

                File files[] = file.listFiles();

                for(File fileDelete:files){
                    deleteFolder2(fileDelete);
                }

                if(file.list().length == 0){

                    for(int i=0; i<imeFoldera1.size(); i++){

                        if(file.getAbsolutePath().equals(imeFoldera1.get(i))) {
                            imeFoldera1.remove(i);
                            brFajlova.remove(i);
                        }

                    }


                    File file2 = new File(pathKonfiguracioni);


                    JSONObject object = new JSONObject();
                    object.put("velicina",unetaVelicina);
                    object.put("zabrana",zabraneJson);
                    object.put("imeFoldera", imeFoldera1);
                    object.put("brFajlova", brFajlova);

                    try {
                        FileWriter writer = new FileWriter(file2,false);
                        writer.write(object.toJSONString());
                        writer.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    file.delete();
                    System.out.println("Directory is deleted : " + file.getAbsolutePath());
                }

            }


        }else{
            file.delete();
            System.out.println("File is deleted : " + file.getAbsolutePath());
        }


    }

    @Override
    public void deleteFile(String path) {

        File file = new File(path);
        if(file.delete()){
            System.out.println("Uspesno");
        }else{
            System.out.println("Neuspesno");
        }
    }

    @Override
    public void read(String path) {

        File folder = new File(path);
        File[] files = folder.listFiles();

        for(File file:files){
            if(file.isFile()){
                System.out.println("File -> " + file.getName());
            }else{
                if(file.isDirectory()){
                    System.out.println("Folder-> "+ file.getName());
                    File[] files1 = file.listFiles();
                    for(File file1: files1){
                        if(file1.isFile()){
                            System.out.println("File -> " + file1.getName());
                        }else{
                            if(file1.isDirectory()){
                                System.out.println("Folder-> "+ file1.getName());
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void read2(String path) throws IOException {
        File folder = new File(path);
        File[] files = folder.listFiles();

        for(File file:files){

            if(file.isFile()){
                System.out.println("File -> " + file.getName());
            }else{
                if(file.isDirectory()){
                    System.out.println("Folder-> "+ file.getName());
                }
            }
        }
    }

    @Override
    public void rename(String path, String ime) throws IOException {

        File folder = new File(path);
        String[] ulaz = path.split("\\\\");
        String novi = "";
        for(int i = 0; i < ulaz.length-1;i++){
            novi += ulaz[i];
            novi += "\\";
        }

        novi += ime;
        System.out.println(novi);
        File noviFajl = new File(novi);
        folder.renameTo(noviFajl);
    }

    @Override
    public void containStr(String path, String string) throws IOException {
        File folder = new File(path);
        File[] files = folder.listFiles();

        for(File file:files){
            if(file.isFile() && file.getName().contains(string)){
                System.out.println("File -> " + file.getName());
            }else{
                if(file.isDirectory()){
                    File[] files1 = file.listFiles();
                    for(File file1: files1){
                        if(file1.isFile() && file1.getName().contains(string)){
                            System.out.println("File -> " + file1.getName());
                        }
                    }
                }
            }
        }
    }

    @Override
    public void containFile(String path, String ime) throws IOException {
        File folder = new File(path);
        File[] files = folder.listFiles();

        for(File file:files){
            if(file.isDirectory()) {
                File[] files1 = file.listFiles();
                for (File file1 : files1) {
                    if (file1.isFile() && file1.getName().contains(ime)) {
                            System.out.println("Folder -> " + file.getName());
                    }else{
                        if(file1.isDirectory()){
                            File[] files2 = file1.listFiles();
                            for(File file2 : files2){
                                if(file2.isFile() && file2.getName().contains(ime)){
                                    System.out.println("Folder -> " + file1.getName());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void doesFolderContainFile(String path, List<String> imena) {
        File folder = new File(path);
        File[] files = folder.listFiles();

        for(File file:files){
            if(file.isFile()){
                for(String ime : imena){
                    if(file.getName().equals(ime)){
                        System.out.println("File -> " + file.getName() + " se sadrzi;");
                    }
                }
            }else{
                if(file.isDirectory()){
                    File[] files1 = file.listFiles();
                    for(File file1: files1){
                        if(file1.isFile()){
                            for(String ime1: imena){
                                if(file1.getName().equals(ime1)){
                                    System.out.println("File -> " + file1.getName() + " se sadrzi");
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void sort(String path, String vrsta, String redosled) throws IOException {
        File folder = new File(path);
        String p = path;
        File[] files = folder.listFiles();
        List<String> files1 = new ArrayList<>();

        for(File f: files) {
            files1.add(f.getName());
        }

        if(vrsta.equals("ime")){
            if(redosled.equals("true")){
                 Collections.sort(files1);
            }else{
                if(redosled.equals("false")) {
                    Collections.sort(files1, Collections.reverseOrder());
                }
            }
            System.out.println(files1);
        }
        List<FileTime> vremenaK = new ArrayList<>();
        for(File f:files) {
            BasicFileAttributes attr = Files.readAttributes(Path.of(f.getPath()), BasicFileAttributes.class);
            FileTime a = attr.creationTime();
            vremenaK.add(a);
        }
        if(vrsta.equals("datumK")){
            if(redosled.equals("true")){

            Collections.sort(vremenaK);
            }else{
                if(redosled.equals("false")){
                    Collections.sort(vremenaK,Collections.reverseOrder());
                }
            }
            System.out.println(vremenaK);
        }
        List<FileTime> vremenaM = new ArrayList<>();
        for(File f:files) {
            BasicFileAttributes attr = Files.readAttributes(Path.of(f.getPath()), BasicFileAttributes.class);
            FileTime a = attr.lastModifiedTime();
            vremenaM.add(a);
        }

        if(vrsta.equals("datumM")){
            if(redosled.equals("true")){

                Collections.sort(vremenaM);
            }else{
                if(redosled.equals("false")){
                    Collections.sort(vremenaK,Collections.reverseOrder());
                }
            }
            System.out.println(vremenaM);
        }
    }
    @Override
    public void moveFile(String sourceFilePath , String destinationFilePath) {

        File sourceFile = new File(sourceFilePath);
        File destinationFile = new File(destinationFilePath);
        if(!sourceFilePath.contains(nazivSkladista) || !destinationFilePath.contains(nazivSkladista)){
            System.out.println("Morate izabrati fajlove unutar skladista!");
            return;
        }
        try {

            boolean flag2 = true;
            String ime = "";
            String[] zaSplit = destinationFilePath.split("\\\\");
            for(int j=0 ; j<zaSplit.length-1 ; j++){

                ime = ime + "\\" + zaSplit[j];

            }

            File ime1 = new File(ime);

            for(int i=0; i<imeFoldera1.size();i++){

                Long b = (Long) brFajlova.get(i);

                if(ime1.getAbsolutePath().equals((String) imeFoldera1.get(i))){
                    if(b < ime1.listFiles().length + 1)
                    {
                        flag2 = false;
                    }

                }

            }
            if(flag2){

                Files.copy(sourceFile.toPath(),destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                if(sourceFile.isDirectory()){
                    deleteFolder(sourceFilePath);
                }else{
                    deleteFile(sourceFilePath);
                }
                System.out.println("Uspesno kopiran!");

            }else{
                System.out.println("Premasili ste ogranicen broj fajlova!");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void lastModified(String path) throws IOException {

        Path p = Paths.get(path);
        BasicFileAttributes attr = Files.readAttributes(p, BasicFileAttributes.class);
        FileTime a = attr.lastModifiedTime();
        System.out.println("Last modified: " + a);
        FileTime b = attr.creationTime();
        System.out.println("Created: " + b);

    }

    @Override
    public boolean downloadFile(String sourceFilePath) {

        File sourceFile = new File(sourceFilePath);
        File destinationFile = new File("C:\\Users\\Micole\\Downloads");
        try {
            Files.copy(sourceFile.toPath(),destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }
    @Override
    public boolean connect(String path) throws IOException {
        return true;
    }
    @Override
    public void konfiguracioniFajl(String path, Long velicina,String imeFoldera, Integer brojFajlova , String zabrana) {

        File file = new File(path + "\\konfiguracija.json");
        pathKonfiguracioni = path + "\\konfiguracija.json";

        unetaVelicina = velicina;
        zabraneJson = zabrana;
        imeFoldera1.add(imeFoldera);
        brFajlova.add(brojFajlova);

        JSONObject object = new JSONObject();
        object.put("velicina",unetaVelicina);
        object.put("zabrana",zabraneJson);
        object.put("imeFoldera", imeFoldera1);
        object.put("brFajlova", brFajlova);

        try {
            FileWriter writer = new FileWriter(file,false);
            writer.write(object.toJSONString());
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setStorageSize(Long velicina) {

        File file = new File(pathKonfiguracioni);

        unetaVelicina = velicina;

        JSONObject object = new JSONObject();
        object.put("velicina",unetaVelicina);
        object.put("zabrana",zabraneJson);
        object.put("imeFoldera", imeFoldera1);
        object.put("brFajlova", brFajlova);

        try {
            FileWriter writer = new FileWriter(file,false);
            writer.write(object.toJSONString());
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setRestrictedxtensions(String restrictions) {

        File file = new File(pathKonfiguracioni);

        zabraneJson = restrictions;

        JSONObject object = new JSONObject();
        object.put("velicina",unetaVelicina);
        object.put("zabrana",zabraneJson);
        object.put("imeFoldera", imeFoldera1);
        object.put("brFajlova", brFajlova);

        try {
            FileWriter writer = new FileWriter(file,false);
            writer.write(object.toJSONString());
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setFolderRestriction(String folderName,Integer broj) {

        File file = new File(pathKonfiguracioni);

        imeFoldera1.add(folderName);
        brFajlova.add(broj);

        JSONObject object = new JSONObject();
        object.put("velicina",unetaVelicina);
        object.put("zabrana",zabraneJson);
        object.put("imeFoldera", imeFoldera1);
        object.put("brFajlova", brFajlova);

        try {
            FileWriter writer = new FileWriter(file,false);
            writer.write(object.toJSONString());
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void returnFilesWithExtension(String path, String extension) {
        File folder = new File(path);
        File[] files = folder.listFiles();

        for(File file:files){
            if(file.isFile() && FilenameUtils.getExtension(file.getName()).equals(extension)){
                System.out.println("File -> " + file.getName());
            }else{
                if(file.isDirectory()){
                    File[] files1 = file.listFiles();
                    for(File file1: files1){
                        if(file1.isFile() && FilenameUtils.getExtension(file1.getName()).equals(extension)){
                            System.out.println("File -> " + file1.getName());
                        }
                    }
                }
            }
        }
    }

    public void readKonfiguracioni(String path) {

        File file = new File(pathKonfiguracioni);
        JSONObject object = new JSONObject();
        JSONParser jsonP = new JSONParser();



        try(FileReader reader = new FileReader(file)) {
            Object obj = jsonP.parse(reader);
            object = (JSONObject) obj;
            unetaVelicina = (Long) object.get("velicina");
            String zaSplit = (String) object.get("zabrana");
            String[] split = zaSplit.split(",");
            for(int i=0; i< split.length; i++){

                zabrane.add(split[i]);

            }
            imeFoldera1 = (JSONArray) object.get("imeFoldera");
            brFajlova = (JSONArray) object.get("brFajlova");


        } catch (ParseException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}