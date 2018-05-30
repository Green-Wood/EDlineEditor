package edLineEditor;

import java.util.ArrayList;

public class Editor {
    private int beginIndex;
    private int endIndex;
    private Page page;
    Editor(int beginIndex, int endIndex, Page page){
        this.beginIndex = beginIndex - 1;
        this.endIndex = endIndex - 1;
        this.page = page;
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
        page.currLine = toIndex + 1;
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
        page.currLine = toIndex + 1;
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
        page.currLine = endIndex + 1;
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
        page.currLine = endIndex + 1;
        page.isSaved = false;
        return true;
    }
    public static void main(String[] args){

    }
}
