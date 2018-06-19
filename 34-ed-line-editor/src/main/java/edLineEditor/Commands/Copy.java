package edLineEditor.Commands;

import edLineEditor.FalseInputFormatException;
import edLineEditor.CommandInfo;
import edLineEditor.Page;

import java.util.ArrayList;

public class Copy extends Command {
    private int toIndex;
    public Copy(CommandInfo info, Page page) throws FalseInputFormatException {
        super(info, page);
        toIndex = info.getToIndex();
        if (toIndex >= page.getSize())
            throw new FalseInputFormatException();
    }

    @Override
    public void execute() {
        if (toIndex == -1){
            toIndex = page.getCurrLine();
        }
        page.saveCurrent();
        ArrayList<String> l = new ArrayList<>();
        for (int i = begIndex; i <= endIndex; i++){
            String s = new String(page.getLine(i));
            l.add(s);
        }
        for (String s: l){
            toIndex++;
            page.addLine(toIndex, s);
        }
        page.setCurrLine(toIndex);
        page.isSaved = false;
    }
}
