package edLineEditor.Commands;

import edLineEditor.FalseInputFormatException;
import edLineEditor.LocInfo;
import edLineEditor.Page;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SaveFile extends Command {
    private boolean isAdd;
    public SaveFile(LocInfo info, Page page) throws FalseInputFormatException {
        super(info, page);
        if (info.fileName().equals("") && page.getFilename().equals(""))
            throw new FalseInputFormatException();
        isAdd = info.getCommand() != 'w';
        if (info.isDefaultLoc()){           // 默认保存全文
            begIndex = 0;
            endIndex = page.getSize() - 1;
        }
    }

    @Override
    public void run() {
        if (info.fileName().equals("")){
            saveFile(page.getFilename(), isAdd);
        }
        else {
            saveFile(info.fileName(), isAdd);
        }
    }

    private void saveFile(String fileName, boolean isAdd){               // 保存指定行
        File file = new File(fileName);
        FileWriter fw;
        try {
            fw = new FileWriter(file, isAdd);
            for (int i = begIndex; i <= endIndex; i++){
                try {
                    fw.write(page.getLine(i) + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        page.isSaved = true;
    }
}
