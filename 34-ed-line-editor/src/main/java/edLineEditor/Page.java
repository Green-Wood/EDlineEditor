package edLineEditor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

// 缓存文件类，在未保存之前，所有文件操作均在Page类中操作
public class Page {
    LinkedList<String> currPage;        // 保存当前和以前的状态，用于撤销
    private Stack<LinkedList<String>> allPages;
    private String filename;                     // 文件名称
    private int currLine;                        // 保存当前行
    private HashMap<Character, Integer> mark;     // 保存标记符号
    boolean isSaved;

    Page(){                       // 从ed直接进入
        currPage = new LinkedList<>();
        allPages = new Stack<>();
        mark = new HashMap<>();
        setFilename("");
        currLine = 0;
    }

    Page(String filename){           // 从文件读入
        ArrayList<String> file = readFile(filename);
        currPage = new LinkedList<>();
        allPages = new Stack<>();
        mark = new HashMap<>();
        setFilename(filename);
        currLine = file.size();
        currPage.addAll(file);
        allPages.push((LinkedList<String>) currPage.clone());
    }

    String getFilename(){
        return this.filename;
    }

    void setFilename(String filename) {
        this.filename = filename;
    }

    int getCurrLine(){
        return this.currLine ;
    }

    void setCurrLine(int line){
        this.currLine = line;
    }

    void setMark(char c, int lineNumber){
        mark.put(c, lineNumber);
    }

    int getMark(char c){
        return mark.getOrDefault(c, -1);
    }

    Set<Map.Entry<Character, Integer>> getMarkedEntry(){
        return mark.entrySet();
    }

    void deleteMark(char c){
        mark.remove(c);
    }

    void saveCurrent(){              // 在对文本操作前保存当前状态，修改前必须调用
        allPages.push((LinkedList<String>) currPage.clone());
    }

    void unDo(){                    // 撤销操作，返回至saveCurrent的状态
        currPage = allPages.pop();
    }

    int findDownLineNumber(String str){           // 从本行往下寻找相匹配的字符串
        for (int i = currLine; i < currPage.size(); i++){
            String s = currPage.get(i);
            if (s.contains(str)) return i + 1;
        }
        for (int i = 0; i < currLine; i++){
            if (currPage.get(i).contains(str)) return i + 1;
        }
        return -1;
    }

    int findUpLineNumber(String str){                  // 从本行往上寻找匹配的字符串
        for (int i = currLine - 2; i >= 0; i--){
            String s = currPage.get(i);
            if (s.contains(str)) return i + 1;
        }
        for (int i = currPage.size() - 1; i > currLine - 2; i--){
            if (currPage.get(i).contains(str)) return i + 1;
        }
        return -1;
    }

    boolean hasChanged(){                          // 对比文本是否发生更改
        LinkedList<String> first = allPages.firstElement();
        return !first.equals(currPage);
    }

    private ArrayList<String> readFile(String filename){            // 从文件读入
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

    public static void main(String[] args){
        Page test = new Page();
        test.currPage.add("ZWQ");
        test.saveCurrent();
        test.currPage.add("Love");
        System.out.println(test.currPage.toString());
        test.unDo();
    }
}
