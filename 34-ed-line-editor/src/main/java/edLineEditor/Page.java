package edLineEditor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Page {

    private LinkedList<String> currPage;            // 当前正在使用的页面
    private Stack<LinkedList<String>> allPages;          // 保存所有页面
    private Stack<Integer> allCurrLine;
    private HashMap<Character, String> mark;      // 保存标记
    private String fileName;             // 文件名称
    private int currLine;            // 当前行
    public boolean isSaved;              // 是否已经保存为文件

    public Page(){
        currPage = new LinkedList<>();
        allPages = new Stack<>();
        allCurrLine = new Stack<>();
        mark = new HashMap<>();
        setFilename("");
        currLine = -1;
    }

    public Page(String fileName){
        ArrayList<String> file = readFile(fileName);
        currPage = new LinkedList<>();
        allPages = new Stack<>();
        allCurrLine = new Stack<>();
        mark = new HashMap<>();
        setFilename(fileName);
        currLine = file.size() - 1;
        currPage.addAll(file);
        allPages.push((LinkedList<String>) currPage.clone());
        allCurrLine.push(currLine);
    }
    public int getSize(){
        return currPage.size();
    }
    public String getFilename(){
        return this.fileName;
    }

    public void setFilename(String filename) {
        this.fileName = filename;
    }

    public int getCurrLine(){
        return this.currLine ;
    }

    public void setCurrLine(int line){
        this.currLine = line;
    }          // 设置当前行行号

    public void setMark(char c, int lineNum){
        mark.put(c, currPage.get(lineNum));
    }          // 设置标记行

    int getMark(char c) throws FalseInputFormatException {
        return searchLine(mark.getOrDefault(c, ""));
    }

    public void addLine(int index, String s){
        currPage.add(index, s);
    }   // 增加指定行

    public void deleteLine(int index){
        currPage.remove(index);
    }        // 删除指定行

    public String getLine(int index){
        return currPage.get(index);
    }       // 得到指定行

    public void saveCurrent(){     // 保存当前状态
        allPages.push((LinkedList<String>) currPage.clone());
        allCurrLine.push(currLine);
    }

    public void unDo(){    // 撤销操作
        currPage = allPages.pop();
        currLine = allCurrLine.pop();
    }

    boolean hasChanged(){          // 对比文本是否发生更改
        LinkedList<String> first = allPages.firstElement();
        return !first.equals(currPage);
    }

    private int searchLine(String s) throws FalseInputFormatException {          // 由string指针寻找地址相同的string
        for (int i = 0; i < currPage.size(); i++){
            if (currPage.get(i) == s){
                return i;
            }
        }
        throw new FalseInputFormatException();             // 找不到就抛出异常
    }

    int findDownLineNumber(String str){      // 从本行往下寻找相匹配的字符串
        for (int i = currLine + 1; i < currPage.size(); i++){
            String s = currPage.get(i);
            if (s.contains(str)) return i;
        }
        for (int i = 0; i <= currLine; i++){
            String s = currPage.get(i);
            if (s.contains(str)) return i;
        }
        return -1;
    }

    int findUpLineNumber(String str){              // 从本行往上寻找相匹配的字符串
        for (int i = currLine - 1; i >= 0; i--){
            String s = currPage.get(i);
            if (s.contains(str)) return i;
        }
        for (int i = currPage.size() - 1; i >= currLine; i--){
            String s = currPage.get(i);
            if (s.contains(str)) return i;
        }
        return -1;
    }

    private ArrayList<String> readFile(String filename){            // 从文件读入
        File file = new File(filename);
        ArrayList<String> list = new ArrayList<>();
        try {
            Scanner in = new Scanner(file);
            while (in.hasNextLine()){
                String s = new String(in.nextLine());
                list.add(s);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }
}
