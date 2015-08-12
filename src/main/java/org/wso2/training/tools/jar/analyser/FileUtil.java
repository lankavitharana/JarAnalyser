package org.wso2.training.tools.jar.analyser;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by rajith on 8/12/15.
 */
public class FileUtil {

    public static void copyTargetFolderJarsToDest(String targetFolder,String destinaionFolder){
        File folder = new File(targetFolder);
        FileUtil.displayIt(folder, destinaionFolder);
    }

    private static void displayIt(File node ,String destination){

        if (node.isFile()){
            String name = node.getName();
            if (name.endsWith(".jar") || name.endsWith(".zip")){
                try {
                    FileUtils.copyFile(node, new File(destination + name));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if(node.isDirectory()){
            String[] subNote = node.list();
            for(String filename : subNote){
                displayIt(new File(node, filename),destination);
            }
        }

    }
}
