package edLineEditor;
import edLineEditor.Commands.*;

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
        Page page;
        LocInfo info;
        Command command;
        String fileName;
        boolean isConfirmed = false;                      // 是否确定退出
        String oldStr = "";                        // 记录上一次参数
        String newStr = "";
        int count = -1;

        Scanner in = new Scanner(System.in);
        String init = in.nextLine();
        if (init.equals("ed"))                  // 初始化Page
            page = new Page();
        else {
            fileName = init.split(" ")[1];
            page = new Page(fileName);
        }
        while (in.hasNextLine()){
            try {
                String line = in.nextLine();
                info = new LocInfo(line, page);
                char c = info.getCommand();
                if (c == 'a' || c == 'i' || c == 'c'){
                    Input input = new Input(info, page);
                    input.insert(in);
                    continue;
                }
                else if (c == 'Q' || c == 'q'){
                    if (!info.isDefaultLoc()) throw new FalseInputFormatException();
                    if (c == 'Q'){
                        break;
                    }
                    else {
                        if (!page.isSaved && !isConfirmed && page.hasChanged()){
                            isConfirmed = true;
                            throw new FalseInputFormatException();
                        }
                        else {
                            break;
                        }
                    }
                }
                else if (c == 'd'){
                    command = new Delete(info, page);
                }
                else if (c == '='){
                    command = new PrintLineNumber(info, page);
                }
                else if (c == 'p'){
                    command = new PrintLines(info, page);
                }
                else if (c == 'z'){
                    command = new PrintLinesTo(info, page);
                }
                else if (c == 'f'){
                    command = new FileName(info, page);
                }
                else if (c == 'w' || c == 'W'){
                    command = new SaveFile(info, page);
                }
                else if (c == 'm'){
                    command = new Move(info, page);
                }
                else if (c == 't'){
                    command = new Copy(info, page);
                }
                else if (c == 'j'){
                    command = new Union(info, page);
                }
                else if (c == 's'){
                    if (line.charAt(line.length() - 1) == 's'){    // 不指定参数则使用以前的
                        if (count == -1) throw new FalseInputFormatException();
                        info.set_S_Param(oldStr, newStr, count);
                    }
                    Replace replace = new Replace(info, page);
                    replace.isContain(info.originStr());
                    replace.run();
                    oldStr = info.originStr();
                    newStr = info.changeToStr();
                    count = info.replaceCount();
                    continue;
                }
                else if (c == 'k'){
                    command = new MarkLine(info, page);
                }
                else if (c == 'u'){
                    command = new Undo(info, page);
                }
                else throw new FalseInputFormatException();
                command.run();                                  // 开始操作
            }catch (FalseInputFormatException e){
                System.out.println("?");
            }
        }
	}
}
