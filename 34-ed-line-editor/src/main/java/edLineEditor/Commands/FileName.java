package edLineEditor.Commands;

import edLineEditor.FalseInputFormatException;
import edLineEditor.CommandInfo;
import edLineEditor.Page;

public class FileName extends Command{
    public FileName(CommandInfo info, Page page) throws FalseInputFormatException {
        super(info, page);
        if (info.fileName().equals("") && page.getFilename().equals(""))
            throw new FalseInputFormatException();
        if (!info.isDefaultLoc())
            throw new FalseInputFormatException();
    }

    @Override
    public void execute() {
        if (info.fileName().equals("")){
            System.out.println(page.getFilename());
        }
        else {
            page.setFilename(info.fileName());
        }
    }
}
