package edLineEditor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

// 缓存文件类，在未保存之前，所有文件操作均在Page类中操作
public class Page {
    LinkedList<String> currPage;        // 保存当前和上一个状态，用于撤销
    Stack<LinkedList<String>> allPages;
    private String filename;                     // 文件名称
    private int currLine;                        // 保存当前行
    HashMap<Character, Integer> mark;     // 保存标记符号
    boolean isSaved;

    public Page(){                       // 从ed直接进入
        currPage = new LinkedList<>();
        allPages = new Stack<>();
        mark = new HashMap<>();
        setFilename("");
        currLine = 0;
    }

    public Page(String filename){           // 从文件读入
        ArrayList<String> file = FileTool.readFile(filename);
        currPage = new LinkedList<>();
        allPages = new Stack<>();
        mark = new HashMap<>();
        setFilename(filename);
        currLine = file.size();
        currPage.addAll(file);
        allPages.push((LinkedList<String>) currPage.clone());
    }

    public String getFilename(){
        return this.filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getCurrLine(){
        return this.currLine ;
    }

    public void setCurrLine(int line){
        this.currLine = line;
    }

    public void saveCurrent(){              // 在对文本操作前保存当前状态，修改前必须调用
        allPages.push((LinkedList<String>) currPage.clone());
    }

    public void unDo(){                    // 撤销操作，返回至saveCurrent的状态
        LinkedList<String> swap = allPages.pop();
        currPage = (LinkedList<String>) swap.clone();
    }

    public int findDownLineNumber(String str){           // 从本行往下寻找相匹配的字符串
        for (int i = currLine; i < currPage.size(); i++){
            String s = currPage.get(i);
            if (s.contains(str)) return i + 1;
        }
        for (int i = 0; i < currLine; i++){
            if (currPage.get(i).contains(str)) return i + 1;
        }
        return -1;
    }

    public int findUpLineNumber(String str){                  // 从本行往上寻找匹配的字符串
        for (int i = currLine - 2; i >= 0; i--){
            String s = currPage.get(i);
            if (s.contains(str)) return i + 1;
        }
        for (int i = currPage.size() - 1; i > currLine - 2; i--){
            if (currPage.get(i).contains(str)) return i + 1;
        }
        return -1;
    }

    public boolean hasChanged(){                          // 对比文本是否发生更改
        LinkedList<String> first = allPages.firstElement();
        return !first.equals(currPage);
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
