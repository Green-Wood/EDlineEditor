package edLineEditor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Editor {
    private int beginIndex;
    private int endIndex;
    Page page;
    Editor(Page page){
        this.page = page;
    }
    void setLines(int beginIndex, int endIndex){
        this.beginIndex = beginIndex - 1;
        this.endIndex = endIndex - 1;
    }

    int getBeginIndex(){
        return beginIndex;
    }

    int getEndIndex(){
        return endIndex;
    }

    void move(int toIndex){
        page.saveCurrent();
        if (toIndex > endIndex) toIndex = toIndex - (endIndex - beginIndex + 1) - 1;
        else toIndex--;
        ArrayList<String> l = new ArrayList<>();
        for (int i = beginIndex; i <= endIndex; i++){
            l.add(page.currPage.get(beginIndex));
            page.currPage.remove(beginIndex);
        }
        for (String s: l){
            toIndex++;
            page.currPage.add(toIndex, s);
        }
        page.setCurrLine(toIndex + 1);
        page.isSaved = false;
    }

    void copy(int toIndex){
        page.saveCurrent();
        toIndex--;
        ArrayList<String> l = new ArrayList<>();
        for (int i = beginIndex; i <= endIndex; i++){
            l.add(page.currPage.get(i));
        }
        for (String s: l){
            toIndex++;
            page.currPage.add(toIndex, s);
        }
        page.setCurrLine(toIndex + 1);
        page.isSaved = false;
    }

    void union(){
        page.saveCurrent();
        String str = page.currPage.get(beginIndex);
        for (int i = beginIndex + 1; i <= endIndex; i++){
            str += page.currPage.get(beginIndex + 1);
            page.currPage.remove(beginIndex + 1);
        }
        page.currPage.remove(beginIndex);
        page.currPage.add(beginIndex, str);
        page.isSaved = false;
    }

    boolean replace(String Old, String New, int count){
        page.saveCurrent();
        for (int i = beginIndex; i <= endIndex; i++){
            String line = page.currPage.get(i);
            int from = -1;
            for (int j = 0; j < count; j++){
                from = line.indexOf(Old, from + 1);          // 先检查一遍
                if (from == -1) return false;
            }
        }
        for (int i = beginIndex; i <= endIndex; i++){
            String line = page.currPage.get(i);
            int from = -1;
            for (int j = 0; j < count; j++){
                from = line.indexOf(Old, from + 1);
            }
            String newLine = line.substring(0, from) + New +
                    line.substring(from + Old.length(), line.length());
            page.currPage.remove(i);
            page.currPage.add(i, newLine);
        }
        page.setCurrLine(endIndex + 1);
        page.isSaved = false;
        return true;
    }

    boolean replace(String Old, String New){
        page.saveCurrent();
        for (int i = beginIndex; i <= endIndex; i++){   // 先检查一遍
            if (!page.currPage.get(i).contains(Old)) return false;
        }
        for (int i = beginIndex; i <= endIndex; i++){
            String line = page.currPage.get(i);
            String newLine = line.replace(Old, New);
            page.currPage.remove(i);
            page.currPage.add(i, newLine);
        }
        page.setCurrLine(endIndex + 1);
        page.isSaved = false;
        return true;
    }

    void printLines(){
        for (int i = beginIndex; i <= endIndex; i++){
            System.out.println(page.currPage.get(i));
        }
    }

    void saveFile(String fileName, boolean isAdd){
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
    public static void main(String[] args){

    }
}
