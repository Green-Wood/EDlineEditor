package edLineEditor.Commands;

import edLineEditor.FalseInputFormatException;
import edLineEditor.CommandInfo;
import edLineEditor.Page;

public class Union extends Command {
    public Union(CommandInfo info, Page page) throws FalseInputFormatException {
        super(info, page);
        if (info.isDefaultLoc() && page.getCurrLine() + 1 >= page.getSize()
                || begIndex == -1)
            throw new FalseInputFormatException();
    }

    @Override
    public void run() {
        if (info.isDefaultLoc()){
            begIndex = page.getCurrLine();
            endIndex = begIndex + 1;
            union(begIndex, endIndex);
            page.setCurrLine(begIndex);
        }
        else if (begIndex != endIndex){
            union(begIndex, endIndex);
            page.setCurrLine(begIndex);
        }
    }

    private void union(int begIndex, int endIndex){
        page.saveCurrent();
        StringBuilder str = new StringBuilder(page.getLine(begIndex));
        for (int i = begIndex; i < endIndex; i++){
            str.append(page.getLine(begIndex + 1));
            page.deleteLine(begIndex + 1);
        }
        page.deleteLine(begIndex);
        page.addLine(begIndex, str.toString());
        page.isSaved = false;
    }
}
