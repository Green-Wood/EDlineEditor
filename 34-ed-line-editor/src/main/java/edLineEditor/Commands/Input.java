package edLineEditor.Commands;

import edLineEditor.FalseInputFormatException;
import edLineEditor.LocInfo;
import edLineEditor.Page;

import java.util.Scanner;

public class Input extends Command{
    private int startWritingIndex;
    public Input(LocInfo info, Page page) throws FalseInputFormatException {
        super(info, page);
        if (info.getCommand() == 'c'){
            if (begIndex == -1)
                throw new FalseInputFormatException();

            page.saveCurrent();
            for (int i = begIndex; i <= endIndex; i++){
                page.deleteLine(begIndex);
            }
            startWritingIndex = begIndex;
            this.page = page;
        }
        else {
            page.saveCurrent();
            // 追加在指定行后面
            if (info.getCommand() == 'a'){
                startWritingIndex = begIndex + 1;
            }
            // 追加在指定行前面
            else {
                if (begIndex == -1) startWritingIndex = 0;
                else startWritingIndex = begIndex;
            }
            this.page = page;
        }
    }

    public void insert(Scanner in){
        while (in.hasNextLine()){
            String line = new String(in.nextLine());
            if (line.equals(".")) break;            // 检测到句号时退出
            page.addLine(startWritingIndex, line);
            startWritingIndex++;
        }
        page.setCurrLine(startWritingIndex - 1);
        page.isSaved = false;
    }

    @Override
    public boolean run(){
        return true;
    }

}
