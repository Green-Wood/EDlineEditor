package edLineEditor.Commands;

import edLineEditor.FalseInputFormatException;
import edLineEditor.LocInfo;
import edLineEditor.Page;

public class MarkLine extends Command{
    char markChara;
    public MarkLine(LocInfo info, Page page) throws FalseInputFormatException {
        super(info, page);
        markChara = info.markChara();
        if (endIndex != begIndex)
            throw new FalseInputFormatException();
        if (!Character.isLowerCase(markChara))
            throw new FalseInputFormatException();
    }

    @Override
    public void run(){
        page.setMark(markChara, begIndex);
    }
}
