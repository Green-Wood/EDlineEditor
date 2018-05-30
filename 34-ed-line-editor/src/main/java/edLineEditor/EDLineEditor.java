package edLineEditor;
import java.util.Scanner;

public class EDLineEditor {
	
	/**
	 * 接收用户控制台的输入，解析命令，根据命令参数做出相应处理。
	 * 不需要任何提示输入，不要输出任何额外的内容。
	 * 输出换行时，使用System.out.println()。或者换行符使用System.getProperty("line.separator")。
	 * 
	 * 待测方法为public static void main(String[] args)方法。args不传递参数，所有输入通过命令行进行。
	 * 方便手动运行。
	 * 
	 * 说明：可以添加其他类和方法，但不要删除该文件，改动该方法名和参数，不要改动该文件包名和类名
	 */

	public static void main(String[] args) {

	    boolean isConfirmed = false;

        Scanner in = new Scanner(System.in);
        String init = in.nextLine();
        Page page;
        if (init.trim().equals("ed")) page = new Page();            // 两种初始化方法
        else {
            String filename = init.trim().split(" ")[1];
            page = new Page(filename);
        }
        String str = "";                          // 记录上一次替换的指令
        while (in.hasNextLine()){
            String line = in.nextLine();
            int beginIndex;
            int endIndex;
            char command;
            String newline = transLoc(line, page);
            command = newline.split(" ")[1].charAt(0);
            String[] loc = newline.split(" ")[0].split(",");
            beginIndex = Integer.parseInt(loc[0]);
            endIndex = Integer.parseInt(loc[1]);

            if (beginIndex > endIndex || beginIndex < 0    // 检查是否符合
                    || endIndex > page.currPage.size()){
                System.out.println("?");
                continue;
            }

            if (command == 'a' || command == 'i'
                    || command == 'c'){
                InputMode input;
                if (command == 'c'){
                    input = new InputMode(beginIndex, endIndex, page);
                }
                else {
                    input = new InputMode(beginIndex, command, page);
                }
                input.insert(in);            // 输入
            }
            else if (command == 'd'){         // 删除
                page.saveCurrent();
                if (endIndex == page.currPage.size()) page.currLine = beginIndex - 1;
                else page.currLine = beginIndex;
                for (int i = beginIndex; i <= endIndex; i++){
                    page.currPage.remove(beginIndex-1);
                }
                page.isSaved = false;
            }
            else if (command == 'p' || command == '='
                    || command == 'z'){
                if (command == '=') System.out.println(beginIndex);  // 打印当前行号
                else if (command == 'p'){
                    FileTool.printLines(beginIndex, endIndex, page);       // 打印指定多行
                    page.currLine = endIndex;
                }
                else {
                    if (line.charAt(0) == 'z' && page.currLine == page.currPage.size()){
                        System.out.println("?");
                        continue;
                    }
                    int index = newline.indexOf("z");
                    if (index == newline.length() - 1) FileTool.printLines(beginIndex, page.currPage.size(), page);
                    else {
                        String param = newline.split("z")[1];
                        int n = Integer.parseInt(param);
                        if (beginIndex + n > page.currPage.size()){
                            FileTool.printLines(beginIndex, page.currPage.size(), page);
                        }
                        else{
                            FileTool.printLines(beginIndex, beginIndex + n, page);
                        }

                    }
                }
            }
            else if (command == 'Q'){
                break;
            }
            else if (command == 'q'){
                if (!page.isSaved && !isConfirmed && !page.filename.equals("")){ //TODO 检查文件是否被修改
                        System.out.println("?");              // 提示后退出
                    isConfirmed = true;
                }
                else {
                    break;
                }
            }
            else if (command == 'f'){           // 设置文件名
                if (newline.split(" ").length == 3){
                    page.filename = newline.split(" ")[2];
                }
                else {
                    if (page.filename.equals("")) System.out.println("?");
                    else System.out.println(page.filename);
                }
            }
            else if (command == 'w' || command == 'W'){                 // 小写w与大写W只是是否追加的区别
                boolean isAdd;
                if (command =='w') isAdd = false;
                else isAdd = true;
                if (Character.isAlphabetic(line.charAt(0))) {             // 未指定时，保存全文
                    beginIndex = 1;
                    endIndex = page.currPage.size();
                }
                if (newline.split(" ").length == 3){
                    FileTool.saveFile(beginIndex, endIndex, page, line.split(" ")[1], isAdd);
                }
                else {
                    if (page.filename.equals("")) System.out.println("?");
                    else FileTool.saveFile(beginIndex, endIndex, page, page.filename, isAdd);
                }
            }
            else if (command == 'm' || command == 't'){
                int toIndex;
                if (newline.indexOf(Character.toString(command)) == newline.length() - 1){
                    toIndex = page.currLine;
                }
                else {
                    toIndex = Integer.parseInt(newline.split(Character.toString(command))[1]);
                }
                if (toIndex > page.currPage.size()) {
                    System.out.println("?");
                    continue;
                }
                Editor editor = new Editor(beginIndex, endIndex, page);
                if (command == 'm') editor.move(toIndex);
                else editor.copy(toIndex);
            }
            else if (command == 'j'){
                if (beginIndex == endIndex){
                    endIndex++;
                }
                Editor editor = new Editor(beginIndex, endIndex, page);
                editor.union();
            }
            else if (command == 's'){
                int count = 0;
                int index = 0;
                for (int i = line.length() - 1; i >= 0; i--){
                    if (line.charAt(i) == '/')   break;
                    if (line.charAt(i) == 's') {
                        line = str;                         // 如果不含参数，则使用上一条s指令的参数
                        break;
                    }
                }
                for (int i = line.length() - 1; i >= 0; i--){
                    if (line.charAt(i) == '/') count++;
                    if (count == 3) {
                        index = i;
                        break;
                    }
                }
                String toLoc = line.substring(index + 1, line.length());
                String[] splitLoc = toLoc.split("/");
                Editor editor = new Editor(beginIndex, endIndex, page);
                boolean isSuccess;
                if (splitLoc.length == 3){
                    if (splitLoc[2].equals("g")) isSuccess = editor.replace(splitLoc[0], splitLoc[1]);
                    else isSuccess = editor.replace(splitLoc[0], splitLoc[1], Integer.parseInt(splitLoc[2]));
                }
                else {
                    isSuccess = editor.replace(splitLoc[0], splitLoc[1], 1);
                }
                if (!isSuccess) System.out.println("?");       // 未成功打问号
                else {
                    str = line;
                }
            }
            else if (command == 'k'){
                char c = newline.charAt(newline.indexOf('k') + 1);
                page.mark.put(c, beginIndex);
            }
            else if (command == 'u'){
                page.unDo();
            }
        }
        in.close();
	}

    static String transLoc(String line, Page page){
	    if (times(line, '/') % 2 != 0){             // 解决替换时'/'产生的冲突
	        int count = 0;
	        int index = 0;
	        for (int i = line.length() - 1; i >= 0 ; i--){
	            if (line.charAt(i) == '/') count++;
	            if (count == 3) {
	                index = i;
	                break;
                }
            }
            line = line.substring(0, index);
        }
        if (line.length() == 0) return Integer.toString(page.currLine) + "," + Integer.toString(page.currLine);
	    if (line.charAt(0) == ';') {
	        String command = line.substring(1, line.length());
	        return Integer.toString(page.currLine) + "," + Integer.toString(page.currPage.size()) + " " + command;
        }
        while (line.contains("'")){                // 转化标记符
            int i = line.indexOf("'");
            String o = line.substring(i, i + 2);
            int lineNumber = page.mark.get(o.charAt(1));
            line = line.replace(o, Integer.toString(lineNumber));
        }
        while (line.contains("/")){              // 转化匹配字符
            int i1 = line.indexOf("/");
            int i2 = line.indexOf("/", i1 + 1);
            String s = line.substring(i1, i2 + 1);
            int lineNumber = page.findDownLineNumber(s.substring(1, s.length() - 1));
            line = line.replace(s, Integer.toString(lineNumber));
        }
        while (line.contains("?")){
            int i1 = line.indexOf("?");
            int i2 = line.indexOf("?", i1 + 1);
            String s = line.substring(i1, i2 + 1);
            int lineNumber = page.findUpLineNumber(s.substring(1, s.length() - 1));
            line = line.replace(s, Integer.toString(lineNumber));
        }
        if (line.contains(".")){
	        if (line.contains(".-")){
	            line = line.substring(1, line.length());
            }
            else {
                line = line.replace(".", Integer.toString(page.currLine));
            }
        }
        if (line.contains(",") && (Character.isAlphabetic(line.charAt(line.indexOf(",") + 1))
                || line.charAt(line.indexOf(",") + 1) == '=')){
            line = line.replace(",", String.format("1,%d", page.currPage.size()));
        }
        if (Character.isAlphabetic(line.charAt(0)) || line.charAt(0) == '='){
            line = String.format("%d,%d", page.currLine, page.currLine) + line;
        }
        while (line.contains("-")){
            if (line.contains("$-")){
               int i = line.indexOf("-");
               int n = Integer.parseInt(line.substring(i + 1, i + 2));
               String formal = line.substring(i - 1, i + 2);
               String newS = Integer.toString(page.currPage.size() - n);
               line = line.replace(formal, newS);
            }
            else {                      // 在当前行进行修改
                int i = line.indexOf("-");
                int n = Integer.parseInt(line.substring(i + 1, i + 2));
                String formal = line.substring(i, i + 2);
                String newS = Integer.toString(page.currLine - n);
                line = line.replace(formal, newS);
            }
        }
        while (line.contains("+")){
            if (line.contains("$+")){
                int i = line.indexOf("+");
                int n = Integer.parseInt(line.substring(i + 1, i + 2));
                String formal = line.substring(i - 1, i + 2);
                String newS = Integer.toString(page.currPage.size() + n);
                line = line.replace(formal, newS);
            }
            else {                      // 在当前行进行修改
                int i = line.indexOf("+");
                int n = Integer.parseInt(line.substring(i + 1, i + 2));
                String formal = line.substring(i, i + 2);
                String newS = Integer.toString(page.currLine + n);
                line = line.replace(formal, newS);
            }
        }
        if (line.contains("$")){
            line = line.replace("$", Integer.toString(page.currPage.size()));
        }
        if (line.contains(",") && Character.isDigit(line.charAt(line.indexOf(",") - 1))
                && (Character.isAlphabetic(line.charAt(line.indexOf(",") + 1))
                || line.charAt(line.indexOf(",") + 1) == '=')){
            String old = line.substring(line.indexOf(",") - 1, line.indexOf(",") + 1);
            String New = old + Integer.toString(page.currLine);
            line = line.replace(old, New);
        }
        if (line.charAt(0) == ',' && Character.isDigit(line.charAt(line.indexOf(",") + 1))){
            String old = line.substring(0, 2);
            String New = old + Integer.toString(page.currLine);
            line = line.replace(old, New);
        }

        if (!line.contains(",")){
            int i;
            for (i = 0; i < line.length(); i++){
                if (Character.isAlphabetic(line.charAt(i)) || line.charAt(i) == '='){
                    break;
                }
            }
            String num = line.substring(0, i);
            String New = num + "," + num;
            line = New + line.substring(i, line.length());
        }
        int i;
        for (i = 0; i < line.length(); i++){
            if (Character.isAlphabetic(line.charAt(i)) || line.charAt(i) == '='){
                break;
            }
        }
        line = line.substring(0, i) + " " + line.substring(i, line.length());
        return line;
    }

    private static int times(String s, char c){
	    int n = 0;
	    for (int i = 0; i < s.length(); i++){
	        if (s.charAt(i) == c) n++;
        }
        return n;
    }
}
