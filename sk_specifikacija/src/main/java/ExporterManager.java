public class ExporterManager {

    private static MyExporter myExporter;

    public ExporterManager() {
    }

    public static void registerExporter(MyExporter var0) {
        myExporter = var0;
    }

    public static MyExporter getExporter(String var0) {
        myExporter.setFileName(var0);
        return myExporter;
    }
}