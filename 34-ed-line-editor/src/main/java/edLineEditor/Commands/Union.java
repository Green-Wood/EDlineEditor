package edLineEditor.Commands;

import edLineEditor.FalseInputFormatException;
import edLineEditor.CommandInfo;
import edLineEditor.Page;

public class Union extends Command {
    public Union(CommandInfo info, Page page) throws FalseInputFormatException {
        super(info, page);
        if (info.isDefaultLoc() && page.getCurrLineNumber() + 1 >= page.getSize()
                || begIndex == -1)                       // 不能指定最后一行作为合并目标
            throw new FalseInputFormatException();
    }

    @Override
    public void execute() {
        if (info.isDefaultLoc()){                  // 默认地址则为当前行和当前行后一行
            begIndex = page.getCurrLineNumber();
            endIndex = begIndex + 1;
            union(begIndex, endIndex);
            page.setCurrLine(begIndex);
        }
        else if (begIndex != endIndex){
            union(begIndex, endIndex);
            page.setCurrLine(begIndex);
        }
        // 若指定单行，则不做任何操作
    }

    private void union(int begIndex, int endIndex){
        page.saveCurrent();
        StringBuilder str = new StringBuilder(page.getLine(begIndex));
        for (int i = begIndex; i < endIndex; i++){
            str.append(page.getLine(begIndex + 1));
            page.deleteLine(begIndex + 1);
        }
        page.deleteLine(begIndex);
        page.addLine(begIndex, str.toString());
        page.isSaved = false;
    }
}
