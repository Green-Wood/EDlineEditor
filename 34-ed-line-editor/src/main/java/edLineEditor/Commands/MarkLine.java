package edLineEditor.Commands;

import edLineEditor.FalseInputFormatException;
import edLineEditor.CommandInfo;
import edLineEditor.Page;

public class MarkLine extends Command{
    private char markChara;
    public MarkLine(CommandInfo info, Page page) throws FalseInputFormatException {
        super(info, page);
        markChara = info.markChara();
        if (endIndex != begIndex)
            throw new FalseInputFormatException();
        if (!Character.isLowerCase(markChara))
            throw new FalseInputFormatException();
    }

    @Override
    public void execute(){
        page.setMark(markChara, begIndex);
    }
}
