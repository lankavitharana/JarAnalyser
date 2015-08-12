package org.wso2.training.tools.jar.analyser;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by rajith on 8/12/15.
 */
public class JarFileExtractor {
    public static void main(String[] args) throws IOException {

        //this variable is jar file folder to be analysed
        String jarFolderToBeAnalysed = "/media/rajith/Office/Wso2/support/DHSDEV-1/wso2dss-3.2.2/";
        //this is the temp directory where jar files will be copied and package data will be written to "<jar_name>.Packages.txt" file
        String tempFolder = "/media/rajith/Office/mytest/MetaInfErrorFinder/src/main/resources/temp1/";


        //this is used to copy all the jar files inside "jarFolderToBeAnalysed" to the temp folder
        FileUtil.copyTargetFolderJarsToDest(jarFolderToBeAnalysed, tempFolder);

        jarAnalyser(tempFolder);

    }

    private static void jarAnalyser(String tempFolder) throws IOException {
        File folder = new File(tempFolder);
        File[] listOfFiles = folder.listFiles();



        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                String fileName = listOfFiles[i].getName();
                if (fileName.endsWith(".jar")){
                    String basename = FilenameUtils.getBaseName(fileName);
                    int p = 0;

                    PrintWriter writer = new PrintWriter(tempFolder + File.separator + basename +".Packages.txt", "UTF-8");

                    JarFile jar = new JarFile(listOfFiles[i]);
                    java.util.Enumeration enumEntries = jar.entries();
                    while (enumEntries.hasMoreElements()) {
                        JarEntry file = (JarEntry) enumEntries.nextElement();
                        String filePath = file.getName();
                        File f = new File(tempFolder + File.separator + basename + File.separator + filePath);
                        if (filePath.endsWith(".class")){
                            String packageName = filePath.replace('/','.');
                            writer.println(packageName);
                        }
                    }
                    writer.close();
                }
            }
        }
    }
}
