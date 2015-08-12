package org.wso2.training.tools.jar.analyser;

import org.apache.commons.io.FilenameUtils;
import org.osgi.framework.Constants;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;


/**
 * Created by rajith on 6/30/15.
 */
public class MetaInfoExtractor {

    public static void main(String[] args){

        //This location is jar folder to be analysed
        String jarFolderToBeAnalysed = "/media/rajith/Office/Wso2/dev/git/carbon-datadd";
        //This location is used to copy those jar files and create human readable manifest files
        String tempFolder = "/media/rajith/Office/mytest/MetaInfErrorFinder/src/main/resources/temp1/";

        FileUtil.copyTargetFolderJarsToDest(jarFolderToBeAnalysed, tempFolder);
        MetaInfoExtractor.manifestChecker(tempFolder);
    }

    public static void manifestChecker(String copyFolder){
        File folder = new File(copyFolder);
        File[] listOfFiles = folder.listFiles();


        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                String fileName = listOfFiles[i].getName();
                if (fileName.endsWith(".jar")){
                    String basename = FilenameUtils.getBaseName(fileName);
                    try {
                        MetaInfoExtractor.readManifest(copyFolder, basename);
                        System.out.println("Jar file read - "+fileName);
                    } catch (Exception e) {
                        System.out.println("error in reading file - "+fileName);
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void readManifest(String folderPath,String jarName){
        try {

            String filename = jarName;

            JarFile jarFile = new JarFile(folderPath+filename+".jar");
            Manifest manifest = jarFile.getManifest();
            Attributes mainAttributes = manifest.getMainAttributes();
//            String bundleVersion = (String) mainAttributes.get(Constants.BUNDLE_VERSION);
            String exportPackString = (String) mainAttributes.getValue(Constants.EXPORT_PACKAGE);
            String importPackString = (String) mainAttributes.getValue(Constants.IMPORT_PACKAGE);

            PrintWriter writer = new PrintWriter(folderPath+filename+".txt", "UTF-8");


            if (exportPackString != null && !exportPackString.isEmpty()) {
                String[] exportPackages = exportPackString.split("\",");
                writer.println("Export packages");
                writer.println();
                for (int i = 0;i<exportPackages.length;i++){
                    String[] usess = exportPackages[i].split("uses:=\"");
                    writer.println(usess[0]);
                    if (usess.length>1) {
                        String[] usedd = usess[1].split(",");
                        writer.println("uses  : "+usedd[0]);
                        for (int j = 1;j<usedd.length;j++){
                            writer.println("        "+usedd[j]);
                        }
                    }
                    writer.println();
                }
            }
            writer.println();
            writer.println();


            if (importPackString != null && !importPackString.isEmpty()) {
                String[] importPackages = importPackString.split("\",");
                writer.println("Import packages");
                writer.println();
                for (int i = 0;i<importPackages.length;i++){
                    writer.println(importPackages[i]+"\"");
                }
            }
            writer.println();
            writer.println();
            writer.println();
            writer.println();
            writer.println();
            writer.println();
            writer.println();
            writer.println();
            Iterator it = mainAttributes.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                writer.println(pair.getKey() + " : " + pair.getValue());
                it.remove(); // avoids a ConcurrentModificationException
            }
            writer.close();

            int pp = 9;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
