package edLineEditor.Commands;

import edLineEditor.FalseInputFormatException;
import edLineEditor.LocInfo;
import edLineEditor.Page;

public class Undo extends Command{
    public Undo(LocInfo info, Page page) {
        super(info, page);
    }

    @Override
    public boolean run() throws FalseInputFormatException {
        page.unDo();
        return true;
    }
}
