package edLineEditor;
import java.util.Scanner;

public class InputMode {
    private Page page;
    private int index;
    public InputMode(int beginIndex, int endIndex, Page page){
        // 替换模式
        page.saveCurrent();
        beginIndex--;
        endIndex--;
        for (int i = beginIndex; i <= endIndex; i++){
            page.currPage.remove(beginIndex);
        }
        index = beginIndex;
        this.page = page;
    }
    public InputMode(int beginIndex, char command, Page page){
        // 追加在指定行后面
        page.saveCurrent();
        beginIndex--;
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

    public void insert(Scanner in){
        page.saveCurrent();
        while (true){
            String line = in.nextLine();
            if (line.equals(".")) break;            // 检测到句号时退出
            page.currPage.add(index, line);
            index++;
        }
        page.currLine = index;
    }

    public static void main (String[] args){
        Page page = new Page();
        page.currPage.add("ab");
        page.currPage.add("cc");
//        page.currPage.add("ad");
        page.currPage.add(0, "insertion");
        page.currLine = 4;
        String s = "3=";
//        FileTool.saveFile(1, page.currPage.size(), page, "test.txt", false);
        System.out.println(EDLineEditor.transLoc(s, page));
//        System.out.println(s.substring(3, 3).equals(""));
    }
}
