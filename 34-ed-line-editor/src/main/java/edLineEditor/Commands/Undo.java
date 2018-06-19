package edLineEditor.Commands;

import edLineEditor.FalseInputFormatException;
import edLineEditor.CommandInfo;
import edLineEditor.Page;

public class Undo extends Command{
    public Undo(CommandInfo info, Page page) throws FalseInputFormatException {
        super(info, page);
        if (!info.isDefaultLoc()) throw new FalseInputFormatException();
    }

    @Override
    public void execute(){
        page.unDo();
    }
}
