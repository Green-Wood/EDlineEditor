package edLineEditor.Commands;

import edLineEditor.FalseInputFormatException;
import edLineEditor.LocInfo;
import edLineEditor.Page;

public abstract class Command {
    LocInfo info;
    Page page;
    int begIndex;
    int endIndex;
    public Command(LocInfo info, Page page){
        this.info = info;
        this.page = page;
        this.begIndex = info.getBeginIndex();
        this.endIndex = info.getEndIndex();
    }
    public abstract boolean run() throws FalseInputFormatException;
}
