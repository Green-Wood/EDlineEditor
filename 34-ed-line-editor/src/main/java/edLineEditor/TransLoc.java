package edLineEditor;

import java.util.LinkedList;

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
        if (commandLine.contains(",") && (Character.isAlphabetic(commandLine.charAt(commandLine.indexOf(",") + 1))
                || commandLine.charAt(commandLine.indexOf(",") + 1) == '=')){
            commandLine = commandLine.replace(",", String.format("1,%d", editor.page.currPage.size()));
        }
        if (Character.isAlphabetic(commandLine.charAt(0)) || commandLine.charAt(0) == '='){
            commandLine = String.format("%d,%d", currentLine, currentLine) + commandLine;
        }
        while (commandLine.contains("-")){
            if (commandLine.contains("$-")){
                int i = commandLine.indexOf("$-");
                int n = Integer.parseInt(commandLine.substring(i + 2, i + 3));
                String formal = commandLine.substring(i, i + 3);
                String newS = Integer.toString(editor.page.currPage.size() - n);
                commandLine = commandLine.replace(formal, newS);
            }
            else {                      // 在当前行进行修改
                int i = commandLine.indexOf("-");
                int n = Integer.parseInt(commandLine.substring(i + 1, i + 2));
                String formal = commandLine.substring(i, i + 2);
                String newS = Integer.toString(currentLine - n);
                commandLine = commandLine.replace(formal, newS);
            }
        }
        while (commandLine.contains("+")){
            if (commandLine.contains("$+")){
                int i = commandLine.indexOf("$+");
                int n = Integer.parseInt(commandLine.substring(i + 2, i + 3));
                String formal = commandLine.substring(i, i + 3);
                String newS = Integer.toString(editor.page.currPage.size() + n);
                commandLine = commandLine.replace(formal, newS);
            }
            else {                      // 在当前行进行修改
                int i = commandLine.indexOf("+");
                int n = Integer.parseInt(commandLine.substring(i + 1, i + 2));
                String formal = commandLine.substring(i, i + 2);
                String newS = Integer.toString(currentLine + n);
                commandLine = commandLine.replace(formal, newS);
            }
        }
        if (commandLine.contains("$")){
            commandLine = commandLine.replace("$", Integer.toString(editor.page.currPage.size()));
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
        LinkedList<String> a = new LinkedList<>();
        LinkedList<String> b = new LinkedList<>();
        a.add("love");
        b.add("love");
        System.out.println(a.equals(b));
    }
}
