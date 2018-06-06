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

        Editor editor = new Editor(page);              // 初始化editor
        String str = "";                          // 记录上一次替换的指令

        while (in.hasNextLine()){
            String line = in.nextLine();
            if (line.trim().length() == 0) {
                System.out.println("?");
                continue;
            }
            String newline = TransLoc.transLoc(line.trim(), editor);
            if (newline.split(" ").length == 1){
                System.out.println("?");
                continue;
            }
            char command = newline.split(" ")[1].charAt(0);   // 获取命令
            int beginIndex = editor.getBeginIndex();    // 获取开始和终止行
            int endIndex = editor.getEndIndex();

            if (beginIndex > endIndex || beginIndex < 0    // 检查是否符合
                    || endIndex > page.currPage.size()){
                System.out.println("?");
                continue;
            }

            if (command == 'a' || command == 'i'
                    || command == 'c'){
                InputMode input;
                if (command == 'c'){
                    if (beginIndex == 0) {
                        System.out.println("?");
                        continue;
                    }
                    input = new InputMode(beginIndex, endIndex, page);
                }
                else {
                    input = new InputMode(beginIndex, command, page);
                }
                input.insert(in);            // 输入
            }
            else if (command == 'd'){         // 删除
                page.saveCurrent();
                if (beginIndex == 0){
                    System.out.println("?");
                    continue;
                }
                if (endIndex == page.currPage.size()) page.setCurrLine(beginIndex - 1);
                else page.setCurrLine(beginIndex);
                for (int i = beginIndex; i <= endIndex; i++){
                    page.currPage.remove(beginIndex-1);
                }
                page.isSaved = false;
            }
            else if (command == '=') {
                System.out.println(beginIndex);  // 打印当前行号
            }
            else if (command == 'p'){
                editor.printLines();       // 打印指定多行
                page.setCurrLine(endIndex);
            }
            else if (command == 'z'){
                if (line.charAt(0) == 'z' && page.getCurrLine() == page.currPage.size()){
                    System.out.println("?");
                    continue;
                }
                if (beginIndex == page.getCurrLine()) beginIndex++;            // 如果使用默认值，则加一
                int index = newline.indexOf("z");
                editor.setLines(beginIndex, page.currPage.size());               // 设置打印范围
                if (index == newline.length() - 1) {                     // 若指令后无参数，则打印全部
                    editor.printLines();
                }
                else {
                    String param = newline.split("z")[1];           // 打印到参数位置
                    int n = Integer.parseInt(param);
                    if (beginIndex + n > page.currPage.size()){
                        editor.printLines();
                    }
                    else{
                        editor.setLines(beginIndex, beginIndex + n);
                        editor.printLines();
                    }

                }
            }
            else if (command == 'Q'){
                break;
            }
            else if (command == 'q'){
                if (!page.isSaved && !isConfirmed && page.hasChanged()){
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
                isAdd = command != 'w';
                if (Character.isAlphabetic(line.charAt(0))) {             // 未指定时，保存全文
                    editor.setLines(1, page.currPage.size());
                }
                if (newline.split(" ").length == 3){
                    editor.saveFile(line.split(" ")[1], isAdd);
                }
                else {
                    if (page.getFilename().equals("")) System.out.println("?");
                    else editor.saveFile(page.getFilename(), isAdd);
                }
            }
            else if (command == 'm' || command == 't'){
                int toIndex;
                if (newline.indexOf(Character.toString(command)) == newline.length() - 1){
                    toIndex = page.getCurrLine();
                }
                else {
                    String to = newline.substring(newline.indexOf(Character.toString(command)) + 1, newline.length());
                    String newTo = TransLoc.transLoc(to + "m", editor);
                    editor.setLines(beginIndex, endIndex);
                    toIndex = Integer.parseInt(newTo.split(",")[0]);
                }
                if (toIndex > page.currPage.size() ||
                        (command == 'm' && beginIndex <= toIndex && endIndex >= toIndex)) {
                    System.out.println("?");
                    continue;
                }
                if (command == 'm') editor.move(toIndex);
                else editor.copy(toIndex);
            }
            else if (command == 'j'){
                if (line.charAt(0) == 'j'){
                    beginIndex = page.getCurrLine();
                    endIndex = beginIndex + 1;
                    if (endIndex > page.currPage.size()){
                        System.out.println("?");
                        continue;
                    }
                    editor.setLines(beginIndex, endIndex);
                    editor.union();
                }
                else if (beginIndex != endIndex){
                    if (beginIndex == 0){
                        System.out.println("?");
                        continue;
                    }
                    editor.union();
                }
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
                if (index + 1 > line.length()){
                    System.out.println("?");
                    continue;
                }
                String toLoc = line.substring(index + 1, line.length());
                String[] splitLoc = toLoc.split("/");
                boolean isSuccess;
                if (splitLoc.length == 3){                // 有指定次数
                    if (splitLoc[2].equals("g")) {
                        isSuccess = editor.replace(splitLoc[0], splitLoc[1]);          // 替换全部
                    }
                    else {
                        isSuccess = editor.replace(splitLoc[0], splitLoc[1], Integer.parseInt(splitLoc[2]));
                    }
                }
                else if (splitLoc.length == 2){
                    isSuccess = editor.replace(splitLoc[0], splitLoc[1], 1);
                }
                else isSuccess = false;
                if (!isSuccess) System.out.println("?");       // 未成功打问号
                else {
                    str = line;                     // 保存此次指令
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
