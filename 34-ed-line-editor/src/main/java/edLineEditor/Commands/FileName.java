package edLineEditor.Commands;

import edLineEditor.FalseInputFormatException;
import edLineEditor.LocInfo;
import edLineEditor.Page;

public class FileName extends Command{
    public FileName(LocInfo info, Page page) throws FalseInputFormatException {
        super(info, page);
        if (info.fileName().equals("") && page.getFilename().equals(""))
            throw new FalseInputFormatException();
        if (!info.isDefaultLoc())
            throw new FalseInputFormatException();
    }

    @Override
    public void run() {
        if (info.fileName().equals("")){
            System.out.println(page.getFilename());
        }
        else {
            page.setFilename(info.fileName());
        }
    }
}
