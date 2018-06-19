package edLineEditor.Commands;

import edLineEditor.FalseInputFormatException;
import edLineEditor.CommandInfo;
import edLineEditor.Page;

public class Delete extends Command {
    public Delete(CommandInfo info, Page page) throws FalseInputFormatException {
        super(info, page);
        if (begIndex == -1)
            throw new FalseInputFormatException();
    }

    @Override
    public void run() {
        page.saveCurrent();
        if (endIndex >= page.getSize() - 1)   // 如果删除的后面没有，则设为前一行
            page.setCurrLine(begIndex - 1);
        else page.setCurrLine(begIndex);
        for (int i = begIndex; i <= endIndex; i++){
            page.deleteLine(begIndex);
        }
        page.isSaved = false;
    }
}
