package edLineEditor.Commands;

import edLineEditor.FalseInputFormatException;
import edLineEditor.LocInfo;
import edLineEditor.Page;

public class Union extends Command {
    public Union(LocInfo info, Page page) throws FalseInputFormatException {
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
        String str = page.getLine(begIndex);
        for (int i = begIndex; i < endIndex; i++){
            str += page.getLine(begIndex + 1);
            page.deleteLine(begIndex + 1);
        }
        page.deleteLine(begIndex);
        page.addLine(begIndex, str);
        page.isSaved = false;
    }
}
