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
        String filename;
        if (init.trim().equals("ed")) page = new Page();            // 两种初始化方法
        else {
            filename = init.trim().split(" ")[1];
            page = new Page(filename);
        }
        String str = "";                          // 记录上一次替换的指令
        while (in.hasNextLine()){
            String line = in.nextLine();
            int beginIndex;
            int endIndex;
            char command;
            String newline = TransLoc.transLoc(line, page);
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
                if (endIndex == page.currPage.size()) page.setCurrLine(beginIndex - 1);
                else page.setCurrLine(beginIndex);
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
                    page.setCurrLine(endIndex);
                }
                else {
                    if (line.charAt(0) == 'z' && page.getCurrLine() == page.currPage.size()){
                        System.out.println("?");
                        continue;
                    }
                    if (beginIndex == page.getCurrLine()) beginIndex++;            // 如果使用默认值，则加一
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
                if (!page.isSaved && !isConfirmed && page.hasChanged() && !page.getFilename().equals("")){
                    System.out.println("?");              // 提示后退出
                    isConfirmed = true;
                }
                else {
                    break;
                }
            }
            else if (command == 'f'){           // 设置文件名
                if (newline.split(" ").length == 3){
                    page.setFilename(newline.split(" ")[2]);
                }
                else {
                    if (page.getFilename().equals("")) System.out.println("?");
                    else System.out.println(page.getFilename());
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
                    if (page.getFilename().equals("")) System.out.println("?");
                    else FileTool.saveFile(beginIndex, endIndex, page, page.getFilename(), isAdd);
                }
            }
            else if (command == 'm' || command == 't'){
                int toIndex;
                if (newline.indexOf(Character.toString(command)) == newline.length() - 1){
                    toIndex = page.getCurrLine();
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
                page.setMark(c, beginIndex);
            }
            else if (command == 'u'){
                page.unDo();
            }
        }
        in.close();
	}
}
