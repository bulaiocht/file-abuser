/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.nashorn.internal.ir.Symbol;

/**
 *
 * @author Carcass
 */
public class ListingFilesToTxt {

    static long fileSizeTrashhold = 1024 * 1024;
    static String[] fileExtensions = {".jpg", ".png", ".bmp", ".gif", ".psd"};
    static String dirName = "j:/Рисунки/Мои сочинения/";
    static String outPutFileToPrint = "j:/Рисунки/Мои сочинения/Список выбранных файлов.doc";

    public static void main(String[] args) throws FileNotFoundException, IOException {
//        trial();
        
        File targetDir = new File(dirName);
        if (targetDir.isDirectory()) {
            File[] listOfBigPics = gatherFiles(targetDir);
            print(listOfBigPics);
        } else {
            File[] errorMsg = new File[]{new File("The directory '" + targetDir.getAbsolutePath() + "' doesn't exist")};
            print(errorMsg);
        }
    }

    /** 
     * This method is separated so you could edit the strings of the initial messages here
     * @param dataOut
     * @throws IOException 
     */
    private static void printInitialLines(OutputStreamWriter dataOut) throws IOException {

        dataOut.write("Selected directory: " + dirName + "\n");
        dataOut.write("The minimum file size trashhold is " + fileSizeTrashhold + " bytes\n");
        dataOut.write("The selected extensions are: ");
        for (String ext : fileExtensions) {
            dataOut.write(ext+"  ");
        }
        dataOut.write("\n\nList of files: \n");
    }
    
    private static File[] gatherFiles(File dir) {

        File[] result = new File[0];
        File[] contents = dir.listFiles();
        int neededContentsLength = 0;
        for (File content : contents) {
            if (content.isFile()) {
                if (fileFilter(content)) {
                    contents[neededContentsLength++] = content;
                }
            } else {
                result = concatinate(result, gatherFiles(content));
            }
        }
        contents = cutNulls(contents, neededContentsLength);
        result = concatinate(result, contents);
        return result;
    }

    public static boolean fileFilter(File file) {
        boolean result = false;

        for (String value : fileExtensions) {
            result |= file.getName().endsWith(value);
        }
        result &= file.length() > fileSizeTrashhold;

        return result;
    }

    public static File[] concatinate(File[] file0, File[] file1) {
        File[] concatinated = new File[file0.length + file1.length];
        System.arraycopy(file0, 0, concatinated, 0, file0.length);
        System.arraycopy(file1, 0, concatinated, file0.length, file1.length);
        return concatinated;
    }

    public static File[] cutNulls(File[] full, int filledLength) {
        File[] cut = new File[filledLength];
        System.arraycopy(full, 0, cut, 0, filledLength);
        return cut;
    }

    public static void print(File[] listToPrint) throws FileNotFoundException, IOException {
        
        OutputStreamWriter dataOut = makeWriter(listToPrint);
        printInitialLines(dataOut);
        for (File file : listToPrint) {
            if (file != null) {
                dataOut.write(file.getAbsolutePath()+"\n");
            } else {
                dataOut.write("null\n");
            }
        }
        dataOut.flush();
        dataOut.close();
    }
    
    public static OutputStreamWriter makeWriter (File[] fileArrayToWorkOn) throws FileNotFoundException {
        BufferedOutputStream outputStream
                = new BufferedOutputStream(
                        new FileOutputStream(
                                new File(outPutFileToPrint)));
        return new OutputStreamWriter(outputStream);
    }

    //<editor-fold defaultstate="collapsed" desc="some trial method, don't mind it">
    private static void trial() throws IOException {
        File[] emptyArr1 = new File[0];
        System.out.println("Empty array #1");
        print(emptyArr1);
        File[] emptyArr2 = new File[0];
        System.out.println("Empty array #2");
        print(emptyArr1);
        File[] concatinatedEmptyArrs = concatinate(emptyArr2, emptyArr2);
        System.out.println("Empty #1 + empty #2");
        print(concatinatedEmptyArrs);
        File[] containingArr = {new File("azaza0"), new File("azaza1"), new File("azaza2")};
        System.out.println("Containing array"
                + "");
        print(containingArr);
        File[] emptyPlusContaining = concatinate(concatinatedEmptyArrs, containingArr);
        System.out.println("Empty + containing");
        print(emptyPlusContaining);
        File[] containingPlusEmpty = concatinate(containingArr, concatinatedEmptyArrs);
        System.out.println("Containing + emoty");
        print(emptyPlusContaining);
    }
//</editor-fold>

}
