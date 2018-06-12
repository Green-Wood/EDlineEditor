package edLineEditor.Commands;

import edLineEditor.FalseInputFormatException;
import edLineEditor.LocInfo;
import edLineEditor.Page;

public class Undo extends Command{
    public Undo(LocInfo info, Page page) throws FalseInputFormatException {
        super(info, page);
        if (!info.isDefaultLoc()) throw new FalseInputFormatException();
    }

    @Override
    public void run(){
        page.unDo();
    }
}
