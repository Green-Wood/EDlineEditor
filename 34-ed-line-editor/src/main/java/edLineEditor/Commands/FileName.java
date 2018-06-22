package edLineEditor.Commands;

import edLineEditor.FalseInputFormatException;
import edLineEditor.CommandInfo;
import edLineEditor.Page;

public class FileName extends Command{
    public FileName(CommandInfo info, Page page) throws FalseInputFormatException {
        super(info, page);
        if (info.fileName().equals("") && page.getFilename().equals(""))    // 文件无名字且命令无名字
            throw new FalseInputFormatException();
        if (!info.isDefaultLoc())                                       // f指令不能包含地址
            throw new FalseInputFormatException();
    }

    @Override
    public void execute() {
        if (info.fileName().equals("")){                          // 未指定时输出当前文件名
            System.out.println(page.getFilename());
        }
        else {
            page.setFilename(info.fileName());
        }
    }
}
