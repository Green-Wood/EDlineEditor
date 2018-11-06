package edLineEditor;

import edLineEditor.Commands.*;

// 指令的静态工厂，如需添加指令可以在此添加、删除
public class CommandFactory {
    public static Command getCommand(char c, Page page, CommandInfo info) throws FalseInputFormatException {
        Command command;
        if (c == 'd'){
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
        else if (c == 'k'){
            command = new MarkLine(info, page);
        }
        else if (c == 'u'){
            command = new Undo(info, page);
        }
        else throw new FalseInputFormatException();
        return command;
    }
}
