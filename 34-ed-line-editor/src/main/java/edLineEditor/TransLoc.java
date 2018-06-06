package edLineEditor;

public class TransLoc {
    static String transLoc(String commandLine, Editor editor){
        int currentLine = editor.page.getCurrLine();
        if (times(commandLine, '/') % 2 != 0){             // 解决替换时'/'产生的冲突
            int count = 0;
            int index = 0;
            for (int i = commandLine.length() - 1; i >= 0 ; i--){
                if (commandLine.charAt(i) == '/') count++;
                if (count == 3) {
                    index = i;
                    break;
                }
            }
            commandLine = commandLine.substring(0, index);
        }
        if (commandLine.charAt(0) == ';') {
            String command = commandLine.substring(1, commandLine.length());
            editor.setLines(currentLine, editor.page.currPage.size());
            return Integer.toString(currentLine) + "," + Integer.toString(currentLine) + " " + command;
        }
        while (commandLine.contains("'")){                // 转化标记符
            int i = commandLine.indexOf("'");
            String o = commandLine.substring(i, i + 2);
            int lineNumber = editor.page.getMark(o.charAt(1));
            commandLine = commandLine.replace(o, Integer.toString(lineNumber));
        }
        while (commandLine.contains("/")){              // 转化匹配字符
            int i1 = commandLine.indexOf("/");
            int i2 = commandLine.indexOf("/", i1 + 1);
            String s = commandLine.substring(i1, i2 + 1);
            int lineNumber = editor.page.findDownLineNumber(s.substring(1, s.length() - 1));
            commandLine = commandLine.replace(s, Integer.toString(lineNumber));
        }
        while (commandLine.contains("?")){
            int i1 = commandLine.indexOf("?");
            int i2 = commandLine.indexOf("?", i1 + 1);
            String s = commandLine.substring(i1, i2 + 1);
            int lineNumber = editor.page.findUpLineNumber(s.substring(1, s.length() - 1));
            commandLine = commandLine.replace(s, Integer.toString(lineNumber));
        }
        while (commandLine.contains(".")){
            if (commandLine.contains(".-")){
                commandLine = commandLine.substring(1, commandLine.length());
            }
            else {
                commandLine = commandLine.replace(".", Integer.toString(editor.page.getCurrLine()));
            }
        }
        while (commandLine.contains("$")){
            commandLine = commandLine.replace("$", Integer.toString(editor.page.currPage.size()));
        }
        if (commandLine.contains(",") && (Character.isAlphabetic(commandLine.charAt(commandLine.indexOf(",") + 1))
                || commandLine.charAt(commandLine.indexOf(",") + 1) == '=')){
            commandLine = commandLine.replace(",", String.format("1,%d", editor.page.currPage.size()));
        }
        if (Character.isAlphabetic(commandLine.charAt(0)) || commandLine.charAt(0) == '='){
            commandLine = String.format("%d,%d", currentLine, currentLine) + commandLine;
        }
        if (commandLine.contains("-")){                   // 处理减号
            int index = 0;
            for (int i = 0; i < commandLine.length(); i++){
                if (Character.isAlphabetic(commandLine.charAt(i))) break;
                index++;
            }
            String loc = commandLine.substring(0, index);

            if (loc.contains(",")){
                String s1 = loc.split(",")[0];
                String s2 = loc.split(",")[1];
                if (s1.contains("-")){
                    int beginIndex;
                    int endIndex;
                    if (s1.indexOf("-") == 0) {
                        beginIndex = editor.page.currPage.size();
                    }
                    else {
                        beginIndex = Integer.parseInt(s1.split("-")[0]);
                    }
                    endIndex = Integer.parseInt(s1.split("-")[1]);
                    s1 = Integer.toString(beginIndex - endIndex);
                }
                if (s2.contains("-")){
                    int beginIndex;
                    int endIndex;
                    if (s2.indexOf("-") == 0) {
                        beginIndex = editor.page.currPage.size();
                    }
                    else {
                        beginIndex = Integer.parseInt(s2.split("-")[0]);
                    }
                    endIndex = Integer.parseInt(s2.split("-")[1]);
                    s2 = Integer.toString(beginIndex - endIndex);
                }
                commandLine = s1 + "," + s2 + commandLine.substring(index, commandLine.length());
            }
            else {
                if (loc.contains("-")){
                    int beginIndex;
                    int endIndex;
                    if (loc.indexOf("-") == 0) {
                        beginIndex = editor.page.currPage.size();
                    }
                    else {
                        beginIndex = Integer.parseInt(loc.split("-")[0]);
                    }
                    endIndex = Integer.parseInt(loc.split("-")[1]);
                    loc = Integer.toString(beginIndex - endIndex);
                }
                commandLine = loc + commandLine.substring(index, commandLine.length());
            }
        }

        if (commandLine.contains("+")){
            int index = 0;
            for (int i = 0; i < commandLine.length(); i++){
                if (Character.isAlphabetic(commandLine.charAt(i))) break;
                index++;
            }
            String loc = commandLine.substring(0, index);

            if (loc.contains(",")){
                String s1 = loc.split(",")[0];
                String s2 = loc.split(",")[1];
                if (s1.contains("+")){
                    int beginIndex;
                    int endIndex;
                    if (s1.indexOf("+") == 0) {
                        beginIndex = editor.page.currPage.size();
                    }
                    else {
                        beginIndex = Integer.parseInt(s1.split("\\+")[0]);
                    }
                    endIndex = Integer.parseInt(s1.split("\\+")[1]);
                    s1 = Integer.toString(beginIndex + endIndex);
                }
                if (s2.contains("+")){
                    int beginIndex;
                    int endIndex;
                    if (s2.indexOf("+") == 0) {
                        beginIndex = editor.page.currPage.size();
                    }
                    else {
                        beginIndex = Integer.parseInt(s2.split("\\+")[0]);
                    }
                    endIndex = Integer.parseInt(s2.split("\\+")[1]);
                    s2 = Integer.toString(beginIndex + endIndex);
                }
                commandLine = s1 + "," + s2 + commandLine.substring(index, commandLine.length());
            }
            else {
                if (loc.contains("+")){
                    int beginIndex;
                    int endIndex;
                    if (loc.indexOf("+") == 0) {
                        beginIndex = editor.page.currPage.size();
                    }
                    else {
                        beginIndex = Integer.parseInt(loc.split("\\+")[0]);
                    }
                    endIndex = Integer.parseInt(loc.split("\\+")[1]);
                    loc = Integer.toString(beginIndex + endIndex);
                }
                commandLine = loc + commandLine.substring(index, commandLine.length());
            }
        }
        if (commandLine.contains(",") && Character.isDigit(commandLine.charAt(commandLine.indexOf(",") - 1))
                && (Character.isAlphabetic(commandLine.charAt(commandLine.indexOf(",") + 1))
                || commandLine.charAt(commandLine.indexOf(",") + 1) == '=')){
            String old = commandLine.substring(commandLine.indexOf(",") - 1, commandLine.indexOf(",") + 1);
            String New = old + Integer.toString(currentLine);
            commandLine = commandLine.replace(old, New);
        }
        if (commandLine.charAt(0) == ',' && Character.isDigit(commandLine.charAt(commandLine.indexOf(",") + 1))){
            String old = commandLine.substring(0, 2);
            String New = old + Integer.toString(currentLine);
            commandLine = commandLine.replace(old, New);
        }

        if (!commandLine.contains(",")){
            int i;
            for (i = 0; i < commandLine.length(); i++){
                if (Character.isAlphabetic(commandLine.charAt(i)) || commandLine.charAt(i) == '='){
                    break;
                }
            }
            String num = commandLine.substring(0, i);
            String New = num + "," + num;
            commandLine = New + commandLine.substring(i, commandLine.length());
        }
        int i;
        for (i = 0; i < commandLine.length(); i++){
            if (Character.isAlphabetic(commandLine.charAt(i)) || commandLine.charAt(i) == '='){
                break;
            }
        }
        commandLine = commandLine.substring(0, i) + " " + commandLine.substring(i, commandLine.length());

        String[] loc = commandLine.split(" ")[0].split(",");
        int beginIndex = Integer.parseInt(loc[0]);
        int endIndex = Integer.parseInt(loc[1]);

        editor.setLines(beginIndex, endIndex);

        return commandLine;
    }

    private static int times(String s, char c){
        int n = 0;
        for (int i = 0; i < s.length(); i++){
            if (s.charAt(i) == c) n++;
        }
        return n;
    }

    public static void main(String[] args){
    }
}
