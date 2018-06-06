package edLineEditor;
import java.util.Map;
import java.util.Scanner;

public class InputMode {
    private Page page;
    private int index;
    InputMode(int beginIndex, int endIndex, Page page){
        // 替换模式
        beginIndex--;
        endIndex--;
        page.saveCurrent();
        for (Map.Entry<Character, Integer> entry: page.getMarkedEntry()){         // 删除符号
            char c = entry.getKey();
            int lineNumber = entry.getValue() - 1;
            if (lineNumber >= beginIndex && lineNumber <= endIndex) {
                page.deleteMark(c);
            }
        }
        for (int i = beginIndex; i <= endIndex; i++){
            page.currPage.remove(beginIndex);
        }
        index = beginIndex;
        this.page = page;
    }
    InputMode(int beginIndex, char command, Page page){
        // 追加在指定行后面
        beginIndex--;
        page.saveCurrent();
        if (command == 'a'){
            index = beginIndex + 1;
        }
        // 追加在指定行前面
        else {
            if (beginIndex == -1) index = 0;
            else index = beginIndex;
        }
        this.page = page;
    }

    void insert(Scanner in){
        int countAddline = 0;
        int startIndex = index;
        while (true){
            String line = in.nextLine();
            if (line.equals(".")) break;            // 检测到句号时退出
            page.currPage.add(index, line);
            index++;
            countAddline++;
        }
        for (Map.Entry<Character, Integer> entry: page.getMarkedEntry()){         // 调整符号
            char c = entry.getKey();
            int lineNumber = entry.getValue() - 1;
            if (lineNumber >= startIndex) {
                lineNumber = lineNumber + countAddline;
                page.setMark(c, lineNumber + 1);
            }
        }
        page.setCurrLine(index);
        page.isSaved = false;
    }

    public static void main (String[] args){
    }
}
