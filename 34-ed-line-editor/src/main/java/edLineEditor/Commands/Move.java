package edLineEditor.Commands;

import edLineEditor.FalseInputFormatException;
import edLineEditor.LocInfo;
import edLineEditor.Page;

import java.util.ArrayList;

public class Move extends Command {
    private int toIndex;
    public Move(LocInfo info, Page page) throws FalseInputFormatException {
        super(info, page);
        toIndex = info.getToIndex();
        if (toIndex >= page.getSize()
                || (begIndex < toIndex && endIndex > toIndex))
            throw new FalseInputFormatException();
    }

    @Override
    public boolean run() {
        if (toIndex == -1){
            toIndex = page.getCurrLine();
        }
        page.saveCurrent();
        if (toIndex > endIndex)
            toIndex = toIndex - (endIndex - begIndex + 1);
        else if (toIndex == endIndex){
            toIndex = endIndex - 1;
        }
        ArrayList<String> l = new ArrayList<>();
        for (int i = begIndex; i <= endIndex; i++){
            l.add(page.getLine(begIndex));
            page.deleteLine(begIndex);
        }
        for (String s: l){
            toIndex++;
            page.addLine(toIndex, s);
        }
        page.setCurrLine(toIndex);
        page.isSaved = false;
        return true;
    }
}
