package edLineEditor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

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
        return beginIndex + 1;
    }

    int getEndIndex(){
        return endIndex + 1;
    }

    void move(int toIndex){
        page.saveCurrent();
        keepMark(toIndex, true);
        if (toIndex > endIndex) toIndex = toIndex - (endIndex - beginIndex + 1);
        ArrayList<String> l = new ArrayList<>();
        for (int i = beginIndex; i <= endIndex; i++){
            l.add(page.currPage.get(beginIndex));
            page.currPage.remove(beginIndex);
        }
        for (String s: l){
            page.currPage.add(toIndex, s);
            toIndex++;
        }
        page.setCurrLine(toIndex);
        page.isSaved = false;
    }

    void copy(int toIndex){
        page.saveCurrent();
        keepMark(toIndex, false);
        ArrayList<String> l = new ArrayList<>();
        for (int i = beginIndex; i <= endIndex; i++){
            l.add(page.currPage.get(i));
        }
        for (String s: l){
            page.currPage.add(toIndex, s);
            toIndex++;
        }
        page.setCurrLine(toIndex);
        page.isSaved = false;
    }

    void union(){
        page.saveCurrent();
        for (Map.Entry<Character, Integer> entry: page.getMarkedEntry()){         // 调整符号
            char c = entry.getKey();
            int lineNumber = entry.getValue() - 1;
            if (lineNumber > beginIndex && lineNumber <= endIndex) {
                page.deleteMark(c);
            }
            else if (lineNumber > endIndex){
                lineNumber = lineNumber - (endIndex - beginIndex + 1);
                page.setMark(c, lineNumber + 1);
            }
        }
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
        for (Map.Entry<Character, Integer> entry: page.getMarkedEntry()){         // 调整符号
            char c = entry.getKey();
            int lineNumber = entry.getValue() - 1;
            if (lineNumber >= beginIndex && lineNumber <= endIndex) {
                page.deleteMark(c);
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

    boolean replace(String Old, String New){               // 替换全部
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

    void printLines(){               // 打印指定行
        for (int i = beginIndex; i <= endIndex; i++){
            System.out.println(page.currPage.get(i));
        }
    }

    void saveFile(String fileName, boolean isAdd){               // 保存指定行
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

    private void keepMark(int toIndex, boolean isMove){               // 调整符号
        int originToIndex = toIndex;
        if (toIndex > endIndex && isMove) toIndex = toIndex - (endIndex - beginIndex + 1);
        for (Map.Entry<Character, Integer> entry: page.getMarkedEntry()){
            char c = entry.getKey();
            int lineNumber = entry.getValue() - 1;
            if (lineNumber <= endIndex && lineNumber >= beginIndex){
                lineNumber = toIndex + (lineNumber - beginIndex);            // 以零开始
                page.setMark(c, lineNumber + 1);
            }
            if (isMove && lineNumber > endIndex && originToIndex > lineNumber){     // 从上往下剪切
                lineNumber = lineNumber - (endIndex - beginIndex + 1);
                page.setMark(c, lineNumber + 1);
            }
            if (isMove && lineNumber < beginIndex && originToIndex <= lineNumber){   // 从下往上剪切
               lineNumber = lineNumber + (endIndex - beginIndex + 1);
               page.setMark(c, lineNumber + 1);
            }
            if (toIndex <= lineNumber && !isMove){
                lineNumber = lineNumber + (endIndex - beginIndex + 1);
                page.setMark(c, lineNumber + 1);
            }
        }
    }

    public static void main(String[] args){

    }
}
