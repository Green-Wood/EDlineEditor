package edLineEditor.Commands;

import edLineEditor.FalseInputFormatException;
import edLineEditor.LocInfo;
import edLineEditor.Page;

public class MarkLine extends Command{
    public MarkLine(LocInfo info, Page page) throws FalseInputFormatException {
        super(info, page);
        if (endIndex != begIndex)
            throw new FalseInputFormatException();
        char c = info.markChara();
        if (!Character.isLowerCase(c))
            throw new FalseInputFormatException();
    }

    @Override
    public boolean run(){
        char c = info.markChara();
        page.setMark(c, begIndex);
        return true;
    }
}
