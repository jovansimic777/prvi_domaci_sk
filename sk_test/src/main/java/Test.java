import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) throws ClassNotFoundException, IOException {
        Class.forName("LokalniExporter");
        MyExporter myExporter = ExporterManager.getExporter("");
        Scanner scanner = new Scanner(System.in);


        String path = "";
        try {
            while(true) {

                System.out.println("Unesite putanju: ");
                path = scanner.nextLine();

                if (myExporter.initStorage(path).equals("new")) {

                    System.out.println("Uspesno!");

                    Scanner scanner30 = new Scanner(System.in);

                    System.out.println("Unesite velicinu skladista: ");
                    Long velicina = Long.valueOf(scanner30.nextLine());

                    System.out.println("Unesite ime foldera:");
                    String imeFoldera = scanner30.nextLine();

                    System.out.println("Unesite maksimalan broj fajlova:");
                    Integer brojFajlova = Integer.valueOf(scanner30.nextLine());

                    System.out.println("Unesite zabranjene ekstenzije fajlova:");
                    String zabrana = scanner30.nextLine();

                    myExporter.konfiguracioniFajl(path,velicina,imeFoldera,brojFajlova,zabrana);


                    System.out.println("Neuspesno konektovan!");

                }
                if(myExporter.initStorage(path).equals("connect")){

                    System.out.println("Vec postoji!");
                    System.out.println("Ovo je skladiste");
                    if(myExporter.connect(path)){
                        System.out.println("Uspesno konektovan!");
                        break;
                    }


                }
                if(myExporter.initStorage(path).equals("false")){
                    System.out.println("Neuspesno!");
                }
                if(myExporter.initStorage(path).equals("confalse")){
                    System.out.println("Vec postoji!");
                    System.out.println("Ovo ne moze da bude skladiste!");
                }
            }

            while(true){

                    System.out.println("(1) Save");
                    System.out.println("(2) Create folder");
                    System.out.println("(3) Create file");
                    System.out.println("(4) Delete folder");
                    System.out.println("(5) Delete file");
                    System.out.println("(6) ReadAll");
                    System.out.println("(7) Move file");
                    System.out.println("(8) Download file");
                    System.out.println("(9) Last modified");
                    System.out.println("(10) Set storage size");
                    System.out.println("(11) Set restricted extensions");
                    System.out.println("(12) Set folder restriction");
                    System.out.println("(13) Rename");
                    System.out.println("(14) Read");
                    System.out.println("(15) File contains String");
                    System.out.println("(16) Which folder contains file");
                    System.out.println("(17) Does folder contain file");
                    System.out.println("(18) Get file with extension");
                    System.out.println("(19) Sort");


                System.out.println("Izaberite opciju:");
                    Scanner scanner100 = new Scanner(System.in);
                    int br = scanner100.nextInt();

                switch(br) {

                    case 1:

                        Scanner scanner11 = new Scanner(System.in);
                        System.out.println("Unesite putanju koji fajl zelite da sacuvate:");
                        String source11 = scanner11.nextLine();
                        System.out.println("Unesite putanju gde u skladistu zelite da sacuvate fajl:");
                        String destination11 = scanner11.nextLine();
                        myExporter.save(source11, destination11);
                        break;
                    case 2:
                        Scanner scanner2 = new Scanner(System.in);
                        System.out.println("Unesite putanju gde zelite da kreirate folder:");
                        String path2 = scanner2.nextLine();
                        myExporter.createFolder(path2);
                        break;
                    case 3:
                        Scanner scanner3 = new Scanner(System.in);
                        System.out.println("Unesite putanju gde zelite da kreirate fajl:");
                        String path3 = scanner3.nextLine();
                        myExporter.createFile(path3);
                        break;
                    case 4:
                        Scanner scanner4 = new Scanner(System.in);
                        System.out.println("Unesite koji folder zelite da obrisete:");
                        String path4 = scanner4.nextLine();
                        myExporter.deleteFolder(path4);
                        break;
                    case 5:
                        Scanner scanner5 = new Scanner(System.in);
                        System.out.println("Unesite koji file zelite da obrisete:");
                        String path5 = scanner5.nextLine();
                        myExporter.deleteFile(path5);
                        break;
                    case 6:
                        Scanner scanner6 = new Scanner(System.in);
                        System.out.println("Unesite koji folder zelite da izlistate:");
                        String path6 = scanner6.nextLine();
                        myExporter.read(path6);
                        break;
                    case 7:
                        Scanner scanner7 = new Scanner(System.in);
                        System.out.println("Unesite koji fajl zelite da premestite:");
                        String source2 = scanner7.nextLine();
                        System.out.println("Unesite gde zelite da premestite fajl:");
                        String destination2 = scanner7.nextLine();
                        myExporter.moveFile(source2, destination2);
                        break;
                    case 8:
                        Scanner scanner8 = new Scanner(System.in);
                        System.out.println("Unesite koji fajl zelite da skinete:");
                        String source = scanner8.nextLine();

                        if (myExporter.downloadFile(source)) {
                            System.out.println("Uspesno skinut!");
                        } else {
                            System.out.println("Neuspesno!");
                        }
                        break;
                    case 9:
                        System.out.println("Unesite koji folder zelite da fidite kad je poslednji put modifikovan: ");
                        Scanner scanner21 = new Scanner(System.in);
                        String path21 = scanner21.nextLine();
                        myExporter.lastModified(path21);
                        break;
                    case 10:

                        System.out.println("Unesite velicinu storage-a:");
                        Scanner scanner40 = new Scanner(System.in);
                        Long vel = Long.parseLong(scanner40.nextLine());
                        myExporter.setStorageSize(vel);

                        break;
                    case 11:

                        System.out.println("Unesite zabranjene extenzije:");
                        Scanner scanner50 = new Scanner(System.in);
                        String res = scanner50.nextLine();
                        myExporter.setRestrictedxtensions(res);

                        break;
                    case 12:

                        System.out.println("Unesite putanju foldera:");
                        Scanner scanner60 = new Scanner(System.in);
                        String fol = scanner60.nextLine();
                        System.out.println("Unesite broj foldera:");
                        Integer b = Integer.parseInt(scanner60.nextLine());
                        myExporter.setFolderRestriction(fol, b);

                        break;
                    case 13:
                        System.out.println("Unesite putanju foldera:");
                        Scanner scanner25132 = new Scanner(System.in);
                        String put = scanner25132.nextLine();
                        System.out.println("Unesite novo ime");
                        String ime = scanner25132.nextLine();
                        myExporter.rename(put, ime);

                        break;
                    case 14:
                        Scanner scanner643 = new Scanner(System.in);
                        System.out.println("Unesite koji folder zelite da izlistate:");
                        String path67 = scanner643.nextLine();
                        myExporter.read2(path67);
                        break;
                    case 15:
                        System.out.println("Unesite putanju foldera:");
                        Scanner scanner257132 = new Scanner(System.in);
                        String put1 = scanner257132.nextLine();
                        System.out.println("Unesite string");
                        String ime1 = scanner257132.nextLine();
                        myExporter.containStr(put1, ime1);
                        break;
                    case 16:
                        System.out.println("Unesite putanju foldera:");
                        Scanner scannerr = new Scanner(System.in);
                        String put12 = scannerr.nextLine();
                        System.out.println("Unesite ime fajla");
                        String ime12 = scannerr.nextLine();
                        myExporter.containFile(put12, ime12);
                        break;
                    case 17:
                        System.out.println("Unesite putanju foldera:");
                        Scanner scanneri = new Scanner(System.in);
                        String put123 = scanneri.nextLine();
                        System.out.println("Unesite imena");
                        String imena = scanneri.nextLine();
                        String[] imena2 = imena.split(",");
                        List<String> finalnaImena = new ArrayList<String>();
                        for (int i = 0; i < imena2.length; i++){
                            finalnaImena.add(imena2[i]);
                        }
                        myExporter.doesFolderContainFile(put123,finalnaImena);
                        break;
                    case 18:
                        System.out.println("Unesite putanju foldera:");
                        Scanner scanner43 = new Scanner(System.in);
                        String put132 = scanner43.nextLine();
                        System.out.println("Unesite ekstenziju");
                        String ime132 = scanner43.nextLine();
                        myExporter.returnFilesWithExtension(put132, ime132);
                        break;
                    case 19:
                        System.out.println("Unesite putanju foldera:");
                        Scanner scanner143 = new Scanner(System.in);
                        String put0 = scanner143.nextLine();
                        System.out.println("Unesite vrstu sort");
                        String ime0 = scanner143.nextLine();
                        System.out.println("Unesite redosled sort");
                        String ime01 = scanner143.nextLine();
                        myExporter.sort(put0, ime0,ime01);
                        break;
                    default:
                        System.out.println("Lose ste uneli!");
                        break;
                }
                if(br == 0){
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
