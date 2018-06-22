package edLineEditor.Commands;

import edLineEditor.FalseInputFormatException;
import edLineEditor.CommandInfo;
import edLineEditor.Page;

public class MarkLine extends Command{
    private char markChara;
    public MarkLine(CommandInfo info, Page page) throws FalseInputFormatException {
        super(info, page);
        markChara = info.markChara();
        if (endIndex != begIndex)                                 // 只允许指定一个地址
            throw new FalseInputFormatException();
        if (!Character.isLowerCase(markChara))                  // 标记符必须是小写字母
            throw new FalseInputFormatException();
    }

    @Override
    public void execute(){
        page.setMark(markChara, begIndex);
    }
}
