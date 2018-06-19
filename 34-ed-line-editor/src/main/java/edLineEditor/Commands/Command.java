package edLineEditor.Commands;

import edLineEditor.FalseInputFormatException;
import edLineEditor.CommandInfo;
import edLineEditor.Page;

public abstract class Command {
    CommandInfo info;
    Page page;
    int begIndex;
    int endIndex;
    public Command(CommandInfo info, Page page){
        this.info = info;
        this.page = page;
        this.begIndex = info.getBeginIndex();
        this.endIndex = info.getEndIndex();
    }
    public abstract void run() throws FalseInputFormatException;
}
