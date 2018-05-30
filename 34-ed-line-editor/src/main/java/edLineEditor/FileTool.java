package edLineEditor;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class FileTool {
    public static ArrayList<String> readFile(String filename){
        File file = new File(filename);
        ArrayList<String> list = new ArrayList<>();
        try {
            Scanner in = new Scanner(file);
            while (in.hasNextLine()){
                list.add(in.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void printLines(int beginIndex, int endIndex, Page page){
        beginIndex--;
        endIndex--;
        for (int i = beginIndex; i <= endIndex; i++){
            System.out.println(page.currPage.get(i));
        }
    }

    public static void saveFile(int beginIndex, int endIndex, Page page, String fileName, boolean isAdd){
        beginIndex--;
        endIndex--;
        File file = new File(fileName);
        FileWriter fw;
        try {
            fw = new FileWriter(file, isAdd);
            for (int i = beginIndex; i <= endIndex; i++){
                try {
                    fw.write(page.currPage.get(i) + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        page.isSaved = true;
    }
}
